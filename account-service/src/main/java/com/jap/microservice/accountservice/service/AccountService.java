package com.jap.microservice.accountservice.service;

import com.jap.microservice.accountservice.db.entity.Account;
import com.jap.microservice.accountservice.db.entity.TempAccount;
import com.jap.microservice.accountservice.db.repository.AccountRepository;
import com.jap.microservice.accountservice.db.repository.TempAccountRepository;
import com.jap.microservice.accountservice.dto.RegisterCheckDto;
import com.jap.microservice.accountservice.dto.RegisterPasswordDto;
import com.jap.microservice.accountservice.dto.RegisterVerificationDto;
import com.jap.microservice.accountservice.feignclient.OTPClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author jap
 * @since 2020.08
 */

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TempAccountRepository tempAccountRepository;
    private final OTPClient otpClient;

    @Autowired
    public AccountService(AccountRepository accountRepository, TempAccountRepository tempAccountRepository, OTPClient otpClient) {
        this.accountRepository = accountRepository;
        this.tempAccountRepository = tempAccountRepository;
        this.otpClient = otpClient;
    }

    public ResponseEntity<?> registerCheck(RegisterCheckDto registerCheckDto) {
        // check db
        Account firstByEmail = accountRepository.getFirstByEmail(registerCheckDto.getEmail());
        if (firstByEmail!=null) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        // check redis
        TempAccount tempAccount = tempAccountRepository.getFirstByEmail(registerCheckDto.getEmail());
        if (tempAccount!=null) return ResponseEntity.ok().build();

        // save temp account
        tempAccount = new TempAccount();
        tempAccount.setEmail(registerCheckDto.getEmail());
        tempAccount.setValid(false);
        tempAccountRepository.save(tempAccount);

        // request otp
        try {
            otpClient.requestOTP(registerCheckDto);
        } catch (FeignException.FeignClientException ex) {
            return ResponseEntity.status(ex.status()).body(ex.contentUTF8());
        }

        return ResponseEntity.ok().build();
    }

    public String testLoadBalancer() {
        return otpClient.testLoadBalancer();
    }

    public ResponseEntity<?> verification(RegisterVerificationDto registerVerificationDto) {
        // check redis
        TempAccount tempAccount = tempAccountRepository.getFirstByEmail(registerVerificationDto.getEmail());
        if (tempAccount==null) return ResponseEntity.notFound().build();

        // verification otp
        try {
            otpClient.verificationOTP(registerVerificationDto);
        } catch (FeignException.FeignClientException ex) {
            ex.printStackTrace();
            return ResponseEntity.unprocessableEntity().build();
        }

        // update verification
        tempAccount.setValid(true);
        tempAccountRepository.save(tempAccount);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> registerPassword(RegisterPasswordDto registerPasswordDto) {
        // check redis
        TempAccount tempAccount = tempAccountRepository.getFirstByEmail(registerPasswordDto.getEmail());
        if (tempAccount==null) return ResponseEntity.notFound().build();

        // verification valid
        if (!tempAccount.isValid()) return ResponseEntity.unprocessableEntity().build();

        // save to postgres
        Account account = new Account();
        account.setEmail(registerPasswordDto.getEmail());
        account.setPassword(registerPasswordDto.getPassword());
        try {
            accountRepository.save(account);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.unprocessableEntity().build();
        }
        
        // delete temp account
        tempAccountRepository.delete(tempAccount);

        return ResponseEntity.ok().build();
    }
}

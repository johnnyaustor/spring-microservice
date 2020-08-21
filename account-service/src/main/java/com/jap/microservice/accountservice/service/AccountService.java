package com.jap.microservice.accountservice.service;

import com.jap.microservice.accountservice.db.entity.Account;
import com.jap.microservice.accountservice.db.entity.TempAccount;
import com.jap.microservice.accountservice.db.repository.AccountRepository;
import com.jap.microservice.accountservice.db.repository.TempAccountRepository;
import com.jap.microservice.accountservice.dto.RegisterCheckDto;
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
}

package com.jap.microservice.accountservice.controller;

import com.jap.microservice.accountservice.dto.RegisterCheckDto;
import com.jap.microservice.accountservice.dto.RegisterPasswordDto;
import com.jap.microservice.accountservice.dto.RegisterVerificationDto;
import com.jap.microservice.accountservice.service.AccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jap
 * @since 2020.08
 */

@Log4j2
@RestController
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/check")
    public ResponseEntity<?> registerCheck(@RequestBody RegisterCheckDto register) {
        log.debug("register {}", register);
        return accountService.registerCheck(register);
    }

    @PostMapping("/verification")
    public ResponseEntity<?> verification(@RequestBody RegisterVerificationDto registerVerificationDto) {
        return accountService.verification(registerVerificationDto);
    }

    @PostMapping("/password")
    public ResponseEntity<?> registerPassword(@RequestBody RegisterPasswordDto registerPasswordDto) {
        return accountService.registerPassword(registerPasswordDto);
    }

    @GetMapping("/load")
    public String testLoadBalancer() {
        return accountService.testLoadBalancer();
    }
}

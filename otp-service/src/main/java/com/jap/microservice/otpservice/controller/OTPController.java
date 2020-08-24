package com.jap.microservice.otpservice.controller;

import com.jap.microservice.otpservice.dto.RegisterCheckDto;
import com.jap.microservice.otpservice.service.OTPService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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
public class OTPController {

    @Value("${db.string}")
    private String dbString;

    private final OTPService otpService;
    private final Environment environment;

    @Autowired
    public OTPController(OTPService otpService, Environment environment) {
        this.otpService = otpService;
        this.environment = environment;
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestOTP(@RequestBody RegisterCheckDto register) {
        log.debug("request OTP: {}", register);
        otpService.requestOTP(register);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test-loadbalancer")
    public String testLoadBalancer() {
        String port = environment.getProperty("local.server.port");
        log.debug("port:{}", port);
        return "oke with port:" + port;
    }

    @GetMapping("/test-profile-config")
    public String testProfileConfig() {
        return dbString;
    }
}

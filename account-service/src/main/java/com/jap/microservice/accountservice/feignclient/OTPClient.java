package com.jap.microservice.accountservice.feignclient;

import com.jap.microservice.accountservice.dto.RegisterCheckDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author jap
 * @since 2020.08
 */

@FeignClient(value = "otp")
public interface OTPClient {
    @PostMapping("/request")
    ResponseEntity<?> requestOTP(@RequestBody RegisterCheckDto register);

    @GetMapping("/test-loadbalancer")
    String testLoadBalancer();
}

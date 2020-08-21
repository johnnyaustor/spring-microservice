package com.jap.microservice.accountservice.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author jap
 * @since 2020.08
 */

@Configuration
@EnableJpaAuditing
@EnableFeignClients("com.jap.microservice.accountservice.feignclient")
public class AppConfig {
    
}

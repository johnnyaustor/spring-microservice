package com.jap.microservice.otpservice.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * @author jap
 * @since 2020.08
 */

@Data
@RedisHash(value = "otp", timeToLive = 300)
public class TempOTP {
    @Id
    private String id;
    private String otp;
    @Indexed
    private String email;
}

package com.jap.microservice.accountservice.db.entity;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

/**
 * @author jap
 * @since 2020.08
 */

@Data
@RedisHash(value = "account", timeToLive = 3600)
public class TempAccount {
    @Id
    private String id;
    @Indexed
    private String email;
    private boolean valid = false;
}

package com.jap.microservice.accountservice.dto;

import lombok.Data;

/**
 * @author jap
 * @since 2020.08
 */

@Data
public class RegisterPasswordDto {
    private String email;
    private String password;
}

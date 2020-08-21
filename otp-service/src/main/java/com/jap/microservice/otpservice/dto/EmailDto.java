package com.jap.microservice.otpservice.dto;

import lombok.Data;

/**
 * @author jap
 * @since 2020.08
 */

@Data
public class EmailDto {
    private String to;
    private String subject;
    private String body;
}

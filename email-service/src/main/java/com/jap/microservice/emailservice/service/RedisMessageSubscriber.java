package com.jap.microservice.emailservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jap.microservice.emailservice.dto.EmailDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

/**
 * @author jap
 * @since 2020.08
 */

@Log4j2
@Service
public class RedisMessageSubscriber implements MessageListener {

    private final EmailService emailService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public RedisMessageSubscriber(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        try {
            String msg = message.toString();
            log.debug("message {}", msg);
            EmailDto emailDto = objectMapper.readValue(msg, EmailDto.class);
            emailService.sendEmail(emailDto.getTo(), emailDto.getSubject(), emailDto.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

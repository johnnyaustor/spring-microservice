package com.jap.microservice.otpservice.service;

import com.jap.microservice.otpservice.db.entity.TempOTP;
import com.jap.microservice.otpservice.db.repository.TempOTPRepository;
import com.jap.microservice.otpservice.dto.EmailDto;
import com.jap.microservice.otpservice.dto.RegisterCheckDto;
import com.jap.microservice.otpservice.dto.RegisterVerificationDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * @author jap
 * @since 2020.08
 */

@Log4j2
@Service
public class OTPService {
    private final TempOTPRepository tempOTPRepository;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;

    @Autowired
    public OTPService(TempOTPRepository tempOTPRepository, RedisTemplate redisTemplate, ChannelTopic channelTopic) {
        this.tempOTPRepository = tempOTPRepository;
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
    }

    public void requestOTP(RegisterCheckDto registerCheckDto) {
        String email = registerCheckDto.getEmail();
        // check otp
        TempOTP otpByEmail = tempOTPRepository.getFirstByEmail(email);
        if (otpByEmail!=null) {
            tempOTPRepository.delete(otpByEmail);
        }

        // generate random number
        String randomOTP = generateOTP();

        // save to redis
        TempOTP tempOTP = new TempOTP();
        tempOTP.setEmail(email);
        tempOTP.setOtp(randomOTP);
        tempOTPRepository.save(tempOTP);

        // send message broker
        sendEmail(email, "Kode Verifikasi Anda: " + randomOTP);
    }

    private void sendEmail(String to, String body) {
        log.debug("to: {}, body: {}", to, body);
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(to);
        emailDto.setSubject("Kode Verifikasi");
        emailDto.setBody(body);
        redisTemplate.convertAndSend(channelTopic.getTopic(), emailDto);
    }

    private String generateOTP() {
        return new DecimalFormat("0000").format(new Random().nextInt(9999));
    }

    public ResponseEntity<?> verificationOTP(RegisterVerificationDto registerVerificationDto) {
        // check by email
        TempOTP tempOTPByEmail = tempOTPRepository.getFirstByEmail(registerVerificationDto.getEmail());
        if (tempOTPByEmail==null) return ResponseEntity.notFound().build();

        // verification otp
        if (!tempOTPByEmail.getOtp().equals(registerVerificationDto.getOtp())) return ResponseEntity.unprocessableEntity().build();

        return ResponseEntity.ok().build();
    }
}

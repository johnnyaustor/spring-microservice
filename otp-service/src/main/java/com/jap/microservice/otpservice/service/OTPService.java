package com.jap.microservice.otpservice.service;

import com.jap.microservice.otpservice.db.entity.TempOTP;
import com.jap.microservice.otpservice.db.repository.TempOTPRepository;
import com.jap.microservice.otpservice.dto.RegisterCheckDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public OTPService(TempOTPRepository tempOTPRepository) {
        this.tempOTPRepository = tempOTPRepository;
    }

    public void requestOTP(RegisterCheckDto registerCheckDto) {
        // check otp
        TempOTP otpByEmail = tempOTPRepository.getFirstByEmail(registerCheckDto.getEmail());
        if (otpByEmail!=null) {
            tempOTPRepository.delete(otpByEmail);
        }

        // generate random number
        String randomOTP = generateOTP();
        log.debug("Random OTP: {}", randomOTP);

        // save to db
        TempOTP tempOTP = new TempOTP();
        tempOTP.setEmail(registerCheckDto.getEmail());
        tempOTP.setOtp(randomOTP);
        tempOTPRepository.save(tempOTP);

        // send message broker
    }

    private String generateOTP() {
        return new DecimalFormat("0000").format(new Random().nextInt(9999));
    }
}

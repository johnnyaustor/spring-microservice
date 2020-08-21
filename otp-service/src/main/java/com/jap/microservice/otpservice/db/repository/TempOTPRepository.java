package com.jap.microservice.otpservice.db.repository;

import com.jap.microservice.otpservice.db.entity.TempOTP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author jap
 * @since 2020.08
 */

@Repository
public interface TempOTPRepository extends CrudRepository<TempOTP, String> {
    TempOTP getFirstByEmail(String email);
}

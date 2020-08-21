package com.jap.microservice.accountservice.db.repository;

import com.jap.microservice.accountservice.db.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author jap
 * @since 2020.08
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account getFirstByEmail(String email);
}

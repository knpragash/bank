package com.learn.accounts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.learn.accounts.entity.Accounts;

@Repository
public interface AccountRepository extends JpaRepository<Accounts, Long> {
    
    public Optional<Accounts> findByCustomerId(Long customerId);

    @Transactional
    @Modifying
    public void deleteByCustomerId(Long customerId);
}

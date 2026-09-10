package com.task.breem.data.repository;

import com.task.breem.data.models.Account;
import com.task.breem.data.models.Enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByUserId(UUID userId);
    Optional<Account> findByUserIdAndAccountType(UUID userId, AccountType accountType);
    boolean existsByAccountNumber(String accountNumber);
}

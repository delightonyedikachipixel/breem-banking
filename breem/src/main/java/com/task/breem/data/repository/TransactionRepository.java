package com.task.breem.data.repository;

import com.task.breem.data.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByReference(String reference);
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(UUID accountId);
    List<Transaction> findByAccountIdAndCategoryIgnoreCase(UUID accountId, String category);
}

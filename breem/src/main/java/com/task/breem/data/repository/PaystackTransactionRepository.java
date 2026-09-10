package com.task.breem.data.repository;

import com.task.breem.data.models.PaystackTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaystackTransactionRepository extends JpaRepository<PaystackTransaction, UUID> {
    Optional<PaystackTransaction> findByPaystackReference(String paystackReference);
    List<PaystackTransaction> findByUserIdOrderByCreatedAtDesc(UUID userId);
}

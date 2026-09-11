package com.task.breem.data.repository;

import com.task.breem.data.models.LoanRepayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanRepaymentRepository extends JpaRepository<LoanRepayment, UUID> {
}

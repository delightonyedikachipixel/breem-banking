package com.task.breem.data.models;

import com.task.breem.data.models.Enums.LoanRepaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "loan_repayments")
public class LoanRepayment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    private BigDecimal amount;

    private LocalDateTime dueDate;

    private LocalDateTime paidDate;

    @Enumerated(EnumType.STRING)
    private LoanRepaymentStatus status =  LoanRepaymentStatus.PENDING;
}

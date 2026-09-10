package com.task.breem.data.models;

import com.task.breem.data.models.Enums.PaystackPurpose;
import com.task.breem.data.models.Enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "paystack_transactions")
public class PaystackTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(unique = true, nullable = false)
    private String paystackReference;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaystackPurpose purpose;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;

    private String authorizationCode;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}

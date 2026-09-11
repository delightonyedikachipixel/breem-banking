package com.task.breem.data.models;

import com.task.breem.data.models.Enums.TransferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    private String receiverAccountNumber;

    private int receiverBankCode;

    private String nibbsSessionId;

    private TransferStatus status;
}

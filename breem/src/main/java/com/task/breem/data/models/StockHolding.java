package com.task.breem.data.models;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "stock_holdings")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockHolding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    private int quantity;

    private BigDecimal price;
}

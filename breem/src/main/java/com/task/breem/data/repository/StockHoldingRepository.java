package com.task.breem.data.repository;

import com.task.breem.data.models.StockHolding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockHoldingRepository extends JpaRepository<StockHolding, UUID> {
}

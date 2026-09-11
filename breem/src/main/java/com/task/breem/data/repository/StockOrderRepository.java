package com.task.breem.data.repository;

import com.task.breem.data.models.StockOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockOrderRepository extends JpaRepository<StockOrder, UUID> {
}

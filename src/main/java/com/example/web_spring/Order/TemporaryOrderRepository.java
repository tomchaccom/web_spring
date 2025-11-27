package com.example.web_spring.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryOrderRepository extends JpaRepository<TemporaryOrder, Long> {
}

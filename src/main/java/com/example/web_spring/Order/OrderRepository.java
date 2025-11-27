package com.example.web_spring.Order;

import com.example.web_spring.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberOrderByIdDesc(Member member);
}

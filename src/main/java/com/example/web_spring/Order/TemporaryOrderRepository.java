package com.example.web_spring.Order;

import com.example.web_spring.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemporaryOrderRepository extends JpaRepository<TemporaryOrder, Long> {
    Optional<TemporaryOrder> findTopByMemberOrderByIdDesc(Member member);
    Optional<TemporaryOrder> findByMember(Member member);
}

package com.example.web_spring.Cart;

import com.example.web_spring.Member.Member;
import com.example.web_spring.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByMember(Member member);

    Optional<Cart> findByMemberAndProduct(Member member, Product product);

    void deleteByMemberAndProduct(Member member, Product product);
}
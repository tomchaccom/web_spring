package com.example.web_spring.Wish;

import com.example.web_spring.Member.Member;
import com.example.web_spring.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByMemberUsername(String username);
    boolean existsByMemberAndProduct(Member member, Product product);
}

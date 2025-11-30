package com.example.web_spring.Coupon;

import com.example.web_spring.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByMemberAndUsedFalse(Member member);

}

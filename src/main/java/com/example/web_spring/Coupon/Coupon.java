package com.example.web_spring.Coupon;

import com.example.web_spring.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int discountAmount; // 10000
    private boolean used = false;

    public Coupon(Member member, int discountAmount) {
        this.member = member;
        this.discountAmount = discountAmount;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}

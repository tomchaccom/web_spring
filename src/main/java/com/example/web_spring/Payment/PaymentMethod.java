package com.example.web_spring.Payment;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CARD("신용/체크카드"),       // 신용/체크카드
    BANK("계좌이체"),       // 계좌이체
    KAKAO("카카오페이");      // 카카오페이

    private final String korean;

    PaymentMethod(String korean) {
        this.korean = korean;
    }
}

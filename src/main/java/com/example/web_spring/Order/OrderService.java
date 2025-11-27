package com.example.web_spring.Order;

import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final TemporaryOrderRepository temporaryOrderRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveTemporaryOrder(OrderFormDto form, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보 없음"));

        TemporaryOrder temp = TemporaryOrder.create(member, form);

        temporaryOrderRepository.save(temp);
    }
}

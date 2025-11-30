package com.example.web_spring.admin.Service;

import com.example.web_spring.Inquiry.Inquiry;
import com.example.web_spring.Inquiry.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryAdminService {

    private final InquiryRepository inquiryRepository;

    /** 문의 전체 조회 (관리자 화면용) */
    public List<Inquiry> findAll() {
        return inquiryRepository.findAllByOrderByCreatedAtDesc();
    }

    /** 문의 상세 조회 */
    public Inquiry findById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문의가 존재하지 않습니다."));
    }

    /** 관리자 답변 저장 */
    @Transactional
    public void answerInquiry(Long id, String answer) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문의가 존재하지 않습니다."));

        inquiry.setAnswer(answer); // 답변 저장
    }
}

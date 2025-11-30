package com.example.web_spring.Inquiry;

import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;
    // 문의 이미지 저장 경로 (리뷰와 동일한 최상위 폴더 구조)
    private final String uploadDir = System.getProperty("user.home") + "/uploads/inquiries/";

    @Transactional
    public void writeInquiry(String username, String title, String content, MultipartFile imageFile) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보 없음"));

        Inquiry inquiry = new Inquiry();
        inquiry.setMember(member);
        inquiry.setTitle(title);
        inquiry.setContent(content);



        // ⭐ 이미지 저장 처리
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs(); // 폴더 자동 생성

                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                File dest = new File(uploadDir + fileName);

                imageFile.transferTo(dest);

                inquiry.setImagePath("/uploads/inquiries/" + fileName);

            } catch (Exception e) {
                throw new RuntimeException("이미지 저장 실패", e);
            }
        }

        inquiryRepository.save(inquiry);
    }

    @Transactional(readOnly = true)
    public List<Inquiry> getMyInquiries(String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        return inquiryRepository.findByMemberOrderByCreatedAtDesc(member);
    }

    @Transactional
    public void deleteInquiry(Long inquiryId, String username) {

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의글을 찾을 수 없습니다."));

        // 본인 확인
        if (!inquiry.getMember().getUsername().equals(username)) {
            throw new IllegalArgumentException("본인만 삭제할 수 있습니다.");
        }

        // 이미지 파일도 삭제
        if (inquiry.getImagePath() != null) {
            File file = new File(System.getProperty("user.home") + inquiry.getImagePath());
            if (file.exists()) file.delete();
        }

        inquiryRepository.delete(inquiry);
    }

}

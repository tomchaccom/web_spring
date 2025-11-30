package com.example.web_spring.Review;

import com.example.web_spring.Product.Product;
import com.example.web_spring.Product.ProductRepository;
import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    /** 상품별 후기 조회 */
    public List<Review> getReviewsByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        return reviewRepository.findByProductOrderByCreatedAtDesc(product);
    }

    /** 후기 작성 */
    // 예: 사용자 홈폴더에 uploads/reviews 저장
    private final String uploadDir = System.getProperty("user.home") + "/uploads/reviews/";

    @Transactional
    public void writeReview(Long productId, String username, int rating, String content, MultipartFile imageFile) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        Review review = new Review();
        review.setMember(member);
        review.setProduct(product);
        review.setRating(rating);
        review.setContent(content);

        // ⭐ 이미지 저장 처리
        if (imageFile != null && !imageFile.isEmpty()) {

            try {
                // 디렉토리 없으면 생성
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                // 저장할 파일명 생성
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

                File dest = new File(uploadDir + fileName);
                imageFile.transferTo(dest);

                // DB에는 URL 형태로 저장
                review.setImagePath("/uploads/reviews/" + fileName);

            } catch (Exception e) {
                throw new RuntimeException("이미지 저장 중 오류 발생", e);
            }
        }

        reviewRepository.save(review);
    }



    /** 후기 삭제 (본인만 가능) */
    @Transactional
    public void deleteReview(Long reviewId, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("후기를 찾을 수 없습니다."));

        if (!review.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("본인만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }
}


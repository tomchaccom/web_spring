package com.example.web_spring.Review;

import com.example.web_spring.Member.Member;
import com.example.web_spring.Product.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    /** 특정 상품의 모든 후기 조회 */
    List<Review> findByProductOrderByCreatedAtDesc(Product product);

    /** 특정 사용자가 작성한 후기 조회 (삭제 권한 체크용) */
    List<Review> findByMember(Member member);
}

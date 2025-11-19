package com.example.web_spring.Review;

import com.example.web_spring.Comment.Comment;
import com.example.web_spring.Member.Member;
import com.example.web_spring.Product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자 ID (FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // 상품 ID (FK)

    private int rating; // 평점 (1~5)

    @Lob
    private String content; // 후기 내용

    private LocalDateTime createdAt; // 생성 일시

    // Comment와의 관계: 하나의 후기는 여러 개의 댓글을 가질 수 있음
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // 댓글 목록

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

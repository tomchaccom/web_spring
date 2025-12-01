package com.example.web_spring.Comment;

import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberRepository;
import com.example.web_spring.Review.Review;
import com.example.web_spring.Review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void addComment(Long reviewId, String username, String content) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰 없음"));

        Comment comment = Comment.create(member, review, content);

        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        if (!comment.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("본인만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

}

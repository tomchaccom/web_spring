package com.example.web_spring.admin.Controller;

import com.example.web_spring.Review.ReviewRepository;
import com.example.web_spring.Comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/cs")
public class AdminReviewController {

    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;

    // 전체 리뷰/댓글 조회
    @GetMapping("/reviews")
    public String reviewManage(Model model) {
        model.addAttribute("reviews", reviewRepository.findAll());
        return "admin/cs/review_manage";
    }

    // 리뷰 삭제
    @PostMapping("/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Long reviewId) {
        reviewRepository.deleteById(reviewId);
        return "redirect:/admin/cs/reviews";
    }

    // 댓글 삭제
    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId) {
        commentRepository.deleteById(commentId);
        return "redirect:/admin/cs/reviews";
    }
}


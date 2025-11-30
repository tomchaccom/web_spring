package com.example.web_spring.Review;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products/{productId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /** 후기 작성 */
    @PostMapping
    public String writeReview(@PathVariable Long productId,
                              @RequestParam int rating,
                              @RequestParam String content,
                              @AuthenticationPrincipal(expression = "username") String username) {

        reviewService.writeReview(productId, username, rating, content);
        return "redirect:/products/" + productId; // 상세페이지로 리다이렉트
    }

    /** 후기 삭제 */
    @PostMapping("/{reviewId}/delete")
    public String deleteReview(@PathVariable Long productId,
                               @PathVariable Long reviewId,
                               @AuthenticationPrincipal(expression = "username") String username) {

        reviewService.deleteReview(reviewId, username);
        return "redirect:/products/" + productId;
    }
}

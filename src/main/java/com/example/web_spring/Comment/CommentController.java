package com.example.web_spring.Comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/products/{productId}/reviews/{reviewId}/comment")
    public String addComment(@PathVariable Long productId,
                             @PathVariable Long reviewId,
                             @RequestParam String content,
                             Principal principal) {

        String username = principal.getName();
        commentService.addComment(reviewId, username, content);

        return "redirect:/products/" + productId;
    }

    @PostMapping("/products/{productId}/reviews/{reviewId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long productId,
                                @PathVariable Long reviewId,
                                @PathVariable Long commentId,
                                Principal principal) {

        String username = principal.getName();
        commentService.deleteComment(commentId, username);

        return "redirect:/products/" + productId;
    }

}

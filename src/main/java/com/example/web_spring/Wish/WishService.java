package com.example.web_spring.Wish;

import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberRepository;
import com.example.web_spring.Product.Product;
import com.example.web_spring.Product.ProductRepository;
import com.example.web_spring.Product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Transactional
    public void addWishList(Long productId, String username) {
        Product product = productService.getProductById(productId);

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("로그인 후 사용해주세요" + username));

        boolean exists = wishRepository.existsByMemberAndProduct(member, product);
        if (exists) {
            return; // 이미 추가되어 있다면 아무 작업도 하지 않음
        }

        Wish wish = new Wish(null, product, member);
        wishRepository.save(wish);
    }

    @Transactional(readOnly = true)
    public List<Wish> getWishList(String username) {
        return wishRepository.findByMemberUsername(username);
    }

    @Transactional
    public void removeWish(Long productId, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("<UNK> <UNK> <UNK>" + username)
        );
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        wishRepository.deleteByMemberAndProduct(member, product);
    }
}

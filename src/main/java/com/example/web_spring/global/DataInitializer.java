package com.example.web_spring.global;

import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberRepository;
import com.example.web_spring.Member.Role;
import com.example.web_spring.Category.Category;
import com.example.web_spring.Category.CategoryRepository;
import com.example.web_spring.Product.Product;
import com.example.web_spring.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // -----------------------
        // 1. 관리자 / 사용자 생성
        // -----------------------
        if (memberRepository.findByUsername("admin").isEmpty()) {
            Member admin = Member.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("1234"))
                    .email("admin@shop.com")
                    .phone("010-0000-0000")
                    .role(Role.ROLE_ADMIN)
                    .build();

            memberRepository.save(admin);
            System.out.println("✅ 관리자 계정 생성 완료 (admin / 1234)");
        }

        if (memberRepository.findByUsername("user").isEmpty()) {
            Member user = Member.builder()
                    .username("user")
                    .password(passwordEncoder.encode("1234"))
                    .email("user@shop.com")
                    .phone("010-1111-1111")
                    .role(Role.ROLE_USER)
                    .build();

            memberRepository.save(user);
            System.out.println("✅ 사용자 계정 생성 완료 (user / 1234)");
        }

        // -----------------------
        // 2. 카테고리 생성
        // -----------------------
        if (categoryRepository.count() == 0) {
            Category laptop = categoryRepository.save(new Category("노트북"));
            Category phone = categoryRepository.save(new Category("스마트폰"));
            Category monitor = categoryRepository.save(new Category("모니터"));
            Category accessory = categoryRepository.save(new Category("악세서리"));

            System.out.println("✅ 기본 카테고리 생성 완료");
        }

        // 카테고리 조회
        Category laptop = categoryRepository.findByName("노트북").orElse(null);
        Category phone = categoryRepository.findByName("스마트폰").orElse(null);
        Category monitor = categoryRepository.findByName("모니터").orElse(null);
        Category accessory = categoryRepository.findByName("악세서리").orElse(null);


        // -----------------------
        // 3. 상품 더미 데이터 생성
        // -----------------------
        if (productRepository.count() == 0) {

            Product p1 = Product.builder()
                    .name("아이폰 15")
                    .price(1200000)
                    .description("최신형 아이폰 15 모델")
                    .imageUrl("/images/iphone.png")
                    .stock(50)
                    .category(phone)
                    .build();

            Product p2 = Product.builder()
                    .name("맥북 프로")
                    .price(3200000)
                    .description("애플 M 시리즈 기반 고성능 노트북")
                    .imageUrl("/images/macbook.jpeg")
                    .stock(30)
                    .category(laptop)
                    .build();

            Product p3 = Product.builder()
                    .name("갤럭시 S24")
                    .price(1100000)
                    .description("삼성 최신 플래그십 스마트폰")
                    .imageUrl("/images/galaxy.jpeg")
                    .stock(40)
                    .category(phone)
                    .build();

            Product p4 = Product.builder()
                    .name("LG 모니터")
                    .price(450000)
                    .description("27인치 고해상도 모니터")
                    .imageUrl("/images/monitor.jpg")
                    .stock(70)
                    .category(monitor)
                    .build();

            Product p5 = Product.builder()
                    .name("에어팟 프로 2세대")
                    .price(320000)
                    .description("노이즈 캔슬링 무선 이어폰")
                    .imageUrl("/images/airpods.jpg")
                    .stock(30)
                    .category(accessory)   // 악세서리 카테고리
                    .build();


            productRepository.save(p1);
            productRepository.save(p2);
            productRepository.save(p3);
            productRepository.save(p4);
            productRepository.save(p5);

            System.out.println("✅ 상품 더미 데이터 생성 완료");
        }
    }
}

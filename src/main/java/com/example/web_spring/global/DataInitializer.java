package com.example.web_spring.global;

import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberRepository;
import com.example.web_spring.Member.Role;
import com.example.web_spring.Category.Category;
import com.example.web_spring.Category.CategoryRepository;
import com.example.web_spring.Product.Product;
import com.example.web_spring.Product.ProductRepository;
import com.example.web_spring.Order.Order;
import com.example.web_spring.Order.OrderRepository;
import com.example.web_spring.Order.OrderStatus;
import com.example.web_spring.OrderItem.OrderItem;
import com.example.web_spring.OrderItem.OrderItemRepository;
import com.example.web_spring.Delivery.Delivery;
import com.example.web_spring.Delivery.DeliveryRepository;
import com.example.web_spring.Delivery.DeliveryState;
import com.example.web_spring.Payment.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (memberRepository.findByUsername("admin").isEmpty()) {
            Member admin = Member.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("1234"))
                    .email("admin@shop.com")
                    .phone("010-0000-0000")
                    .role(Role.ROLE_ADMIN)
                    .build();
            memberRepository.save(admin);
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
        }

        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("노트북"));
            categoryRepository.save(new Category("스마트폰"));
            categoryRepository.save(new Category("모니터"));
            categoryRepository.save(new Category("악세서리"));

            System.out.println("✅ 기본 카테고리 생성 완료");
        }

        // 카테고리 조회
        Category laptop = categoryRepository.findByName("노트북").orElse(null);
        Category phone = categoryRepository.findByName("스마트폰").orElse(null);
        Category monitor = categoryRepository.findByName("모니터").orElse(null);
        Category accessory = categoryRepository.findByName("악세서리").orElse(null);


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
                    .category(accessory)
                    .build();

            productRepository.saveAll(List.of(p1, p2, p3, p4, p5));
        }


        // 주문 더미 데이터
        Member user = memberRepository.findByUsername("user").orElse(null);

        if (user != null && orderRepository.count() == 0) {

            Product phoneProduct = productRepository.findByName("아이폰 15").orElse(null);
            Product macProduct = productRepository.findByName("맥북 프로").orElse(null);

            // 주문 더미 생성
            Order order1 = new Order(
                    user,
                    "홍길동",
                    "010-2222-2222",
                    "서울시 강남구 테헤란로 101",
                    1200000L,
                    PaymentMethod.CARD,
                    OrderStatus.PAYMENT_COMPLETED
            );

            OrderItem item1 = new OrderItem(order1, phoneProduct, 1, phoneProduct.getPrice());
            order1.addOrderItem(item1);

            Delivery d1 = new Delivery(order1, "111-222-333", "서울시 강남구 테헤란로 101", DeliveryState.DELIVERED);
            order1.setDelivery(d1);

            orderRepository.save(order1);


            Order order2 = new Order(
                    user,
                    "홍길동",
                    "010-2222-2222",
                    "서울시 강남구 테헤란로 101",
                    3200000L,
                    PaymentMethod.CARD,
                    OrderStatus.PAYMENT_COMPLETED
            );


            OrderItem item2 = new OrderItem(order2, macProduct, 1, macProduct.getPrice());
            order2.addOrderItem(item2);

            Delivery d2 = new Delivery(order2, "준비중", "서울시 강남구 테헤란로 101", DeliveryState.READY);
            order2.setDelivery(d2);

            orderRepository.save(order2);

            System.out.println("더미 주문 데이터 2건 생성 완료 (배송완료 + 주문취소)");
        }
    }
}
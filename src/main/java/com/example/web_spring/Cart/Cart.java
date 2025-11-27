package com.example.web_spring.Cart;

import com.example.web_spring.Member.Member;
import com.example.web_spring.Product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private int price;

    public Cart(Member member, Product product, int quantity, int price) {
        this.member = member;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public void addQuantity() {
        this.quantity++;
    }

    public void removeQuantity() {
        if (this.quantity > 1) this.quantity--;
    }

}
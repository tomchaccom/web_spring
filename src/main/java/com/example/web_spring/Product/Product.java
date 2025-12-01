package com.example.web_spring.Product;

import com.example.web_spring.Category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;

    @Lob
    private String description;

    private String imageUrl;
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void reduceStock(int qty) {
        if (this.stock < qty) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.stock -= qty;
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
    }
    public void updateProduct(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public void changeImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void changeStock(int newStock) {
        if (newStock < 0) {
            throw new IllegalStateException("재고는 0 이상이어야 합니다.");
        }
        this.stock = newStock;
    }

}
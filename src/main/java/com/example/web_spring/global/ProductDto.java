package com.example.web_spring.global;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class ProductDto {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
}


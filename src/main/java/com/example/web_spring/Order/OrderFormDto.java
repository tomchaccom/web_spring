package com.example.web_spring.Order;

import lombok.Data;

@Data
public class OrderFormDto {

    private String receiver;
    private String phoneNumber;
    private String address;
}
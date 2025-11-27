package com.example.web_spring.OrderItem;

import com.example.web_spring.Order.Order;
import com.example.web_spring.Product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private int price;

    public static OrderItem create(Product product, int quantity, int price) {
        OrderItem item = new OrderItem();
        item.product = product;
        item.quantity = quantity;
        item.price = price;
        return item;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

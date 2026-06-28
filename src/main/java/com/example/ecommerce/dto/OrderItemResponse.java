package com.example.ecommerce.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {

    private Long productId;

    private String productTitle;

    private Integer quantity;

    private Double price;

    private Double subTotal;

}

package com.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemResponse {
    private Long cartItemId;

    private Long productId;

    private String productName;

    private Double productPrice;

    private Integer quantity;

    private Double subTotal;
}

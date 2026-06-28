package com.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CartResponse {
    private Long cartId;

    private Long customerId;

    private Double totalPrice;
    private List<CartItemResponse> items;

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }
}

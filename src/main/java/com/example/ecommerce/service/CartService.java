package com.example.ecommerce.service;

import com.example.ecommerce.dto.AddToCartRequest;
import com.example.ecommerce.dto.CartResponse;

public interface CartService {
    CartResponse addToCart(AddToCartRequest request);
    CartResponse updateCart(Long cartItemId, Integer quantity);
    CartResponse getMyCart();
    String removeCartItem(Long cartItemId);

    String clearCart();
}

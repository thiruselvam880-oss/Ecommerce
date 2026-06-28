package com.example.ecommerce.controller;

import com.example.ecommerce.dto.AddToCartRequest;
import com.example.ecommerce.dto.CartResponse;
import com.example.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Add Product to Cart
    @PostMapping("/add")
    public CartResponse addToCart(
            @Valid @RequestBody AddToCartRequest request) {

        return cartService.addToCart(request);
    }

    // View Logged-in User Cart
    @GetMapping
    public CartResponse getMyCart() {

        return cartService.getMyCart();
    }

    // Update Quantity
    @PutMapping("/{cartItemId}")
    public CartResponse updateCartItem(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {

        return cartService.updateCart(cartItemId, quantity);
    }

    // Remove Item
    @DeleteMapping("/{cartItemId}")
    public String removeCartItem(
            @PathVariable Long cartItemId) {

        return cartService.removeCartItem(cartItemId);
    }

    // Clear Cart
    @DeleteMapping("/clear")
    public String clearCart() {

        return cartService.clearCart();
    }

}

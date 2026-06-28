package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Checkout
    @PostMapping("/checkout")
    public OrderResponse checkout() {
        return orderService.checkout();
    }

    // Logged-in User Orders
    @GetMapping("/my-orders")
    public List<OrderResponse> getMyOrders() {
        return orderService.getMyOrders();
    }

    // Admin
    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Admin
    @PutMapping("/{orderId}")
    public OrderResponse updateStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {

        return orderService.updateOrderStatus(orderId, status);
    }
}
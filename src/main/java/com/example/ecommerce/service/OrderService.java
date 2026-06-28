package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse checkout();

    List<OrderResponse> getMyOrders();

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrderStatus(Long orderId, String status);
}

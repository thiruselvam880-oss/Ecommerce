package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderItemResponse;
import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserDetailsRepository userRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * Logged in User
     */
    private Users getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    /**
     * Logged in Customer
     */
    private Customer getCustomer() {

        Users user = getLoggedInUser();

        return customerRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));
    }

    /**
     * Checkout
     */
    @Override
    public OrderResponse checkout() {

        Customer customer = getCustomer();

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();

        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();

        double total = 0;

        for (CartItem cartItem : cart.getCartItems()) {

            Product product = cartItem.getProduct();

            if (product.getStocks() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        product.getTitle() + " is out of stock");
            }

            // reduce stock
            product.setStocks(
                    product.getStocks() - cartItem.getQuantity());

            productRepository.save(product);

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);

            orderItem.setQuantity(cartItem.getQuantity());

            orderItem.setPrice(cartItem.getPrice());

            orderItem.setSubTotal(cartItem.getSubTotal());

            orderItems.add(orderItem);

            total += cartItem.getSubTotal();
        }

        order.setOrderItems(orderItems);

        order.setTotalAmount(total);

        orderRepository.save(order);

        orderItemRepository.saveAll(orderItems);

        // Remaining checkout logic
        // (clear cart + return response)
        // will be added in Part 2
        // Save Order
        orderRepository.save(order);

        orderItemRepository.saveAll(orderItems);

// Clear Cart
        cartItemRepository.deleteAll(cart.getCartItems());

        cart.getCartItems().clear();

        cart.setTotalPrice(0.0);

        cartRepository.save(cart);

        return map(order);

    }

    private OrderResponse map(Order order) {

        OrderResponse response = new OrderResponse();

        response.setOrderId(order.getId());

        response.setOrderDate(order.getOrderDate());

        response.setStatus(order.getOrderStatus().name());

        response.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponse> items = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {

            OrderItemResponse dto = new OrderItemResponse();

            dto.setProductId(item.getProduct().getId());

            dto.setProductTitle(item.getProduct().getTitle());

            dto.setQuantity(item.getQuantity());

            dto.setPrice(item.getPrice());

            dto.setSubTotal(item.getSubTotal());

            items.add(dto);
        }

        response.setItems(items);

        return response;
    }

    @Override
    public List<OrderResponse> getMyOrders() {

        Customer customer = getCustomer();

        List<Order> orders =
                orderRepository.findByCustomer(customer);

        List<OrderResponse> responses =
                new ArrayList<>();

        for (Order order : orders) {
            responses.add(map(order));
        }

        return responses;
    }

    @Override
    public List<OrderResponse> getAllOrders() {

        List<Order> orders =
                orderRepository.findAll();

        List<OrderResponse> responses =
                new ArrayList<>();

        for (Order order : orders) {
            responses.add(map(order));
        }

        return responses;
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId,
                                           String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        order.setOrderStatus(
                OrderStatus.valueOf(status.toUpperCase()));

        orderRepository.save(order);

        return map(order);
    }
}
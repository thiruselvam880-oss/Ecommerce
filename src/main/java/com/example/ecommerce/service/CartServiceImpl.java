package com.example.ecommerce.service;

import com.example.ecommerce.dto.AddToCartRequest;
import com.example.ecommerce.dto.CartItemResponse;
import com.example.ecommerce.dto.CartResponse;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
    private final UserDetailsRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    /**
     * Get logged-in user from JWT
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
     * Get customer profile
     */
    private Customer getCustomer() {

        Users user = getLoggedInUser();

        return customerRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Customer profile not found"));
    }

    /**
     * Create cart automatically if it doesn't exist
     */
    private Cart getOrCreateCart(Customer customer) {

        Optional<Cart> cartOptional =
                cartRepository.findByCustomer(customer);

        if (cartOptional.isPresent()) {
            return cartOptional.get();
        }

        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setTotalPrice(0.0);

        return cartRepository.save(cart);
    }

    /**
     * Add Product To Cart
     */
    @Override
    public CartResponse addToCart(AddToCartRequest request) {

        Customer customer = getCustomer();

        Cart cart = getOrCreateCart(customer);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        Optional<CartItem> existingItem =
                cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {

            CartItem item = existingItem.get();

            item.setQuantity(
                    item.getQuantity() + request.getQuantity());

            item.setSubTotal(
                    item.getQuantity() * item.getPrice());

            cartItemRepository.save(item);

        } else {

            CartItem item = new CartItem();

            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());

            item.setPrice(product.getPrice());

            item.setSubTotal(
                    product.getPrice() * request.getQuantity());

            cartItemRepository.save(item);

            cart.getCartItems().add(item);
        }

        calculateTotal(cart);

        cartRepository.save(cart);

        return map(cart);

    }

    /**
     * View Logged-in Customer Cart
     */
    @Override
    public CartResponse getMyCart() {

        Customer customer = getCustomer();

        Cart cart = getOrCreateCart(customer);

        calculateTotal(cart);

        return map(cart);

    }

    /**
     * Calculate Total Price
     */
    private void calculateTotal(Cart cart) {

        double total = 0;

        for (CartItem item : cart.getCartItems()) {
            total += item.getSubTotal();
        }

        cart.setTotalPrice(total);
    }

    /**
     * Convert Entity -> DTO
     */
    private CartResponse map(Cart cart) {

        CartResponse response = new CartResponse();

        response.setCartId(cart.getId());
        response.setCustomerId(cart.getCustomer().getId());
        response.setTotalPrice(cart.getTotalPrice());

        ArrayList<CartItemResponse> items =
                new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {

            CartItemResponse dto =
                    new CartItemResponse();

            dto.setCartItemId(item.getId());

            dto.setProductId(
                    item.getProduct().getId());

            dto.setProductName(
                    item.getProduct().getTitle());

            dto.setProductPrice(
                    item.getPrice());

            dto.setQuantity(
                    item.getQuantity());

            dto.setSubTotal(
                    item.getSubTotal());

            items.add(dto);
        }



        return response;
    }
    @Override
    public CartResponse updateCart(Long cartItemId, Integer quantity) {

        Customer customer = getCustomer();

        Cart cart = getOrCreateCart(customer);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Invalid cart item");
        }

        Product product = item.getProduct();

        if (quantity > product.getStocks()) {
            throw new RuntimeException("Only " + product.getStocks() + " items available");
        }

        item.setQuantity(quantity);
        item.setSubTotal(quantity * item.getPrice());

        cartItemRepository.save(item);

        calculateTotal(cart);
        cartRepository.save(cart);

        return map(cart);
    }
    @Override
    public String removeCartItem(Long cartItemId) {

        Customer customer = getCustomer();

        Cart cart = getOrCreateCart(customer);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Invalid cart item");
        }

        cart.getCartItems().remove(item);

        cartItemRepository.delete(item);

        calculateTotal(cart);

        cartRepository.save(cart);

        return "Item removed successfully";
    }
    @Override
    public String clearCart() {

        Customer customer = getCustomer();

        Cart cart = getOrCreateCart(customer);

        cartItemRepository.deleteAll(cart.getCartItems());

        cart.getCartItems().clear();

        cart.setTotalPrice(0.0);

        cartRepository.save(cart);

        return "Cart cleared successfully";
    }
}

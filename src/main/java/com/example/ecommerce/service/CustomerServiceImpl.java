package com.example.ecommerce.service;

import com.example.ecommerce.dto.CustomerRequestDTO;
import com.example.ecommerce.dto.CustomerResponse;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.entity.Users;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
private final CustomerRepository customerRepository;
private final UserDetailsRepository userRepository;

private Users getLoggedInUser() {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    String username = authentication.getName();

    return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
}

@Override
public CustomerResponse createCustomer(CustomerRequestDTO request) {

    Users user = getLoggedInUser();

    if (customerRepository.findByUser(user).isPresent()) {
        throw new RuntimeException("Customer profile already exists");
    }

    Customer customer = new Customer();

    customer.setFirstName(request.getFirstName());
    customer.setLastName(request.getLastName());
    customer.setPhoneNumber(request.getPhoneNumber());
    customer.setAddress(request.getAddress());
    customer.setCity(request.getCity());
    customer.setState(request.getState());
    customer.setCountry(request.getCountry());
    customer.setPincode(request.getPincode());

    customer.setUser(user);

    customerRepository.save(customer);

    return map(customer);

}

@Override
public CustomerResponse updateCustomer(CustomerRequestDTO request) {

    Users user = getLoggedInUser();

    Customer customer = customerRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    customer.setFirstName(request.getFirstName());
    customer.setLastName(request.getLastName());
    customer.setPhoneNumber(request.getPhoneNumber());
    customer.setAddress(request.getAddress());
    customer.setCity(request.getCity());
    customer.setState(request.getState());
    customer.setCountry(request.getCountry());
    customer.setPincode(request.getPincode());

    customerRepository.save(customer);

    return map(customer);

}

@Override
public CustomerResponse getCustomer() {

    Users user = getLoggedInUser();

    Customer customer = customerRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    return map(customer);

}

private CustomerResponse map(Customer customer){

    CustomerResponse response = new CustomerResponse();

    response.setId(customer.getId());
    response.setFirstName(customer.getFirstName());
    response.setLastName(customer.getLastName());
    response.setPhoneNumber(customer.getPhoneNumber());
    response.setAddress(customer.getAddress());
    response.setCity(customer.getCity());
    response.setState(customer.getState());
    response.setCountry(customer.getCountry());
    response.setPincode(customer.getPincode());
    response.setUsername(customer.getUser().getUsername());

    return response;
}

}
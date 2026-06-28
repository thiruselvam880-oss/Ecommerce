package com.example.ecommerce.service;

import com.example.ecommerce.dto.CustomerRequestDTO;
import com.example.ecommerce.dto.CustomerResponse;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequestDTO request);

    CustomerResponse updateCustomer(CustomerRequestDTO request);

    CustomerResponse getCustomer();
}

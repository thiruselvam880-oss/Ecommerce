package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CustomerRequestDTO;
import com.example.ecommerce.dto.CustomerResponse;
import com.example.ecommerce.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public CustomerResponse createCustomer(
            @Valid @RequestBody CustomerRequestDTO request){

        return customerService.createCustomer(request);
    }

    @PutMapping
    public CustomerResponse updateCustomer(
            @Valid @RequestBody CustomerRequestDTO request){

        return customerService.updateCustomer(request);
    }

    @GetMapping
    public CustomerResponse getCustomer(){

        return customerService.getCustomer();
    }

}

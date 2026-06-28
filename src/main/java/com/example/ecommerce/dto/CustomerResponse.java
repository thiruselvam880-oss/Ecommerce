package com.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address;

    private String city;

    private String state;

    private String country;

    private String pincode;

    private String username;

}

package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Entity
@Setter
@Getter
public class Customer {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String phoneNumber;

    private String address;

    private String city;

    private String state;

    private String country;

    private String pincode;
    @OneToOne
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private Users user;
    public Customer() {
    }
}

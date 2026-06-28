package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private double price;

    @Column(length = 1000)
    private String description;

    private String category;

    @Lob
    //@Column(columnDefinition = "LONGBLOB")
    @Basic(fetch=FetchType.LAZY)
    private byte[] image;

    private double rating;
    @Column(name = "stocks", nullable = true)

    private Integer stocks;
}
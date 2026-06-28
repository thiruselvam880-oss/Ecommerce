package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUser(Users user);
}

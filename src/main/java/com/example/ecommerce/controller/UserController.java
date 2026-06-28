package com.example.ecommerce.controller;

import com.example.ecommerce.dto.RegisterUserRequest;
import com.example.ecommerce.dto.UserResponse;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterUserRequest registerUserRequest){
        registerUserRequest.setRole(Role.USER);
        UserResponse userResponse = userService.registerUser(registerUserRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<UserResponse> registerByAdmin(@RequestBody RegisterUserRequest registerUserRequest){
        UserResponse userResponse = userService.registerUser(registerUserRequest);
        return ResponseEntity.ok(userResponse);
    }
}

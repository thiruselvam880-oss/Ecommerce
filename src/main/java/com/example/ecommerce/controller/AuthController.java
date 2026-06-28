package com.example.ecommerce.controller;

import com.example.ecommerce.dto.AuthRequest;
import com.example.ecommerce.dto.AuthResponse;
import com.example.ecommerce.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTUtil jwtUtil;
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> generateToken(@RequestBody AuthRequest authRequest){
        try {
            System.out.println(authRequest.getUsername()+" "+authRequest.getPassword()+" ");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            String token = jwtUtil.generateToken(authRequest.getUsername());

            return ResponseEntity.ok(new AuthResponse(token));

        }catch (Exception e){
            throw e;
        }
    }
}

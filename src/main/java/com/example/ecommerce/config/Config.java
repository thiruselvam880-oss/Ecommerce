package com.example.ecommerce.config;

import com.example.ecommerce.filter.JwtAuthFilter;
import com.example.ecommerce.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class Config {
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    JwtAuthFilter jwtAuthFilter;
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .cors(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth


                                .requestMatchers("/authenticate").permitAll()

                                .requestMatchers("/api/users/register").permitAll()

                                // Products
                                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/products/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")

                                // Customer
                                .requestMatchers("/api/customer/**").hasRole("USER")

                                // Cart
                                .requestMatchers("/api/cart/**").hasRole("USER")

                                // Orders
                                .requestMatchers(HttpMethod.POST,
                                        "/api/orders/checkout").hasRole("USER")

                                .requestMatchers(HttpMethod.GET,
                                        "/api/orders/my-orders").hasRole("USER")

                                .requestMatchers(HttpMethod.GET,
                                        "/api/orders").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.PUT,
                                        "/api/orders/**").hasRole("ADMIN")

                                // Admin user creation
                                .requestMatchers("/api/users/admin/create")
                                .hasRole("ADMIN")

                                .anyRequest().authenticated()
                        );


        http.addFilterBefore(jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
//    @Bean
//    public UserDetailsService userDetailsService(){
//        return new CustomUserDetailsService();
//    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }
}

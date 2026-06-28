package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/load")
    public String loadProducts() {

        service.fetchAndSaveProducts();

        return "Products Saved Successfully";
    }
    @GetMapping
    public List<ProductResponse> getProducts() {
        return service.getProducts();
    }
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {

        Product product = service.getImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(product.getImage());
    }
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> addProduct(
            @RequestParam("ptitle") String ptitle,
            @RequestParam("price") Double price,
            @RequestParam("rating") Double rating,
            @RequestParam("stock") Integer stock,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image

    ) throws IOException {

        Product product = new Product();
        product.setTitle(ptitle);
        product.setPrice(price);
        product.setRating(rating);
        product.setCategory(category);
        product.setDescription(description);
        product.setImage(image.getBytes());
        product.setImage(image.getBytes());
       service.saveProduct(product);


        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Product added successfully");

        return ResponseEntity.ok(response);    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") long id)
    {
        return service.deleteProduct(id);
    }
    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") long id)
    {
       return service.getProductById(id);
    }
    @GetMapping("search")
    public ResponseEntity<?> getCategory(@RequestParam("keyword") String keyword)
    {
        //System.out.println(service.search(keyword));
       return ResponseEntity.ok().body(service.search(keyword));
    }
    @GetMapping("/categories")
    public List<String> getCategories() {
        return service.getCategories();
    }
    @PutMapping(value="/{id}",consumes="multipart/form-data")
    public ResponseEntity<?> updateProduct(

            @PathVariable Long id,

            @RequestParam("ptitle") String title,

            @RequestParam("price") Double price,

            @RequestParam("description") String description,

            @RequestParam("category") String category,

            @RequestParam("rating") Double rating,

            @RequestParam("stock") Integer stock,

            @RequestParam(value="image",required=false)
            MultipartFile image

    )throws Exception{

        return service.updateProduct(
                id,
                title,
                price,
                description,
                category,
                rating,
                stock,
                image
        );

    }
}

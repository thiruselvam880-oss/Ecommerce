package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductRepository repository;

    public void fetchAndSaveProducts() {

        String url = "https://fakestoreapi.com/products";

        ProductDTO[] products = restTemplate.getForObject(url, ProductDTO[].class);

        for (ProductDTO dto : products) {

            Product product = new Product();

            //product.setId(dto.getId());
            product.setTitle(dto.getTitle());
            product.setPrice(dto.getPrice());
            product.setDescription(dto.getDescription());
            product.setCategory(dto.getCategory());
            product.setRating(dto.getRating().getRate());
            product.setStocks(dto.getRating().getCount());

            try {
                byte[] imageBytes = restTemplate.getForObject(dto.getImage(), byte[].class);
                product.setImage(imageBytes);
            } catch (Exception e) {
                System.out.println("Failed to download image: " + dto.getImage());
                e.printStackTrace();
            }

            repository.save(product);
        }
    }
   // @Cacheable(value = "products")
    public List<ProductResponse> getProducts() {

        return repository.findAll().stream().map(product -> {

            ProductResponse dto = new ProductResponse();

            dto.setId(product.getId());
            dto.setTitle(product.getTitle());
            dto.setPrice(product.getPrice());
            dto.setDescription(product.getDescription());
            dto.setCategory(product.getCategory());
            dto.setRating(product.getRating());
            dto.setStocks(product.getStocks());

            return dto;

        }).toList();
    }
   // @Cacheable("images")
    public Product getImage(Long id) {

        System.out.println("Image ID: " + id);

        return repository.findById(id).orElseThrow();
    }

    public void saveProduct(Product product) {

        repository.save(product);
    }
    //@CacheEvict(value = "products", key = "#id")
    public ResponseEntity<?> deleteProduct(Long id) {
         repository.deleteById(id);
         return ResponseEntity.ok("Deleted Successfully");
    }


    public List<ProductResponse> search(String keyword) {
       return  repository.searchProducts(keyword);
    }
    public List<String> getCategories() {
        return repository.getCategories();
    }

    public ResponseEntity<ProductResponse> getProductById(long id) {
        Product product = repository.findById(id).orElseThrow();
        ProductResponse dto = new ProductResponse();

        dto.setRating(product.getRating());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setStocks(product.getStocks());
        dto.setTitle(product.getTitle());
        dto.setId(product.getId());
        dto.setPrice(product.getPrice());
        return ResponseEntity.ok().body(dto);
    }


    public ResponseEntity<?> updateProduct(Long id, String title, Double price, String description, String category, Double rating, Integer stock, MultipartFile image) throws IOException {
        Product product = repository.findById(id).orElseThrow();
        product.setTitle(title);
        product.setPrice(price);
        product.setDescription(description);
        product.setCategory(category);
        product.setRating(rating);
        product.setStocks(stock);
        if (image != null && !image.isEmpty() && image.getSize() > 0) {
            product.setImage(image.getBytes());
        }
        repository.save(product);
        return ResponseEntity.ok("Product Updated Successfully");
    }
}

package com.example.ecommerce.repository;

import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.entity.Product;
import jdk.jfr.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
SELECT new com.example.ecommerce.dto.ProductResponse(
    p.id,
    p.title,
    p.price,
    p.description,
    p.category,
    p.rating,
    p.stocks
)
FROM Product p
WHERE lower(p.title) LIKE lower(concat('%', :keyword, '%'))
   OR lower(p.category) LIKE lower(concat('%', :keyword, '%'))
   OR lower(p.description) LIKE lower(concat('%', :keyword, '%'))
""")
    List<ProductResponse> searchProducts(@Param("keyword") String keyword);

    @Query("select distinct p.category from Product p order by p.category")
    List<String> getCategories();

}
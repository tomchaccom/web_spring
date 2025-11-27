package com.example.web_spring.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.category.name = :name")
    List<Product> findByCategoryName(@Param("name") String name);

    Optional<Product> findByName(String name);

}

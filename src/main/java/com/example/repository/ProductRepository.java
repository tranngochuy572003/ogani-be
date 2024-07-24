package com.example.repository;

import com.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
  Product findProductByNameProduct(String name);
  List<Product> findByCreatedDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
  List<Product> findByPriceBetween(Long priceLowest, Long price);

}

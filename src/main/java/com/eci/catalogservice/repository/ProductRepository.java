package com.eci.catalogservice.repository;

import com.eci.catalogservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
}

/* JpaRepository → provides all CRUD operations.
Adds a finder method for SKU lookup (useful for other microservices like Orders or Inventory).*/

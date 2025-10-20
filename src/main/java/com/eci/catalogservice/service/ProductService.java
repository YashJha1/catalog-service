package com.eci.catalogservice.service;

import com.eci.catalogservice.model.Product;
import com.eci.catalogservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    public Optional<Product> getProductBySku(String sku) {
        return repository.findBySku(sku);
    }

    public Product createProduct(Product product) {
        return repository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        return repository.findById(id).map(existing -> {
            existing.setSku(product.getSku());
            existing.setName(product.getName());
            existing.setCategory(product.getCategory());
            existing.setPrice(product.getPrice());
            existing.setActive(product.isActive());
            return repository.save(existing);
        }).orElse(null);
    }

    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }
}

/* Handles all business logic.

updateProduct() performs a partial overwrite.

Returns Optional for clean null-handling. */

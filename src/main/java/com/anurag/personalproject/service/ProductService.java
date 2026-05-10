package com.anurag.personalproject.service;

import com.anurag.personalproject.entity.Product;
import com.anurag.personalproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    // Spring automatically injects ProductRepository here
    private final ProductRepository productRepository;

    // Get all products from DB
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get one product by ID — throws error if not found
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    // Get products filtered by category
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    // Save a new product to DB
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}
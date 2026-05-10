package com.anurag.personalproject.service;

import com.anurag.personalproject.entity.Product;
import com.anurag.personalproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import com.anurag.personalproject.exception.ProductNotFoundException;

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
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    // Get products filtered by category
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    // Save a new product to DB
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    public Product updateProduct(Long id, Product updatedProduct) {

        // First check product exists — throws 404 if not
        Product existing = getProductById(id);

        // Update only the fields that were sent
        existing.setName(updatedProduct.getName());
        existing.setCategory(updatedProduct.getCategory());
        existing.setBasePrice(updatedProduct.getBasePrice());
        existing.setStock(updatedProduct.getStock());

        // Save returns the updated product
        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {

        // Check product exists first — throws 404 if not
        getProductById(id);

        productRepository.deleteById(id);
    }
}
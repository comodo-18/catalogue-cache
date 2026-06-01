package com.anurag.personalproject.service;

import com.anurag.personalproject.entity.Product;
import com.anurag.personalproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import com.anurag.personalproject.exception.ProductNotFoundException;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    // Spring automatically injects ProductRepository here
    private final ProductRepository productRepository;
    private final CacheStatService cacheStatService;
    @Autowired(required = false)
    private CacheInvalidationProducer cacheInvalidationProducer;

    // Get all products from DB
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get one product by ID — throws error if not found
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        log.info("Cache MISS — fetching product from DB for id: {}", id);
        cacheStatService.recordMiss();
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
    @CacheEvict(value = "products", key = "#id")
    public Product updateProduct(Long id, Product updatedProduct) {

        // First check product exists — throws 404 if not
        Product existing = getProductById(id);

        // Update only the fields that were sent
        existing.setName(updatedProduct.getName());
        existing.setCategory(updatedProduct.getCategory());
        existing.setBasePrice(updatedProduct.getBasePrice());
        existing.setStock(updatedProduct.getStock());

        // Save returns the updated product
        Product saved =  productRepository.save(existing);
        if (cacheInvalidationProducer != null) {
            cacheInvalidationProducer.publishInvalidation(id, "UPDATED");
        }
        return  saved;

    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {

        // Check product exists first — throws 404 if not
        getProductById(id);

        productRepository.deleteById(id);
        if (cacheInvalidationProducer != null) {
            cacheInvalidationProducer.publishInvalidation(id, "DELETED");
        }
    }
}
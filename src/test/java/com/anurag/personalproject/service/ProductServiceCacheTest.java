package com.anurag.personalproject.service;

import com.anurag.personalproject.entity.Product;
import com.anurag.personalproject.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceCacheTest {

    @Autowired
    private ProductService productService;

    @SpyBean
    private ProductRepository productRepository;

    @Autowired
    private CacheManager cacheManager;

    private Product savedProduct;

    @BeforeEach
    void setUp() {
        // Clear cache before each test
        cacheManager.getCache("products").clear();

        savedProduct = new Product();
        savedProduct.setName("Test Sofa");
        savedProduct.setCategory("FURNITURE");
        savedProduct.setBasePrice(499.99);
        savedProduct.setStock(10);
        savedProduct = productRepository.save(savedProduct);
    }

    @Test
    void shouldHitDatabaseOnlyOnceWhenFetchedTwice() {
        Long id = savedProduct.getId();

        // Reset spy count after setup's save
        clearInvocations(productRepository);

        // First call — cache miss — hits DB
        productService.getProductById(id);

        // Second call — cache hit — should NOT hit DB
        productService.getProductById(id);

        // VERIFY — findById called exactly ONCE despite two fetches
        verify(productRepository, times(1)).findById(id);
    }
}
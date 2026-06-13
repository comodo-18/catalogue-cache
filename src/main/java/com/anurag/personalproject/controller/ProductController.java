package com.anurag.personalproject.controller;

import com.anurag.personalproject.entity.Product;
import com.anurag.personalproject.service.CacheStatService;
import com.anurag.personalproject.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CacheStatService cacheStatService;

    // GET /api/products OR GET /api/products?category=FURNITURE
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) String category) {
        if (category != null) {
            return ResponseEntity.ok(productService.getProductsByCategory(category));
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // GET /api/products/1
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long id,
            HttpServletResponse response) {

        cacheStatService.recordRequest();

        long start = System.currentTimeMillis();
        Product product = productService.getProductById(id);
        long ms = System.currentTimeMillis() - start;

        String source = ProductService.responseSource.get();
        if (source == null) source = "REDIS";
        ProductService.responseSource.remove();

        response.setHeader("X-Response-Source", source + " · " + ms + "ms");
        return ResponseEntity.ok(product);
    }

    // POST /api/products
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }
    // PUT /api/products/1
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    // DELETE /api/products/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // returns 204 No Content
    }
}
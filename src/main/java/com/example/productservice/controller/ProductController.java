package com.example.productservice.controller;

import com.example.productservice.dto.ProductResponse;
import com.example.productservice.dto.UpdateProductRequest;
import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductDetail(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();

        Product product = productService.getProductById(id);

        long executionTime = System.currentTimeMillis() - startTime;
        String source = (executionTime >= 100) ? "DATABASE" : "REDIS_CACHE";

        System.out.printf(">>> [GET /products/%d] Phản hồi trong: %d ms | Nguồn dữ liệu: %s\n", id, executionTime, source);

        return ResponseEntity.ok(new ProductResponse(product, executionTime, source));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProductDetail(@PathVariable Long id, @RequestBody UpdateProductRequest request) {
        Product updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(updatedProduct);
    }
}
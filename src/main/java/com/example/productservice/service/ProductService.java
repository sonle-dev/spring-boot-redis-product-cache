package com.example.productservice.service;

import com.example.productservice.dto.UpdateProductRequest;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 1. Lấy chi tiết sản phẩm áp dụng Cache-Aside
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        log.warn(">>> [CACHE MISS] Dữ liệu chưa có trong Redis, đang truy vấn Database cho sản phẩm ID: {}", id);

        // Giả lập độ trễ truy vấn dữ liệu phức tạp từ DB (~150-200ms)
        try {
            Thread.sleep(180);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
    }

    // 2. Cập nhật sản phẩm và xóa sạch Cache cũ liên quan
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product updateProduct(Long id, UpdateProductRequest request) {
        log.info(">>> Đang cập nhật sản phẩm ID: {} và kích hoạt @CacheEvict xóa key trong Redis...", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        if (request.getProductName() != null) product.setProductName(request.getProductName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() > 0) product.setPrice(request.getPrice());
        if (request.getImageUrl() != null) product.setImageUrl(request.getImageUrl());
        if (request.getStockQuantity() >= 0) product.setStockQuantity(request.getStockQuantity());

        Product savedProduct = productRepository.save(product);
        log.info(">>> Đã lưu thông tin mới vào Database. Key 'products::{}' đã bị xóa khỏi Redis!", id);

        return savedProduct;
    }
}
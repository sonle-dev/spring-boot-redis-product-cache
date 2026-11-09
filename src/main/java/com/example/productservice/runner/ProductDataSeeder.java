package com.example.productservice.runner;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProductDataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductDataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            Product defaultProduct = new Product(
                    "Thực phẩm bảo vệ sức khỏe Vitamin C 1000mg",
                    "Viên sủi tăng cường sức đề kháng và miễn dịch, hương cam tự nhiên",
                    85000.0,
                    "https://example.com/images/vitamin-c-1000.png",
                    500
            );
            productRepository.save(defaultProduct);
            System.out.println(">>> Đã khởi tạo dữ liệu sản phẩm mẫu (ID = 1, Tên: Vitamin C 1000mg)");
        }
    }
}
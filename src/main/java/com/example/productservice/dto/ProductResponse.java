package com.example.productservice.dto;

import com.example.productservice.entity.Product;

public class ProductResponse {
    private Product product;
    private long executionTimeMs;
    private String dataSource; // "DATABASE" hoặc "REDIS_CACHE"

    public ProductResponse() {
    }

    public ProductResponse(Product product, long executionTimeMs, String dataSource) {
        this.product = product;
        this.executionTimeMs = executionTimeMs;
        this.dataSource = dataSource;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
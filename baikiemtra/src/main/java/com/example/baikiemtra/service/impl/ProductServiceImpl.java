package com.example.baikiemtra.service.impl;

import com.example.baikiemtra.model.Product;
import com.example.baikiemtra.repository.ProductRepository;
import com.example.baikiemtra.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.getProductById(id);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.getProductsByName(name);
    }

    @Override
    public List<Product> getSortedPrice(double minPrice, double maxPrice) {
        return productRepository.getSortedPrice(minPrice,maxPrice);
    }




}

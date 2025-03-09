package com.example.baikiemtra.service;

import com.example.baikiemtra.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductService {
    public List<Product> getProducts();
    public Product getProductById(int id);
    public List<Product> getProductsByName(String name);
    public List<Product> getSortedPrice(double minPrice, double maxPrice);
}

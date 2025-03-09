package com.example.baikiemtra.repository;

import com.example.baikiemtra.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ProductRepository {
    public List<Product> getProducts();
    public Product getProductById(int id);
    public List<Product> getProductsByName(String name);
    public List<Product> getSortedPrice(double minPrice, double maxPrice);
}

package com.example.bai1.controller;

import com.example.bai1.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
public class ProductController {
    private List<Product> products = new ArrayList<>(List.of(new Product("1","iphone 12","ngon",1222,"apple"),
            new Product("2","iphone 13","ngon",1222,"apple"),
            new Product("3","iphone 14","ngon",1220,"apple"),
            new Product("4","iphone 15","ngon",1228,"apple"),
            new Product("5","iphone 16","ngon",1227,"apple"),
            new Product("6","iphone 17","ngon",1226,"apple"),
            new Product("7","iphone 18","ngon",1224,"apple")));

    @GetMapping("/products")
    public List<Product> getProducts() {
        return products;
    }


}




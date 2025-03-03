package com.example.b2.controller;

import com.example.b2.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@RestController
@RequestMapping("/products")
public class ProductController {
    private List<Product> products = new ArrayList<>(List.of(new Product("1","iphone 12","ngon",1222,"apple"),
            new Product("2","iphone 13","ngon",1222,"hawel"),
            new Product("3","nokia","ngon",1220,"apple"),
            new Product("4","iphone 15","ngon",1228,"vivo"),
            new Product("5","iphone 16","ngon",1227,"apple"),
            new Product("6","samsung","ngon",1226,"nokia"),
            new Product("7","iphone 18","ngon",1224,"samsung")));


    //    1. Lấy thông tin chi tiết của một sản phẩm
//    API: GET /products/{id}
//    Mô tả: Trả về chi tiết của sản phẩm dựa trên id được cung cấp.
    @GetMapping("/{id}")
    public Product getProducts(@PathVariable String id) {
        for (Product product : products) {
            if(product.getId().equals(id)){
                return product;
            }

        }
        return null;
    }
//
//2. Lấy sản phẩm với tên bắt đầu bằng prefix nào đó
//    API: GET /products/name-starts/{prefix}
//    Mô tả: Trả về danh sách sản phẩm có tên bắt đầu bằng nào đó.
@GetMapping("/name-start/{prefix}")
    public List<Product> getProductsByPrefix(@PathVariable String prefix) {
        List<Product> productss = new ArrayList<>();
    for (Product product : products) {
        if(product.getName().startsWith(prefix)){
            productss.add(product);
        }
    }
    return productss ;
}
//3. Lọc sản phẩm theo khoảng gia
//    API: GET /products/price/{min}/{max}
//    Mô tả: Trả về danh sách sản phẩm có giá nằm trong khoảng từ min đến max.
@GetMapping("/price/{min}/{max}")
    public List<Product> getProducts2(@PathVariable int min, @PathVariable int max) {
        List<Product> productss = new ArrayList<>();
        for (Product product : products) {
            if(product.getPrice() >= min && product.getPrice() <= max){
                productss.add(product);
            }
        }
        return productss;
    }

//4. Lấy sản phẩm theo thương hiệu
//    API: GET /products/brand/{brand}
//    Mô tả: Trả về danh sách sản phẩm thuộc thương hiệu được chỉ định.
@GetMapping("/brand/{brand}")
    public List<Product> getProductsByBrand(@PathVariable String brand) {
        List<Product> productss = new ArrayList<>();
        for (Product product : products) {
            if(product.getBrand().equals(brand)){
                productss.add(product);
            }
        }
        return productss;
}
//            5. Lấy sản phẩm có giá cao nhất
//    API: GET /products/brand/{brand}/max-price
//    Mô tả: Trả về sản phẩm có giá cao nhất theo brand được chỉ định.

    @GetMapping("/brand/{brand}/max-price")
    public List<Product> getProductsByBrandMaxPrice(@PathVariable String brand) {
        List<Product> brandProducts = new ArrayList<>();
        int maxPrice = 0;

        // Lọc sản phẩm theo thương hiệu và tìm giá lớn nhất
        for (Product product : products) {
            if (product.getBrand().equals(brand)) {
                brandProducts.add(product);
                if (product.getPrice() > maxPrice) {
                    maxPrice = product.getPrice();
                }
            }
        }
        List<Product> result = new ArrayList<>();
        for (Product product : brandProducts){
            if(product.getPrice() == maxPrice){
                result.add(product);
            }
        }
        return result;
    }
}
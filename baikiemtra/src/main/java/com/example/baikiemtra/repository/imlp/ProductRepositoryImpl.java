package com.example.baikiemtra.repository.imlp;

import com.example.baikiemtra.db.ProductDB;
import com.example.baikiemtra.model.Product;
import com.example.baikiemtra.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.baikiemtra.db.ProductDB.products;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public Product getProductById(int id) {
        Product products = ProductDB.products.stream().filter(product -> product.getId() == id).findFirst().orElse(null);
        return products;
    }

    @Override
    public List<Product> getProductsByName(String name) {
        List<Product> filteredProducts = new ArrayList<>();
        if(name != null) {

            filteredProducts = ProductDB.products.stream().filter(product -> product.getName().toLowerCase().contains(name.toLowerCase())).toList();

        }else {
            filteredProducts = ProductDB.products;
        }
        return filteredProducts;
    }

    @Override
    public List<Product> getSortedPrice(double minPrice, double maxPrice) {
        return ProductDB.products.stream()
                .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
    }




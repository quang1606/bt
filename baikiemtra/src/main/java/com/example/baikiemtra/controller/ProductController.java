package com.example.baikiemtra.controller;

import com.example.baikiemtra.db.ProductDB;
import com.example.baikiemtra.model.PageResponse;
import com.example.baikiemtra.model.Product;
import com.example.baikiemtra.service.PageResponseService;
import com.example.baikiemtra.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.baikiemtra.db.ProductDB.products;

@Controller
public class ProductController {
    private final ProductService productService;
    private final PageResponseService pageResponseService;

    public ProductController(ProductService productService, PageResponseService pageResponseService) {
        this.productService = productService;
        this.pageResponseService = pageResponseService;
    }

    @GetMapping("/products/all")
    public String getProducts(@RequestParam(value = "keyword", required = false) String search,
                              @RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "0") int minPrice,
                              @RequestParam(required = false, defaultValue = "48000000") int maxPrice,
                              Model model) {
        try {
            List<Product> filteredProducts = productService.getProductsByPriceRange(search, minPrice, maxPrice);
            if (filteredProducts == null) {
                filteredProducts = new ArrayList<>();
            }

            // Phân trang (mỗi trang 10 sản phẩm)
            List<Product> pageResponse = pageResponseService.getData(filteredProducts, 10, page);
            int totalPages = pageResponseService.getTotalPages(filteredProducts.size(), 10);

            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageResponse", pageResponse);
            model.addAttribute("currentPage", page);
            model.addAttribute("search", search);
            model.addAttribute("minPrice", minPrice);
            model.addAttribute("maxPrice", maxPrice);

            return "index";
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Trả về trang lỗi
        }
    }



    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product";
    }


}


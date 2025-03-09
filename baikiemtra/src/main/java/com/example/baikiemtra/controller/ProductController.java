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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/")
    public String getProduct(Model model, @RequestParam(value = "keyword",required = false) String search, @RequestParam(required = false, defaultValue = "1") int page) {
        List<Product> products = productService.getProducts();
        List<Product> filteredProducts = productService.getProductsByName(search);
        List<Product> pageResponse = pageResponseService.getData(filteredProducts, 10, page);
        PageResponse<Product> pageResponse1 = new PageResponse<>(filteredProducts, 10, page);
        int totalPages = pageResponseService.getTotalPages(products.size(), 10);
        model.addAttribute("pageResponse1", pageResponse1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageResponse", pageResponse);
        model.addAttribute("filteredProducts", filteredProducts);
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/filter")
    public String filterProducts(
            @RequestParam(value = "minPrice", required = false, defaultValue = "0") double minPrice,
            @RequestParam(value = "maxPrice", required = false, defaultValue = "1000000") double maxPrice,
            RedirectAttributes redirectAttributes) {

        List<Product> filteredProducts = productService.getSortedPrice(minPrice, maxPrice);
        redirectAttributes.addFlashAttribute("filteredProducts", filteredProducts);
        return "redirect:/filter-result";
    }

    @GetMapping("/filter-result")
    public String showFilteredProducts(Model model) {
        return "filtered-products";  // Chuyển sang trang mới
    }
}


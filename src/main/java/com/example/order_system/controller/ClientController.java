package com.example.order_system.controller;
import com.example.order_system.model.Product;
import com.example.order_system.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class ClientController {

    private final ProductRepository productRepository;

    public ClientController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @GetMapping("/client/products")
    public String clientProducts(@RequestParam(required = false) String keyword, Model model) {

        List<Product> products;

        if (keyword != null && !keyword.isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(keyword);
        } else {
            products = productRepository.findAll();
        }

        model.addAttribute("products", products);
        return "client-products";
    }
}
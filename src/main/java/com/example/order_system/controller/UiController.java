package com.example.order_system.controller;

import com.example.order_system.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.order_system.model.Product;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class UiController {

    private final ProductService productService;

    public UiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/ui/products")
    public String showProducts(Model model) {
        model.addAttribute("products", productService.getProducts());
        return "admin-products";
    }
    @GetMapping("/ui/products/new")
    public String showAddForm() {
        return "add-product";
    }
    @PostMapping("/ui/products")
    public String addProduct(@RequestParam String name,
                             @RequestParam BigDecimal price) {

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        productService.save(product);

        return "redirect:/ui/products?success=created";
    }

    @GetMapping("/ui/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "edit-product";   // ✅ 返回页面
    }

    @PostMapping("/ui/products/update")
    public String updateProduct(@RequestParam Long id,
                                @RequestParam String name,
                                @RequestParam BigDecimal price) {

        Product product = productService.getProductById(id);
        product.setPrice(price);
        product.setName(name);
        productService.save(product);

        return "redirect:/ui/products?success=updated";
    }

    @PostMapping("/ui/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/ui/products?success=deleted";
    }

}
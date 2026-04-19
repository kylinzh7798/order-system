package com.example.order_system.controller;

import com.example.order_system.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.order_system.model.Product;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UiController {

    private final ProductService productService;

    public UiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/ui/products")
    public String showProducts(Model model) {
        model.addAttribute("products", productService.getProducts());
        return "products";
    }
    @GetMapping("/ui/products/new")
    public String showAddForm() {
        return "add-product";
    }
    @PostMapping("/ui/products")
    public String addProduct(@RequestParam String name,
                             @RequestParam Double price) {

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
                                @RequestParam Double price) {

        Product product = productService.getProductById(id);
        product.setPrice(price);
        product.setName(name);
        productService.save(product);

        return "redirect:/ui/products?success=updated";
    }
}
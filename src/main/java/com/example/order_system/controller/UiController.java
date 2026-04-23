package com.example.order_system.controller;

import com.example.order_system.model.Product;
import com.example.order_system.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/ui")
public class UiController {

    private final ProductService productService;

    public UiController(ProductService productService) {
        this.productService = productService;
    }

    // =============================
    // 🔐 LOGIN PAGE
    // =============================
    @GetMapping("/login")
    public String login() {
        return "login";
    }


    // =============================
    // 📦 LIST PRODUCTS
    // =============================
    @GetMapping("/products")
    public String showProducts(Model model) {

        boolean isAdmin = isAdmin();

        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("isAdmin", isAdmin);

        return isAdmin ? "admin-products" : "client-products";
    }

    // =============================
    // ➕ SHOW ADD FORM
    // =============================
    @GetMapping("/products/new")
    public String showAddForm() {
        if (!isAdmin()) {
            return "redirect:/ui/products?error=denied";
        }
        return "add-product";
    }

    // =============================
    // ➕ HANDLE ADD
    // =============================
    @PostMapping("/products")
    public String addProduct(
            @RequestParam String name,
            @RequestParam BigDecimal price
    ) {
        if (!isAdmin()) {
            return "redirect:/ui/products?error=denied";
        }

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        productService.save(product);

        return "redirect:/ui/products?success=created";
    }

    // =============================
    // ✏️ SHOW EDIT FORM
    // =============================
    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        if (!isAdmin()) {
            return "redirect:/ui/products?error=denied";
        }

        Product product = productService.getProductById(id);

        if (product == null) {
            return "redirect:/ui/products?error=notfound";
        }

        model.addAttribute("product", product);
        return "edit-product";
    }

    // =============================
    // ✏️ HANDLE UPDATE
    // =============================
    @PostMapping("/products/update")
    public String updateProduct(
            @RequestParam Long id,
            @RequestParam String name,
            @RequestParam BigDecimal price
    ) {

        if (!isAdmin()) {
            return "redirect:/ui/products?error=denied";
        }

        Product product = productService.getProductById(id);

        if (product == null) {
            return "redirect:/ui/products?error=notfound";
        }

        product.setName(name);
        product.setPrice(price);

        productService.save(product);

        return "redirect:/ui/products?success=updated";
    }

    // =============================
    // 🗑 DELETE
    // =============================
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {

        if (!isAdmin()) {
            return "redirect:/ui/products?error=denied";
        }

        productService.deleteById(id);

        return "redirect:/ui/products?success=deleted";
    }

    // =============================
    // 🔐 ROLE CHECK（核心）
    // =============================
    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
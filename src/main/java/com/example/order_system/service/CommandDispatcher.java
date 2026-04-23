package com.example.order_system.service;

import com.example.order_system.dto.ToolRequest;
import com.example.order_system.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommandDispatcher {

    private final ProductService productService;

    // ✅ constructor 只能注入依赖
    public CommandDispatcher(ProductService productService) {
        this.productService = productService;
    }

    // ✅ auth 只能在方法里
    public String handle(ToolRequest req) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));



        switch (req.getAction()) {
            case "addProduct":
                return productService.addProduct(req.getName(), req.getPrice(), isAdmin);
            case "searchProduct":
                return productService.searchProduct(req.getKeyword());
            case "deleteProduct":
                return productService.deleteProduct(req.getKeyword(), isAdmin);
            case "updateProduct":
                return productService.updateProduct(req, isAdmin);
            default:
                return "❌ Unknown command";
        }
    }
}
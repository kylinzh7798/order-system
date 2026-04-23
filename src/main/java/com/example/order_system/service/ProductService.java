package com.example.order_system.service;

import com.example.order_system.dto.ToolRequest;
import com.example.order_system.model.Product;
import com.example.order_system.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    // =============================
    // 📦 UI 使用
    // =============================

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProductById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void save(Product product) {
        repo.save(product);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    // =============================
    // 🤖 AI / Command 使用
    // =============================

    // ✅ ADD
    public String addProduct(String name, BigDecimal price, boolean isAdmin) {

        if (!isAdmin) {
            return "❌ Permission denied (admin only)";
        }

        Product p = new Product();
        p.setName(name);
        p.setPrice(price);

        repo.save(p);

        return "✅ Product added";
    }

    // ✅ SEARCH
    public String searchProduct(String keyword) {

        List<Product> list = repo.findByNameContainingIgnoreCase(keyword);

        if (list.isEmpty()) {
            return "❌ No products found";
        }

        StringBuilder sb = new StringBuilder("🔍 Found products:\n");

        for (Product prod : list) {
            sb.append(prod.getId())
                    .append(". ")
                    .append(prod.getName())
                    .append(" - $")
                    .append(prod.getPrice())
                    .append("\n");
        }

        return sb.toString();
    }

    // ✅ DELETE
    public String deleteProduct(String keyword, boolean isAdmin) {

        if (!isAdmin) {
            return "❌ Permission denied (admin only)";
        }

        try {
            Long id = Long.parseLong(keyword);
            repo.deleteById(id);
            return "🗑️ Product deleted (id=" + id + ")";
        } catch (Exception e) {
            return "❌ Invalid product id";
        }
    }

    // ✅ UPDATE
    public String updateProduct(ToolRequest req, boolean isAdmin) {

        if (!isAdmin) {
            return "❌ Permission denied (admin only)";
        }

        try {
            Long id = Long.parseLong(req.getKeyword());

            Product existing = repo.findById(id).orElse(null);

            if (existing == null) {
                return "❌ Product not found";
            }

            existing.setName(req.getName());
            existing.setPrice(req.getPrice());

            repo.save(existing);

            return "✏️ Product updated (id=" + id + ")";

        } catch (Exception e) {
            return "❌ Update failed";
        }
    }
}
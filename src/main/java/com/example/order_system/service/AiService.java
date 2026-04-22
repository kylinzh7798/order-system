package com.example.order_system.service;

import com.example.order_system.dto.ToolRequest;
import com.example.order_system.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AiService {

    private final ProductService productService;

    public AiService(ProductService productService) {
        this.productService = productService;
    }


    public String handle(ToolRequest req) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        switch (req.getAction()) {

            // ✅ ADD
            case "addProduct":

                if (!isAdmin) {
                    return "❌ Permission denied (admin only)";
                }

                Product p = new Product();
                p.setName(req.getName());
                p.setPrice(req.getPrice());

                productService.save(p);

                return "✅ Product added";


            // ✅ SEARCH
            case "searchProduct":

                List<Product> list = productService.search(req.getKeyword());

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


            // ✅ DELETE
            case "deleteProduct":

                if (!isAdmin) {
                    return "❌ Permission denied (admin only)";
                }

                try {
                    Long id = Long.parseLong(req.getKeyword());
                    productService.deleteById(id);
                    return "🗑️ Product deleted (id=" + id + ")";
                } catch (Exception e) {
                    return "❌ Invalid product id";
                }


                // ✅ EDIT（update）
            case "updateProduct":

                if (!isAdmin) {
                    return "❌ Permission denied (admin only)";
                }

                try {
                    Long id = Long.parseLong(req.getKeyword());

                    Product existing = productService.getProductById(id);

                    if (existing == null) {
                        return "❌ Product not found";
                    }

                    existing.setName(req.getName());
                    existing.setPrice(req.getPrice());

                    productService.save(existing);

                    return "✏️ Product updated (id=" + id + ")";
                } catch (Exception e) {
                    return "❌ Update failed";
                }


            default:
                return "❌ Unknown command";
        }
    }
}
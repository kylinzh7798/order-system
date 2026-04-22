package com.example.order_system.service;

import com.example.order_system.dto.ToolRequest;
import com.example.order_system.model.Product;
import com.example.order_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToolExecutorService {

    @Autowired
    private ProductRepository productRepository;

    public String execute(ToolRequest req) {

        switch (req.getAction()) {

            case "addProduct":
                Product p = new Product();
                p.setName(req.getName());
                p.setPrice(req.getPrice());
                productRepository.save(p);
                return "Product added: " + p.getName();

            case "searchProduct":
                return productRepository
                        .findByNameContainingIgnoreCase(req.getKeyword())
                        .toString();

            case "deleteProduct":
                try {
                    Long id = Long.parseLong(req.getKeyword());
                    productRepository.deleteById(id);
                    return "Deleted product id=" + id;
                } catch (Exception e) {
                    return "Invalid id";
                }

            default:
                return "Unknown command";
        }
    }
}

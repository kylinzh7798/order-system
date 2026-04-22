package com.example.order_system.service;

import com.example.order_system.dto.ToolRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AIParserService {

    public ToolRequest parse(String message) {

        message = message.toLowerCase().trim();
        message = message.replace("\"", "");

        ToolRequest req = new ToolRequest();

        String[] parts = message.split("\\s+");

        if (message.startsWith("add")) {

            if (parts.length >= 3) {
                req.setAction("addProduct");
                req.setName(parts[1]);
                req.setPrice(new BigDecimal(parts[2]));
            } else {
                req.setAction("invalid");   // 👈 新加
            }

        } else if (message.startsWith("search")) {

            if (parts.length >= 2) {
                req.setAction("searchProduct");
                req.setKeyword(parts[1]);
            } else {
                req.setAction("invalid");
            }

        } else if (message.startsWith("delete")) {

            if (parts.length >= 2) {
                req.setAction("deleteProduct");
                req.setKeyword(parts[1]);
            } else {
                req.setAction("invalid");
            }

        } else {
            req.setAction("unknown");
        }

        return req;
    }
}
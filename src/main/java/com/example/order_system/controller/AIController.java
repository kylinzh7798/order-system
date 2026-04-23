package com.example.order_system.controller;

import com.example.order_system.dto.ChatRequest;
import com.example.order_system.dto.ToolRequest;
import com.example.order_system.service.AIParserService;
import com.example.order_system.service.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIParserService parser;

    @Autowired
    private CommandDispatcher aiService;   // ✅ 加这个

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {

        String message = request.getMessage();

        ToolRequest req = parser.parse(message);

        return aiService.handle(req);   // ✅ 改这里
    }
}
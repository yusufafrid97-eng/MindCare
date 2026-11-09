
package com.example.mentalwellnessbackend.controller;

import com.example.mentalwellnessbackend.entity.User;
import com.example.mentalwellnessbackend.entity.ChatMessage;

import com.example.mentalwellnessbackend.repository.UserRepository;

import com.example.mentalwellnessbackend.service.QwenService;
import com.example.mentalwellnessbackend.service.ChatMemoryService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(
        origins = {
                "http://localhost:5500",
                "http://127.0.0.1:5500"
        },
        allowCredentials = "true"
)
public class ChatController {

    private final QwenService qwenService;
    private final ChatMemoryService chatMemoryService;
    private final UserRepository userRepository;

    public ChatController(
            QwenService qwenService,
            ChatMemoryService chatMemoryService,
            UserRepository userRepository) {

        this.qwenService = qwenService;
        this.chatMemoryService = chatMemoryService;
        this.userRepository = userRepository;
    }

    // 1. CHAT API
    @PostMapping
    public ResponseEntity<Map<String, String>> chat(
            @RequestBody Map<String, String> request,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Please login first."));
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "User not found."));
        }

        String question = request.get("question");

        if (question == null || question.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Question cannot be empty."));
        }

        try {
            // Get this user's previous messages
            List<ChatMessage> history =
                    chatMemoryService.getChatHistory(user);

            // Save user's new message
            chatMemoryService.saveMessage(user, "user", question);

            // Generate Qwen response
            String answer = qwenService.askQwen(question, history);

            // Save AI response for this user
            chatMemoryService.saveMessage(user, "assistant", answer);

            return ResponseEntity.ok(
                    Map.of("answer", answer)
            );

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error",
                            "AI service failed. Please try again."
                    ));
        }
    }

    // 2. GET CHAT HISTORY API
    @GetMapping("/history")
    public ResponseEntity<?> getChatHistory(HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Please login first."));
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "User not found."));
        }

        List<ChatMessage> history =
                chatMemoryService.getChatHistory(user);

        List<Map<String, String>> result = new ArrayList<>();

        for (ChatMessage message : history) {
            result.add(Map.of(
                    "role", message.getRole(),
                    "message", message.getMessage()
            ));
        }

        return ResponseEntity.ok(result);
    }
}








package com.example.mentalwellnessbackend.service;

import com.example.mentalwellnessbackend.entity.ChatMessage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QwenService {

    public String askQwen(
            String question,
            List<ChatMessage> history) {

        // Ollama API URL
        String url = "http://localhost:11434/api/chat";

        RestTemplate restTemplate = new RestTemplate();

        // Prepare conversation messages
        List<Map<String, String>> messages =
                new ArrayList<>();

        // AI instructions
        messages.add(Map.of(
                "role", "system",
                "content",
                "You are MindCare AI, a supportive wellness companion. "
                        + "Use simple English and listen without judgement. "
                        + "Provide general wellness support. "
                        + "Do not diagnose medical or mental health conditions. "
                        + "Do not claim to be a medical professional. "
                        + "If someone may be in immediate danger, "
                        + "encourage them to contact emergency services "
                        + "or a trusted person."
        ));

        // Add previous conversation history
        for (ChatMessage chat : history) {

            messages.add(Map.of(
                    "role", chat.getRole(),
                    "content", chat.getMessage()
            ));
        }

        // Add latest user question
        messages.add(Map.of(
                "role", "user",
                "content", question
        ));

        // Prepare Ollama request
        Map<String, Object> request = new HashMap<>();

        request.put("model", "qwen2.5-coder:7b");
        request.put("messages", messages);
        request.put("stream", false);

        // Send request to Ollama
        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        url,
                        request,
                        Map.class
                );

        // Extract Qwen response
        Map result =
                (Map) response.getBody().get("message");

        return result.get("content").toString();
    }
}

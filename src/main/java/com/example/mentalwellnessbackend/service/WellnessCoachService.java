
package com.example.mentalwellnessbackend.service;

import com.example.mentalwellnessbackend.entity.Mood;
import com.example.mentalwellnessbackend.entity.User;
import com.example.mentalwellnessbackend.repository.MoodRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class WellnessCoachService {

    private final MoodRepository moodRepository;
    private final MoodAnalysisService moodAnalysisService;
    private final RestTemplate restTemplate;

    @Value("${ollama.api-url}")
    private String ollamaUrl;

    @Value("${ollama.model}")
    private String ollamaModel;

    public WellnessCoachService(
            MoodRepository moodRepository,
            MoodAnalysisService moodAnalysisService) {

        this.moodRepository = moodRepository;
        this.moodAnalysisService = moodAnalysisService;
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> generatePlan(User user) {

        // 1. Get user's mood history
        List<Mood> moods =
                moodRepository.findTop7ByUserOrderByCreatedAtDesc(user);

        if (moods.isEmpty()) {
            return Map.of(
                    "status", "INSUFFICIENT_DATA",
                    "message", "Please record your mood first."
            );
        }

        // 2. Get mood trend from Python API
        Map<String, Object> trend;

        try {
            trend = moodAnalysisService.analyzeMood(user);
        } catch (Exception e) {
            trend = Map.of(
                    "trend", "UNKNOWN",
                    "message", "Mood trend is currently unavailable."
            );
        }

        // 3. Prepare mood information
        List<String> moodHistory = new ArrayList<>();

        for (Mood mood : moods) {
            moodHistory.add(
                    "Mood: " + mood.getMood()
                            + ", Score: " + mood.getMoodScore()
            );
        }

        // 4. Create prompt for Qwen
        String prompt = """
                You are MindCare, a friendly general wellness coach.

                Create a personalized 7-day wellness plan using
                the user's actual mood information.

                Recent mood history:
                %s

                Mood trend:
                %s

                Requirements:
                1. Give a short and friendly summary.
                2. Give exactly 7 daily activities.
                3. Include simple activities like breathing,
                   journaling, walking and taking breaks.
                4. Keep activities realistic and easy.
                5. Explain why each activity may help.
                6. Use simple English.
                7. Do not diagnose any mental health condition.
                8. Do not claim that the plan replaces professional care.

                Return the result in clear, readable text.
                """.formatted(moodHistory, trend);

        // 5. Send request to Ollama
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> request = new HashMap<>();
        request.put("model", ollamaModel);
        request.put("messages", List.of(message));
        request.put("stream", false);

        // 6. Get Qwen response
        Map response = restTemplate.postForObject(
                ollamaUrl,
                request,
                Map.class
        );

        if (response == null || !(response.get("message") instanceof Map)) {
            throw new RuntimeException("Invalid response from Ollama.");
        }

        Map responseMessage = (Map) response.get("message");
        Object content = responseMessage.get("content");

        if (!(content instanceof String)) {
            throw new RuntimeException("Qwen returned an empty response.");
        }

        // 7. Return wellness plan
        return Map.of(
                "status", "SUCCESS",
                "moodTrend", trend.getOrDefault("trend", "UNKNOWN"),
                "wellnessPlan", content
        );
    }
}


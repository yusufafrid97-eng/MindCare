
package com.example.mentalwellnessbackend.controller;

import com.example.mentalwellnessbackend.entity.User;
import com.example.mentalwellnessbackend.entity.Mood;

import com.example.mentalwellnessbackend.repository.UserRepository;

import com.example.mentalwellnessbackend.service.MoodService;
import com.example.mentalwellnessbackend.service.MoodAnalysisService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/moods")
@CrossOrigin(
        origins = {
                "http://localhost:5500",
                "http://127.0.0.1:5500"
        },
        allowCredentials = "true"
)
public class MoodController {

    private final MoodService moodService;
    private final UserRepository userRepository;
    private final MoodAnalysisService moodAnalysisService;

    public MoodController(
            MoodService moodService,
            UserRepository userRepository,
            MoodAnalysisService moodAnalysisService) {

        this.moodService = moodService;
        this.userRepository = userRepository;
        this.moodAnalysisService = moodAnalysisService;
    }

    // 1. SAVE MOOD
    @PostMapping
    public ResponseEntity<?> saveMood(
            @RequestBody Map<String, Object> request,
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

        try {
            String mood = (String) request.get("mood");

            Object scoreValue = request.get("moodScore");

            Integer score = scoreValue instanceof Number
                    ? ((Number) scoreValue).intValue()
                    : null;

            String note = (String) request.get("note");

            Mood savedMood = moodService.saveMood(
                    user, mood, score, note
            );

            return ResponseEntity.ok(Map.of(
                    "message", "Mood saved successfully",
                    "id", savedMood.getId()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Unable to save mood."));
        }
    }

    // 2. GET ALL MOODS
    @GetMapping
    public ResponseEntity<?> getMoodHistory(HttpSession session) {

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

        List<Mood> moods = moodService.getMoodHistory(user);

        return ResponseEntity.ok(moods);
    }

    // 3. GET LATEST 7 MOODS
    @GetMapping("/weekly")
    public ResponseEntity<?> getWeeklyMoods(HttpSession session) {

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

        return ResponseEntity.ok(
                moodService.getWeeklyMoods(user)
        );
    }

    // 4. AI MOOD TREND ANALYSIS
    @GetMapping("/analysis")
    public ResponseEntity<?> analyzeMood(HttpSession session) {

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

        try {
            Map<String, Object> result =
                    moodAnalysisService.analyzeMood(user);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Mood analysis failed."));
        }
    }
}



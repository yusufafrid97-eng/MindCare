
package com.example.mentalwellnessbackend.service;

import com.example.mentalwellnessbackend.entity.Mood;
import com.example.mentalwellnessbackend.entity.User;
import com.example.mentalwellnessbackend.repository.MoodRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MoodAnalysisService {

    private final MoodRepository moodRepository;
    private final RestTemplate restTemplate;

    public MoodAnalysisService(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> analyzeMood(User user) {

        // 1. Get this user's mood history from MySQL
        List<Mood> moods =
                moodRepository.findByUserOrderByCreatedAtDesc(user);

        // 2. Reverse: oldest mood first
        Collections.reverse(moods);

        // 3. Extract mood scores
        List<Integer> scores = moods.stream()
                .map(Mood::getMoodScore)
                .collect(Collectors.toList());

        // 4. Prepare request for Python
        Map<String, Object> request = new HashMap<>();
        request.put("moodScores", scores);

        // 5. Send request to Python Flask
        Map<String, Object> result = restTemplate.postForObject(
                "http://127.0.0.1:5000/mood-trend",
                request,
                Map.class
        );

        return result;
    }
}




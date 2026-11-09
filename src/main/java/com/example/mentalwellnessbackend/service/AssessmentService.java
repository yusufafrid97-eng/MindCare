package com.example.mentalwellnessbackend.service;

import com.example.mentalwellnessbackend.dto.AiPredictionResponse;
import com.example.mentalwellnessbackend.entity.Assessment;
import com.example.mentalwellnessbackend.repository.AssessmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;

    public AssessmentService(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    public String saveAssessment(Assessment assessment) {

        // Create connection to Python AI
        RestTemplate restTemplate = new RestTemplate();

        // Python AI URL
        String pythonUrl = "http://127.0.0.1:5000/predict";

        // Prepare data for Python
        Map<String, Object> aiRequest = new HashMap<>();

        aiRequest.put("concentration", assessment.getConcentration());
        aiRequest.put("energy", assessment.getEnergy());
        aiRequest.put("overwhelmed", assessment.getOverwhelmed());
        aiRequest.put("physicalActivity", assessment.getPhysicalActivity());
        aiRequest.put("socialConnection", assessment.getSocialConnection());
        aiRequest.put("screenTime", assessment.getScreenTime());
        aiRequest.put("routineSatisfaction", assessment.getRoutineSatisfaction());

        // Send data to Python and receive response
        ResponseEntity<AiPredictionResponse> response =
                restTemplate.postForEntity(
                        pythonUrl,
                        aiRequest,
                        AiPredictionResponse.class
                );

        // Get risk from Python
        String risk = response.getBody().getRisk();

        System.out.println("AI Risk: " + risk);

        // Save assessment in MySQL
        assessmentRepository.save(assessment);

        // Return risk to Controller
        return risk;
    }
}

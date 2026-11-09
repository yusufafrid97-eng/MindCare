package com.example.mentalwellnessbackend.controller;

import com.example.mentalwellnessbackend.dto.AssessmentRequest;
import com.example.mentalwellnessbackend.entity.Assessment;
import com.example.mentalwellnessbackend.service.AssessmentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
@RestController
@RequestMapping("/api")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @PostMapping("/assessment")
    public String receiveAssessment(@RequestBody AssessmentRequest request) {

        // Create Assessment object
        Assessment assessment = new Assessment();

        // Copy data from DTO to Entity
        assessment.setSleep(request.getSleep());
        assessment.setStress(request.getStress());
        assessment.setMood(request.getMood());
        assessment.setConcentration(request.getConcentration());
        assessment.setEnergy(request.getEnergy());
        assessment.setOverwhelmed(request.getOverwhelmed());
        assessment.setPhysicalActivity(request.getPhysicalActivity());
        assessment.setSocialConnection(request.getSocialConnection());
        assessment.setScreenTime(request.getScreenTime());
        assessment.setRoutineSatisfaction(request.getRoutineSatisfaction());

        // Send assessment to Service
        String risk = assessmentService.saveAssessment(assessment);

        // Send AI result back
        return "Risk estimate: " + risk;
    }
}
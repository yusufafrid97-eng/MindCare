
package com.example.mentalwellnessbackend.controller;

import com.example.mentalwellnessbackend.entity.User;
import com.example.mentalwellnessbackend.repository.UserRepository;
import com.example.mentalwellnessbackend.service.WellnessCoachService;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/wellness-coach")
@CrossOrigin(
        origins = {
                "http://localhost:5500",
                "http://127.0.0.1:5500"
        },
        allowCredentials = "true"
)
public class WellnessCoachController {

    private final WellnessCoachService wellnessCoachService;
    private final UserRepository userRepository;

    public WellnessCoachController(
            WellnessCoachService wellnessCoachService,
            UserRepository userRepository) {

        this.wellnessCoachService = wellnessCoachService;
        this.userRepository = userRepository;
    }

    @GetMapping("/plan")
    public ResponseEntity<?> generatePlan(HttpSession session) {

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
                    wellnessCoachService.generatePlan(user);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error",
                            "Unable to generate wellness plan. Please try again."
                    ));
        }
    }
}


package com.example.mentalwellnessbackend.repository;

import com.example.mentalwellnessbackend.entity.ChatMessage;
import com.example.mentalwellnessbackend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findTop20ByUserOrderByCreatedAtDesc(User user);
}

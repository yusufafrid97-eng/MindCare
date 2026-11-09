
package com.example.mentalwellnessbackend.service;

import com.example.mentalwellnessbackend.entity.ChatMessage;
import com.example.mentalwellnessbackend.entity.User;
import com.example.mentalwellnessbackend.repository.ChatMessageRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collections;

@Service
public class ChatMemoryService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMemoryService(
            ChatMessageRepository chatMessageRepository) {

        this.chatMessageRepository = chatMessageRepository;
    }

    // Save a message
    public void saveMessage(User user, String role, String message) {

        ChatMessage chat = new ChatMessage();

        chat.setUser(user);
        chat.setRole(role);
        chat.setMessage(message);

        chatMessageRepository.save(chat);
    }

    // Retrieve previous messages
    public List<ChatMessage> getChatHistory(User user) {

        List<ChatMessage> messages =
                chatMessageRepository
                        .findTop20ByUserOrderByCreatedAtDesc(user);

        // Change newest-first into oldest-first
        Collections.reverse(messages);

        return messages;
    }
}

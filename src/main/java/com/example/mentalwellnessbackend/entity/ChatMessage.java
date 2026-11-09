
package com.example.mentalwellnessbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String role;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime createdAt;

    public ChatMessage() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @PrePersist
    public void setCreatedAt() {
        createdAt = LocalDateTime.now();
    }



}

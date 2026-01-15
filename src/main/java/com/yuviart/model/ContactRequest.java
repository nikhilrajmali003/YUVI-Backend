package com.yuviart.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_requests")
public class ContactRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private String artType;
    
    @Column(length = 2000)
    private String message;
    
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.NEW;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public ContactRequest() {
    }
    
    public ContactRequest(Long id, String name, String email, String artType, String message, RequestStatus status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.artType = artType;
        this.message = message;
        this.status = status;
    }
    
    // Lifecycle callback
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Request Status Enum
    public enum RequestStatus {
        NEW, IN_PROGRESS, RESPONDED, CLOSED
    }
    
    // ==================== GETTERS AND SETTERS ====================
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getArtType() {
        return artType;
    }
    
    public void setArtType(String artType) {
        this.artType = artType;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public RequestStatus getStatus() {
        return status;
    }
    
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
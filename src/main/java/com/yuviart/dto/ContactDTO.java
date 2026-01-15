package com.yuviart.dto;

public class ContactDTO {
    private String name;
    private String email;
    private String artType;
    private String message;

    public ContactDTO() {
    }

    public ContactDTO(String name, String email, String artType, String message) {
        this.name = name;
        this.email = email;
        this.artType = artType;
        this.message = message;
    }

    // Getters and Setters
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
}
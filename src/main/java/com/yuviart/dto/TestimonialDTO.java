package com.yuviart.dto;

public class TestimonialDTO {
    private String name;
    private String email;
    private Integer rating;
    private String text;

    public TestimonialDTO() {}

    public TestimonialDTO(String name, String email, Integer rating, String text) {
        this.name = name;
        this.email = email;
        this.rating = rating;
        this.text = text;
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

    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}

	package com.yuviart.dto;
	
	import java.math.BigDecimal;
	
	public class ArtworkDTO {
	    private Long id;
	    private String title;
	    private String description;
	    private String category;
	    private BigDecimal price;
	    private String imageUrl;
	    private Integer rating;
	    private Boolean available;
	
	    public ArtworkDTO() {
	    }
	
	    public ArtworkDTO(Long id, String title, String description, String category, 
	                      BigDecimal price, String imageUrl, Integer rating, Boolean available) {
	        this.id = id;
	        this.title = title;
	        this.description = description;
	        this.category = category;
	        this.price = price;
	        this.imageUrl = imageUrl;
	        this.rating = rating;
	        this.available = available;
	    }
	
	    // Getters and Setters
	    public Long getId() {
	        return id;
	    }
	
	    public void setId(Long id) {
	        this.id = id;
	    }
	
	    public String getTitle() {
	        return title;
	    }
	
	    public void setTitle(String title) {
	        this.title = title;
	    }
	
	    public String getDescription() {
	        return description;
	    }
	
	    public void setDescription(String description) {
	        this.description = description;
	    }
	
	    public String getCategory() {
	        return category;
	    }
	
	    public void setCategory(String category) {
	        this.category = category;
	    }
	
	    public BigDecimal getPrice() {
	        return price;
	    }
	
	    public void setPrice(BigDecimal price) {
	        this.price = price;
	    }
	
	    public String getImageUrl() {
	        return imageUrl;
	    }
	
	    public void setImageUrl(String imageUrl) {
	        this.imageUrl = imageUrl;
	    }
	
	    public Integer getRating() {
	        return rating;
	    }
	
	    public void setRating(Integer rating) {
	        this.rating = rating;
	    }
	
	    public Boolean getAvailable() {
	        return available;
	    }
	
	    public void setAvailable(Boolean available) {
	        this.available = available;
	    }
	}
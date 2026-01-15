	package com.yuviart.model;
	
	import jakarta.persistence.*;
	import lombok.AllArgsConstructor;
	import lombok.NoArgsConstructor;
	
	@Entity
	@Table(name = "artworks")
	@NoArgsConstructor
	@AllArgsConstructor
	public class Artwork {
	    
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(nullable = false)
	    private String title;
	    
	    @Column(columnDefinition = "TEXT")
	    private String description;
	    
	    @Column(nullable = false)
	    private String category;
	    
	    @Column(nullable = false)
	    private Double price;
	    
	    @Column(name = "image_url")
	    private String imageUrl;
	    
	    @Column(nullable = false)
	    private Boolean available = true;
	    
	    @Column(name = "stock_quantity")
	    private Integer stockQuantity = 0;
	    
	    private Double rating = 0.0;
	    
	    public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }
	    
	    public String getTitle() { return title; }
	    public void setTitle(String title) { this.title = title; }
	    
	    public String getDescription() { return description; }
	    public void setDescription(String description) { this.description = description; }
	    
	    public String getCategory() { return category; }
	    public void setCategory(String category) { this.category = category; }
	    
	    public Double getPrice() { return price; }
	    public void setPrice(Double price) { this.price = price; }
	    
	    public String getImageUrl() { return imageUrl; }
	    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
	    
	    public Boolean getAvailable() { return available; }
	    public void setAvailable(Boolean available) { this.available = available; }
	    
	    public Integer getStockQuantity() { return stockQuantity; }
	    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
	    
	    public Double getRating() { return rating; }
	    public void setRating(double d) { this.rating = d; }
	}

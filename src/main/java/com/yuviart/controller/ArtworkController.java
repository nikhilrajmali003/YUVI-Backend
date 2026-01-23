package com.yuviart.controller;

import com.yuviart.model.Artwork;
import com.yuviart.service.ArtworkService;
import com.yuviart.service.CloudinaryService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/artworks")
@CrossOrigin(origins = "*")
@Validated
public class ArtworkController {

    @Autowired
    private ArtworkService artworkService;

    @Autowired
    private CloudinaryService cloudinaryService;  // ✅ Use Cloudinary instead

    // 🎨 Get all artworks
    @GetMapping
    public ResponseEntity<?> getAllArtworks() {
        try {
            System.out.println("📋 Fetching all artworks...");
            List<Artwork> artworks = artworkService.getAllArtworks();
            System.out.println("✅ Found " + artworks.size() + " artworks");
            return ResponseEntity.ok(artworks);
        } catch (Exception e) {
            System.err.println("❌ Error fetching artworks: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch artworks: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 🎨 Get artwork by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getArtworkById(@PathVariable Long id) {
        try {
            System.out.println("🔍 Fetching artwork with ID: " + id);
            Artwork artwork = artworkService.getArtworkById(id);
            return ResponseEntity.ok(artwork);
        } catch (RuntimeException e) {
            System.err.println("❌ Artwork not found: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            System.err.println("❌ Error fetching artwork: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch artwork: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 🎨 Create artwork (JSON body with imageUrl)
    @PostMapping
    public ResponseEntity<?> createArtwork(@Valid @RequestBody Artwork artwork) {
        try {
            System.out.println("🎨 Creating new artwork: " + artwork.getTitle());
            System.out.println("🖼️ Image URL: " + artwork.getImageUrl());
            
            // Validate required fields
            if (artwork.getTitle() == null || artwork.getTitle().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Title is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (artwork.getPrice() == null || artwork.getPrice() <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Valid price is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            Artwork savedArtwork = artworkService.createArtwork(artwork);
            System.out.println("✅ Artwork created successfully with ID: " + savedArtwork.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArtwork);
        } catch (Exception e) {
            System.err.println("❌ Error creating artwork: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create artwork: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 🎨 Upload artwork with image (FormData) - Now uses Cloudinary
    @PostMapping("/with-image")
    public ResponseEntity<?> uploadArtworkWithImage(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("price") Double price,
            @RequestParam(value = "rating", defaultValue = "5") Integer rating,
            @RequestParam(value = "stockQuantity", defaultValue = "1") Integer stockQuantity,
            @RequestParam(value = "available", defaultValue = "true") Boolean available,
            @RequestParam("image") MultipartFile image) {
        
        try {
            System.out.println("🎨 Uploading artwork with image: " + title);
            
            // Validate inputs
            if (title == null || title.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Title is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (price == null || price <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Valid price is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (image == null || image.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Image is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            // ✅ Upload image to Cloudinary instead of local storage
            if (!cloudinaryService.isValidImage(image)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid image file or file too large (max 10MB)");
                return ResponseEntity.badRequest().body(error);
            }
            
            String imageUrl = cloudinaryService.uploadImage(image, "artworks");
            System.out.println("✅ Image uploaded to Cloudinary: " + imageUrl);
            
            // Create artwork object
            Artwork artwork = new Artwork();
            artwork.setTitle(title.trim());
            artwork.setDescription(description != null ? description.trim() : "");
            artwork.setCategory(category);
            artwork.setPrice(price);
            artwork.setRating(rating);
            artwork.setStockQuantity(stockQuantity);
            artwork.setAvailable(available);
            artwork.setImageUrl(imageUrl);  // ✅ Full Cloudinary URL
            
            // Save to database
            Artwork savedArtwork = artworkService.createArtwork(artwork);
            System.out.println("✅ Artwork created successfully with ID: " + savedArtwork.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArtwork);
        } catch (Exception e) {
            System.err.println("❌ Error uploading artwork: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload artwork: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 🎨 Update artwork
    @PutMapping("/{id}")
    public ResponseEntity<?> updateArtwork(
            @PathVariable Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(value = "stockQuantity", required = false) Integer stockQuantity,
            @RequestParam(value = "available", required = false) Boolean available,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        try {
            System.out.println("✏️ Updating artwork with ID: " + id);
            
            // Get existing artwork
            Artwork existingArtwork = artworkService.getArtworkById(id);
            
            // Update fields if provided
            if (title != null) existingArtwork.setTitle(title.trim());
            if (description != null) existingArtwork.setDescription(description.trim());
            if (category != null) existingArtwork.setCategory(category);
            if (price != null) existingArtwork.setPrice(price);
            if (rating != null) existingArtwork.setRating(rating);
            if (stockQuantity != null) existingArtwork.setStockQuantity(stockQuantity);
            if (available != null) existingArtwork.setAvailable(available);
            
            // ✅ If new image is provided, upload to Cloudinary and delete old one
            if (image != null && !image.isEmpty()) {
                // Delete old image from Cloudinary
                if (existingArtwork.getImageUrl() != null && 
                    existingArtwork.getImageUrl().contains("cloudinary.com")) {
                    cloudinaryService.deleteImage(existingArtwork.getImageUrl());
                }
                
                // Upload new image
                String newImageUrl = cloudinaryService.uploadImage(image, "artworks");
                existingArtwork.setImageUrl(newImageUrl);
                System.out.println("✅ New image uploaded: " + newImageUrl);
            }
            
            Artwork updatedArtwork = artworkService.updateArtwork(id, existingArtwork);
            System.out.println("✅ Artwork updated successfully");
            
            return ResponseEntity.ok(updatedArtwork);
        } catch (RuntimeException e) {
            System.err.println("❌ Artwork not found: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            System.err.println("❌ Error updating artwork: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update artwork: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 🎨 Delete artwork
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtwork(@PathVariable Long id) {
        try {
            System.out.println("🗑️ Deleting artwork with ID: " + id);
            
            // Get artwork to delete its image from Cloudinary
            Artwork artwork = artworkService.getArtworkById(id);
            
            // ✅ Delete image from Cloudinary
            if (artwork.getImageUrl() != null && 
                artwork.getImageUrl().contains("cloudinary.com")) {
                cloudinaryService.deleteImage(artwork.getImageUrl());
                System.out.println("✅ Image deleted from Cloudinary");
            }
            
            // Delete artwork from database
            artworkService.deleteArtwork(id);
            System.out.println("✅ Artwork deleted successfully");
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Artwork deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("❌ Artwork not found: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            System.err.println("❌ Error deleting artwork: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete artwork: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 🎨 Get artworks by category
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getArtworksByCategory(@PathVariable String category) {
        try {
            System.out.println("📋 Fetching artworks for category: " + category);
            List<Artwork> artworks = artworkService.getArtworksByCategory(category);
            System.out.println("✅ Found " + artworks.size() + " artworks");
            return ResponseEntity.ok(artworks);
        } catch (Exception e) {
            System.err.println("❌ Error fetching artworks by category: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch artworks: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 🎨 Get available artworks only
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableArtworks() {
        try {
            System.out.println("📋 Fetching available artworks...");
            List<Artwork> artworks = artworkService.getAvailableArtworks();
            System.out.println("✅ Found " + artworks.size() + " available artworks");
            return ResponseEntity.ok(artworks);
        } catch (Exception e) {
            System.err.println("❌ Error fetching available artworks: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch artworks: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
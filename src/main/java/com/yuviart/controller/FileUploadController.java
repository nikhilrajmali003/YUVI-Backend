package com.yuviart.controller;

import com.yuviart.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    /**
     * Client Image Upload (for testimonials/custom art)
     * Uses Cloudinary instead of local storage
     */
    @PostMapping("/client-image")
    public ResponseEntity<?> uploadClientImage(@RequestParam("image") MultipartFile file) {
        try {
            System.out.println("📤 Uploading client image: " + file.getOriginalFilename());
            
            // Validate image
            if (!cloudinaryService.isValidImage(file)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid image file or file too large (max 10MB)");
                return ResponseEntity.badRequest().body(error);
            }

            // Upload to Cloudinary in 'clients' folder
            String imageUrl = cloudinaryService.uploadImage(file, "clients");
            
            System.out.println("✅ Client image uploaded: " + imageUrl);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("imageUrl", imageUrl);
            response.put("url", imageUrl); // Add this for compatibility
            response.put("message", "Image uploaded successfully");
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Upload error: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * General image upload - accepts both 'file' and 'image' parameter names
     * POST /api/upload/images
     */
    @PostMapping("/images")
    public ResponseEntity<?> uploadImage(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "folder", required = false, defaultValue = "yuviart") String folder) {
        
        try {
            // Accept either 'file' or 'image' parameter
            MultipartFile uploadFile = (file != null) ? file : image;
            
            if (uploadFile == null || uploadFile.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "No file provided. Use 'file' or 'image' parameter");
                return ResponseEntity.badRequest().body(error);
            }
            
            System.out.println("📤 Uploading: " + uploadFile.getOriginalFilename() + " to folder: " + folder);
            
            if (!cloudinaryService.isValidImage(uploadFile)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid image file or file too large (max 10MB)");
                return ResponseEntity.badRequest().body(error);
            }

            String imageUrl = cloudinaryService.uploadImage(uploadFile, folder);
            
            System.out.println("✅ Upload successful: " + imageUrl);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("url", imageUrl); // Frontend expects 'url'
            response.put("imageUrl", imageUrl); // Keep for backward compatibility
            response.put("fileName", uploadFile.getOriginalFilename());
            response.put("message", "Image uploaded successfully");
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Upload error: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Delete image from Cloudinary
     * DELETE /api/upload/images
     */
    @DeleteMapping("/images")
    public ResponseEntity<?> deleteImage(@RequestParam("imageUrl") String imageUrl) {
        try {
            System.out.println("🗑️ Deleting: " + imageUrl);
            
            boolean deleted = cloudinaryService.deleteImage(imageUrl);
            
            if (deleted) {
                System.out.println("✅ Image deleted");
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Image deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Failed to delete image");
                return ResponseEntity.badRequest().body(error);
            }

        } catch (Exception e) {
            System.err.println("❌ Delete error: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Delete failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
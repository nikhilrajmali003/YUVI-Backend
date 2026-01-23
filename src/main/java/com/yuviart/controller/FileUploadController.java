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
@CrossOrigin(origins = "*")
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
     * General image upload
     * POST /api/upload/images
     */
    @PostMapping("/images")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false, defaultValue = "yuviart") String folder) {
        
        try {
            System.out.println("📤 Uploading: " + file.getOriginalFilename() + " to folder: " + folder);
            
            if (!cloudinaryService.isValidImage(file)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid image file or file too large (max 10MB)");
                return ResponseEntity.badRequest().body(error);
            }

            String imageUrl = cloudinaryService.uploadImage(file, folder);
            
            System.out.println("✅ Upload successful: " + imageUrl);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("imageUrl", imageUrl);
            response.put("fileName", file.getOriginalFilename());
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
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
@RequestMapping("/api/images")
@CrossOrigin(origins = "${app.frontend.url}")
public class ImageUploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    /**
     * Upload single image
     * POST /api/images/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false, defaultValue = "yuviart") String folder) {
        
        try {
            // Validate image
            if (!cloudinaryService.isValidImage(file)) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Invalid image file or file too large (max 10MB)"));
            }

            // Upload to Cloudinary
            String imageUrl = cloudinaryService.uploadImage(file, folder);

            // Return success response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Image uploaded successfully");
            response.put("imageUrl", imageUrl);
            response.put("fileName", file.getOriginalFilename());
            response.put("fileSize", String.format("%.2f MB", cloudinaryService.getFileSizeInMB(file)));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to upload image: " + e.getMessage()));
        }
    }

    /**
     * Upload multiple images
     * POST /api/images/upload/multiple
     */
    @PostMapping("/upload/multiple")
    public ResponseEntity<?> uploadMultipleImages(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "folder", required = false, defaultValue = "yuviart") String folder) {
        
        try {
            // Validate files
            if (files == null || files.length == 0) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("No files provided"));
            }

            // Validate each file
            for (MultipartFile file : files) {
                if (!cloudinaryService.isValidImage(file)) {
                    return ResponseEntity.badRequest()
                            .body(createErrorResponse("Invalid image file: " + file.getOriginalFilename()));
                }
            }

            // Upload all images
            String[] imageUrls = cloudinaryService.uploadMultipleImages(files, folder);

            // Return success response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Images uploaded successfully");
            response.put("imageUrls", imageUrls);
            response.put("count", imageUrls.length);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to upload images: " + e.getMessage()));
        }
    }

    /**
     * Delete image
     * DELETE /api/images/delete
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteImage(@RequestParam("imageUrl") String imageUrl) {
        try {
            boolean deleted = cloudinaryService.deleteImage(imageUrl);

            if (deleted) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Image deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Failed to delete image"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error deleting image: " + e.getMessage()));
        }
    }

    /**
     * Delete multiple images
     * DELETE /api/images/delete/multiple
     */
    @DeleteMapping("/delete/multiple")
    public ResponseEntity<?> deleteMultipleImages(@RequestBody String[] imageUrls) {
        try {
            int deletedCount = cloudinaryService.deleteMultipleImages(imageUrls);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Images deleted");
            response.put("deletedCount", deletedCount);
            response.put("totalCount", imageUrls.length);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error deleting images: " + e.getMessage()));
        }
    }

    /**
     * Get optimized image URL
     * GET /api/images/optimize
     */
    @GetMapping("/optimize")
    public ResponseEntity<?> getOptimizedImage(
            @RequestParam("imageUrl") String imageUrl,
            @RequestParam(value = "width", required = false, defaultValue = "800") int width,
            @RequestParam(value = "height", required = false, defaultValue = "600") int height) {
        
        try {
            String optimizedUrl = cloudinaryService.getOptimizedImageUrl(imageUrl, width, height);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("originalUrl", imageUrl);
            response.put("optimizedUrl", optimizedUrl);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error optimizing image: " + e.getMessage()));
        }
    }

    /**
     * Get thumbnail URL
     * GET /api/images/thumbnail
     */
    @GetMapping("/thumbnail")
    public ResponseEntity<?> getThumbnail(@RequestParam("imageUrl") String imageUrl) {
        try {
            String thumbnailUrl = cloudinaryService.getThumbnailUrl(imageUrl);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("originalUrl", imageUrl);
            response.put("thumbnailUrl", thumbnailUrl);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error creating thumbnail: " + e.getMessage()));
        }
    }

    /**
     * Helper method to create error response
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return response;
    }
}
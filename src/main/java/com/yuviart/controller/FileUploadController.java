package com.yuviart.controller;

import com.yuviart.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    // 🧍Client Image Upload (for testimonials/custom art)
    @PostMapping("/client-image")
    public ResponseEntity<String> uploadClientImage(@RequestParam("image") MultipartFile file) {
        String filename = fileStorageService.storeFile(file);
        String imageUrl = "/api/upload/images/" + filename;
        return ResponseEntity.ok(imageUrl);
    }
}

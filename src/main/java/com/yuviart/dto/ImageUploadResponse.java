package com.yuviart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadResponse {
    private boolean success;
    private String message;
    private String imageUrl;
    private String thumbnailUrl;
    private String fileName;
    private String fileSize;
    private Long timestamp;

    // Constructor for success response
    public ImageUploadResponse(String imageUrl, String fileName, String fileSize) {
        this.success = true;
        this.message = "Image uploaded successfully";
        this.imageUrl = imageUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.timestamp = System.currentTimeMillis();
    }

    // Constructor for error response
    public static ImageUploadResponse error(String message) {
        ImageUploadResponse response = new ImageUploadResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
}
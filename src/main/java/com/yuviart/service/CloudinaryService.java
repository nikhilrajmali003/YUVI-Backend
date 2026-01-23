package com.yuviart.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    /**
     * Upload image to Cloudinary
     * @param file - MultipartFile from the request
     * @param folder - Folder name in Cloudinary (optional, e.g., "products", "users")
     * @return URL of the uploaded image
     */
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Generate unique public ID
        String publicId = UUID.randomUUID().toString();

        // Upload parameters
        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "public_id", publicId,
                "folder", folder != null ? folder : "yuviart",
                "resource_type", "image",
                "overwrite", false,
                "quality", "auto",
                "fetch_format", "auto"
        );

        // Upload to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);

        // Return secure URL
        return (String) uploadResult.get("secure_url");
    }

    /**
     * Upload image with default folder
     */
    public String uploadImage(MultipartFile file) throws IOException {
        return uploadImage(file, "yuviart");
    }

    /**
     * Upload multiple images
     * @param files - Array of MultipartFiles
     * @param folder - Folder name in Cloudinary
     * @return Array of URLs
     */
    public String[] uploadMultipleImages(MultipartFile[] files, String folder) throws IOException {
        String[] urls = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            urls[i] = uploadImage(files[i], folder);
        }
        return urls;
    }

    /**
     * Delete image from Cloudinary
     * @param imageUrl - Full URL of the image
     * @return true if deleted successfully
     */
    public boolean deleteImage(String imageUrl) {
        try {
            // Extract public_id from URL
            String publicId = extractPublicId(imageUrl);
            
            if (publicId == null) {
                return false;
            }

            // Delete from Cloudinary
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            
            return "ok".equals(result.get("result"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete multiple images
     * @param imageUrls - Array of image URLs
     * @return Number of successfully deleted images
     */
    public int deleteMultipleImages(String[] imageUrls) {
        int deletedCount = 0;
        for (String url : imageUrls) {
            if (deleteImage(url)) {
                deletedCount++;
            }
        }
        return deletedCount;
    }

    /**
     * Extract public_id from Cloudinary URL
     * Example URL: https://res.cloudinary.com/dz3k46pri/image/upload/v1234567890/yuviart/abc-123.jpg
     * Returns: yuviart/abc-123
     */
    private String extractPublicId(String imageUrl) {
        try {
            if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
                return null;
            }

            // Split by /upload/
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2) {
                return null;
            }

            // Get the part after /upload/
            String afterUpload = parts[1];

            // Remove version (v1234567890/)
            if (afterUpload.contains("/")) {
                int firstSlash = afterUpload.indexOf("/");
                afterUpload = afterUpload.substring(firstSlash + 1);
            }

            // Remove file extension
            int lastDot = afterUpload.lastIndexOf(".");
            if (lastDot > 0) {
                afterUpload = afterUpload.substring(0, lastDot);
            }

            return afterUpload;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get optimized image URL with transformations (URL manipulation method)
     * This method builds the URL manually for better compatibility
     * @param imageUrl - Original Cloudinary URL
     * @param width - Desired width
     * @param height - Desired height
     * @return Transformed URL
     */
    public String getOptimizedImageUrl(String imageUrl, int width, int height) {
        try {
            if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
                return imageUrl;
            }

            // Split URL into parts
            String[] parts = imageUrl.split("/upload/");
            if (parts.length != 2) {
                return imageUrl;
            }

            // Build transformation string
            String transformation = String.format("w_%d,h_%d,c_fill,q_auto,f_auto", width, height);
            
            // Rebuild URL with transformation
            return parts[0] + "/upload/" + transformation + "/" + parts[1];
        } catch (Exception e) {
            e.printStackTrace();
            return imageUrl;
        }
    }

    /**
     * Get thumbnail URL
     * @param imageUrl - Original Cloudinary URL
     * @return Thumbnail URL (200x200)
     */
    public String getThumbnailUrl(String imageUrl) {
        return getOptimizedImageUrl(imageUrl, 200, 200);
    }

    /**
     * Get circular thumbnail (for avatars)
     * @param imageUrl - Original Cloudinary URL
     * @param size - Size of the thumbnail
     * @return Circular thumbnail URL
     */
    public String getCircularThumbnail(String imageUrl, int size) {
        try {
            if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
                return imageUrl;
            }

            String[] parts = imageUrl.split("/upload/");
            if (parts.length != 2) {
                return imageUrl;
            }

            // Transformation: width, height, crop thumb, gravity face, radius max (circular)
            String transformation = String.format("w_%d,h_%d,c_thumb,g_face,r_max,q_auto,f_auto", size, size);
            
            return parts[0] + "/upload/" + transformation + "/" + parts[1];
        } catch (Exception e) {
            e.printStackTrace();
            return imageUrl;
        }
    }

    /**
     * Get image with watermark
     * @param imageUrl - Original Cloudinary URL
     * @param watermarkText - Text to use as watermark
     * @return Image URL with watermark
     */
    public String getWatermarkedImage(String imageUrl, String watermarkText) {
        try {
            if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
                return imageUrl;
            }

            String[] parts = imageUrl.split("/upload/");
            if (parts.length != 2) {
                return imageUrl;
            }

            // URL encode the watermark text
            String encodedText = watermarkText.replace(" ", "%20");
            
            // Transformation: overlay text, position, opacity
            String transformation = String.format(
                "l_text:Arial_60_bold:%s,g_south_east,x_10,y_10,co_rgb:ffffff,o_70", 
                encodedText
            );
            
            return parts[0] + "/upload/" + transformation + "/" + parts[1];
        } catch (Exception e) {
            e.printStackTrace();
            return imageUrl;
        }
    }

    /**
     * Get blurred image (for backgrounds)
     * @param imageUrl - Original Cloudinary URL
     * @param blurStrength - Blur strength (1-2000)
     * @return Blurred image URL
     */
    public String getBlurredImage(String imageUrl, int blurStrength) {
        try {
            if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
                return imageUrl;
            }

            String[] parts = imageUrl.split("/upload/");
            if (parts.length != 2) {
                return imageUrl;
            }

            String transformation = String.format("e_blur:%d,q_auto", blurStrength);
            
            return parts[0] + "/upload/" + transformation + "/" + parts[1];
        } catch (Exception e) {
            e.printStackTrace();
            return imageUrl;
        }
    }

    /**
     * Get grayscale image
     * @param imageUrl - Original Cloudinary URL
     * @return Grayscale image URL
     */
    public String getGrayscaleImage(String imageUrl) {
        try {
            if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
                return imageUrl;
            }

            String[] parts = imageUrl.split("/upload/");
            if (parts.length != 2) {
                return imageUrl;
            }

            String transformation = "e_grayscale,q_auto";
            
            return parts[0] + "/upload/" + transformation + "/" + parts[1];
        } catch (Exception e) {
            e.printStackTrace();
            return imageUrl;
        }
    }

    /**
     * Get sepia-toned image (vintage effect)
     * @param imageUrl - Original Cloudinary URL
     * @return Sepia image URL
     */
    public String getSepiaImage(String imageUrl) {
        try {
            if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
                return imageUrl;
            }

            String[] parts = imageUrl.split("/upload/");
            if (parts.length != 2) {
                return imageUrl;
            }

            String transformation = "e_sepia,q_auto";
            
            return parts[0] + "/upload/" + transformation + "/" + parts[1];
        } catch (Exception e) {
            e.printStackTrace();
            return imageUrl;
        }
    }

    /**
     * Get image with custom transformation
     * @param imageUrl - Original Cloudinary URL
     * @param transformationString - Custom transformation string (e.g., "w_500,h_500,c_fill")
     * @return Transformed image URL
     */
    public String getCustomTransformedImage(String imageUrl, String transformationString) {
        try {
            if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
                return imageUrl;
            }

            String[] parts = imageUrl.split("/upload/");
            if (parts.length != 2) {
                return imageUrl;
            }

            return parts[0] + "/upload/" + transformationString + "/" + parts[1];
        } catch (Exception e) {
            e.printStackTrace();
            return imageUrl;
        }
    }

    /**
     * Check if file is valid image
     * @param file - MultipartFile
     * @return true if valid
     */
    public boolean isValidImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }

        // Check if it's an image
        if (!contentType.startsWith("image/")) {
            return false;
        }

        // Check file size (max 10MB)
        long maxSize = 10 * 1024 * 1024; // 10MB
        return file.getSize() <= maxSize;
    }

    /**
     * Get file size in MB
     * @param file - MultipartFile
     * @return Size in MB
     */
    public double getFileSizeInMB(MultipartFile file) {
        return file.getSize() / (1024.0 * 1024.0);
    }
}
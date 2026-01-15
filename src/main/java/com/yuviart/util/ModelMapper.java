package com.yuviart.util;

import com.yuviart.dto.*;
import com.yuviart.model.*;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class ModelMapper {
    
    // 🎨 Artwork Entity → DTO
    public ArtworkDTO toDTO(Artwork artwork) {
        if (artwork == null) return null;
        
        return new ArtworkDTO(
            artwork.getId(),
            artwork.getTitle(),
            artwork.getDescription(),
            artwork.getCategory(),
            // Convert Double to BigDecimal safely
            artwork.getPrice() != null ? BigDecimal.valueOf(artwork.getPrice()) : null,
            artwork.getImageUrl(),
            // Convert Double to Integer safely
            artwork.getRating() != null ? artwork.getRating().intValue() : null,
            artwork.getAvailable()
        );
    }
    
    // 🎨 Artwork DTO → Entity
    public Artwork toEntity(ArtworkDTO dto) {
        if (dto == null) return null;
        
        Artwork artwork = new Artwork();
        artwork.setId(dto.getId());
        artwork.setTitle(dto.getTitle());
        artwork.setDescription(dto.getDescription());
        artwork.setCategory(dto.getCategory());
        // Convert BigDecimal to Double safely
        artwork.setPrice(dto.getPrice() != null ? dto.getPrice().doubleValue() : null);
        artwork.setImageUrl(dto.getImageUrl());
        // Convert Integer to Double safely
        artwork.setRating(dto.getRating() != null ? dto.getRating().doubleValue() : null);
        artwork.setAvailable(dto.getAvailable());
        
        return artwork;
    }

    // ✉️ Contact DTO → Entity
    public ContactRequest toEntity(ContactDTO dto) {
        if (dto == null) return null;
        
        ContactRequest request = new ContactRequest();
        request.setName(dto.getName());
        request.setEmail(dto.getEmail());
        request.setArtType(dto.getArtType());
        request.setMessage(dto.getMessage());
        
        return request;
    }
    
    // 💬 Testimonial DTO → Entity
    public Testimonial toEntity(TestimonialDTO dto) {
        if (dto == null) return null;
        
        Testimonial testimonial = new Testimonial();
        testimonial.setName(dto.getName());
        testimonial.setEmail(dto.getEmail());
        testimonial.setRating(dto.getRating());
        testimonial.setText(dto.getText());
        
        return testimonial;
    }
}

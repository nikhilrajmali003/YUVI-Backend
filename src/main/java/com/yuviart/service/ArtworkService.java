package com.yuviart.service;

import com.yuvi.exception.ResourceNotFoundException;
import com.yuviart.model.Artwork;
import com.yuviart.repository.ArtworkRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArtworkService {
    private final ArtworkRepository artworkRepository;
    
    public ArtworkService(ArtworkRepository artworkRepository) {
        this.artworkRepository = artworkRepository;
    }

    public List<Artwork> getAllArtworks() {
        return artworkRepository.findAll();
    }

    public List<Artwork> getAvailableArtworks() {
        return artworkRepository.findByAvailableTrue();
    }

    public List<Artwork> getArtworksByCategory(String category) {
        return artworkRepository.findByCategoryAndAvailableTrue(category);
    }

 // Update in ArtworkService.java
    public Artwork getArtworkById(Long id) {
        return artworkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artwork not found with ID: " + id));
    }

    public Artwork createArtwork(Artwork artwork) {
        return artworkRepository.save(artwork);
    }

    public Artwork updateArtwork(Long id, Artwork artworkDetails) {
        Artwork artwork = getArtworkById(id);
        artwork.setTitle(artworkDetails.getTitle());
        artwork.setDescription(artworkDetails.getDescription());
        artwork.setCategory(artworkDetails.getCategory());
        artwork.setPrice(artworkDetails.getPrice());
        artwork.setImageUrl(artworkDetails.getImageUrl());
        artwork.setAvailable(artworkDetails.getAvailable());
        artwork.setStockQuantity(artworkDetails.getStockQuantity());
        artwork.setRating(artworkDetails.getRating());
        return artworkRepository.save(artwork);
    }

    public void deleteArtwork(Long id) {
        // Check if exists before deleting
        getArtworkById(id);
        artworkRepository.deleteById(id);
    }
}
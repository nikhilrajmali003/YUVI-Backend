package com.yuviart.repository;

import com.yuviart.model.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    List<Artwork> findByCategory(String category);
    List<Artwork> findByAvailableTrue();
    List<Artwork> findByCategoryAndAvailableTrue(String category);
    List<Artwork> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

}

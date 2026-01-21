package com.yuviart.controller;

import com.yuviart.model.Testimonial;
import com.yuviart.repository.TestimonialRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TestimonialController {

    private final TestimonialRepository testimonialRepository;

    public TestimonialController(TestimonialRepository testimonialRepository) {
        this.testimonialRepository = testimonialRepository;
    }

    // ✅ PUBLIC ENDPOINT - Only approved testimonials (for website)
    @GetMapping("/testimonials")
    public ResponseEntity<List<Testimonial>> getApprovedTestimonials() {
        return ResponseEntity.ok(testimonialRepository.findByApprovedTrue());
    }

    // ✅ PUBLIC ENDPOINT - Submit new testimonial
    @PostMapping("/testimonials")
    public ResponseEntity<Testimonial> createTestimonial(@RequestBody Testimonial testimonial) {
        testimonial.setApproved(false); // Default to pending
        Testimonial saved = testimonialRepository.save(testimonial);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ✅ ADMIN ENDPOINT - Get ALL testimonials (approved + pending)
    @GetMapping("/admin/testimonials")
    public ResponseEntity<List<Testimonial>> getAllTestimonialsForAdmin() {
        return ResponseEntity.ok(testimonialRepository.findAll());
    }

    // ✅ ADMIN ENDPOINT - Get only pending testimonials
    @GetMapping("/admin/testimonials/pending")
    public ResponseEntity<List<Testimonial>> getPendingTestimonials() {
        return ResponseEntity.ok(testimonialRepository.findByApprovedFalse());
    }

    // ✅ ADMIN ENDPOINT - Approve a testimonial
    @PutMapping("/admin/testimonials/{id}/approve")
    public ResponseEntity<Testimonial> approveTestimonial(@PathVariable Long id) {
        return testimonialRepository.findById(id)
                .map(testimonial -> {
                    testimonial.setApproved(true);
                    testimonialRepository.save(testimonial);
                    return ResponseEntity.ok(testimonial);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ ADMIN ENDPOINT - Delete a testimonial
    @DeleteMapping("/admin/testimonials/{id}")
    public ResponseEntity<Void> deleteTestimonial(@PathVariable Long id) {
        if (testimonialRepository.existsById(id)) {
            testimonialRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ LEGACY ENDPOINT - Keep for backward compatibility
    @GetMapping("/testimonials/all")
    public ResponseEntity<List<Testimonial>> getAllTestimonials() {
        return ResponseEntity.ok(testimonialRepository.findAll());
    }

    // ✅ LEGACY ENDPOINT - Keep for backward compatibility
    @PutMapping("/testimonials/{id}/approve")
    public ResponseEntity<Testimonial> approveTestimonialLegacy(@PathVariable Long id) {
        return approveTestimonial(id);
    }

    // ✅ LEGACY ENDPOINT - Keep for backward compatibility
    @DeleteMapping("/testimonials/{id}")
    public ResponseEntity<Void> deleteTestimonialLegacy(@PathVariable Long id) {
        return deleteTestimonial(id);
    }
}
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

    // ✅ PUBLIC - Only approved testimonials
    @GetMapping("/testimonials")
    public ResponseEntity<List<Testimonial>> getApprovedTestimonials() {
        return ResponseEntity.ok(testimonialRepository.findByApprovedTrue());
    }

    // ✅ PUBLIC - Submit new testimonial
    @PostMapping("/testimonials")
    public ResponseEntity<Testimonial> createTestimonial(@RequestBody Testimonial testimonial) {
        testimonial.setApproved(false);
        Testimonial saved = testimonialRepository.save(testimonial);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ✅ ADMIN - Get ALL testimonials
    @GetMapping("/admin/testimonials")
    public ResponseEntity<List<Testimonial>> getAllTestimonials() {
        return ResponseEntity.ok(testimonialRepository.findAll());
    }

    // ✅ ADMIN - Get only pending
    @GetMapping("/admin/testimonials/pending")
    public ResponseEntity<List<Testimonial>> getPendingTestimonials() {
        return ResponseEntity.ok(testimonialRepository.findByApprovedFalse());
    }

    // ✅ ADMIN - Approve testimonial
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

    // ✅ ADMIN - Delete testimonial
    @DeleteMapping("/admin/testimonials/{id}")
    public ResponseEntity<Void> deleteTestimonial(@PathVariable Long id) {
        if (testimonialRepository.existsById(id)) {
            testimonialRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
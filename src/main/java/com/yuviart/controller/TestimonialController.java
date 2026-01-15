package com.yuviart.controller;

import com.yuviart.model.Testimonial;
import com.yuviart.repository.TestimonialRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/testimonials")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175"}, allowCredentials = "true")
public class TestimonialController {

    private final TestimonialRepository testimonialRepository;

    public TestimonialController(TestimonialRepository testimonialRepository) {
        this.testimonialRepository = testimonialRepository;
    }

    // ✅ Client: Get only approved testimonials
    @GetMapping
    public ResponseEntity<List<Testimonial>> getApprovedTestimonials() {
        return ResponseEntity.ok(testimonialRepository.findByApprovedTrue());
    }

    // ✅ Client: Submit new testimonial (auto not approved)
    @PostMapping
    public ResponseEntity<Testimonial> createTestimonial(@RequestBody Testimonial testimonial) {
        testimonial.setApproved(false);
        Testimonial saved = testimonialRepository.save(testimonial);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ✅ Admin: Get all testimonials (approved + pending)
    @GetMapping("/all")
    public ResponseEntity<List<Testimonial>> getAllTestimonials() {
        return ResponseEntity.ok(testimonialRepository.findAll());
    }

    // ✅ Admin: Approve testimonial
    @PutMapping("/{id}/approve")
    public ResponseEntity<Testimonial> approveTestimonial(@PathVariable Long id) {
        return testimonialRepository.findById(id)
                .map(testimonial -> {
                    testimonial.setApproved(true);
                    testimonialRepository.save(testimonial);
                    return ResponseEntity.ok(testimonial);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Admin: Delete testimonial
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestimonial(@PathVariable Long id) {
        if (testimonialRepository.existsById(id)) {
            testimonialRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

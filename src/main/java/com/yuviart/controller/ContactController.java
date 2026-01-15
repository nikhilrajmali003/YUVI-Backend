package com.yuviart.controller;

import com.yuviart.model.ContactRequest;
import com.yuviart.repository.ContactRepository;
import com.yuviart.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {
    
    private final ContactRepository contactRepository;
    private final EmailService emailService;
    
    public ContactController(ContactRepository contactRepository, EmailService emailService) {
        this.contactRepository = contactRepository;
        this.emailService = emailService;
    }

    // 🎨 POST: handle commission/contact request
    @PostMapping
    public ResponseEntity<ContactRequest> submitContactRequest(@RequestBody ContactRequest request) {
        // Save to DB
        ContactRequest saved = contactRepository.save(request);

        try {
            // Send confirmation to the user
            emailService.sendContactConfirmation(saved);

            // Send notification to the artist (you)
            emailService.sendCommissionAlertToArtist(saved);

        } catch (Exception e) {
            System.out.println("⚠️ Email service not configured or failed: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}

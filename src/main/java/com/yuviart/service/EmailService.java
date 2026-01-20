package com.yuviart.service;

import com.yuviart.model.Order;
import com.yuviart.model.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String artistEmail = "yuviraj7232@gmail.com"; // your main email

    // 🎨 Send order confirmation to customer
    public void sendOrderConfirmation(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getCustomerEmail());
        message.setSubject("Order Confirmation - YuviArt");
        message.setText("Dear " + order.getCustomerName() + ",\n\n" +
            "Thank you for your order!\n" +
            "Order ID: " + order.getId() + "\n" +
            "Total Amount: ₹" + order.getTotalAmount() + "\n\n" +
            "We will notify you once your order is shipped.\n\n" +
            "Best regards,\nYuviArt Team");

        mailSender.send(message);
    }

    // ✉️ Send confirmation to the user for commission request
    public void sendContactConfirmation(ContactRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Commission Request Received - YuviArt");
        message.setText("Dear " + request.getName() + ",\n\n" +
            "We have received your commission request for \"" + request.getArtType() + "\".\n" +
            "Our team will review your request and get back to you within 24-48 hours.\n\n" +
            "Best regards,\nYuviArt Team");

        mailSender.send(message);
    }

    // 🧑‍🎨 Send commission request details to the artist (you)
    public void sendCommissionAlertToArtist(ContactRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(artistEmail);
        message.setSubject("🎨 New Commission Request from " + request.getName());
        message.setText(
            "Hey Yuvi 👋,\n\n" +
            "You’ve received a new commission request!\n\n" +
            "📩 Name: " + request.getName() + "\n" +
            "📧 Email: " + request.getEmail() + "\n" +
            "🎨 Art Type: " + request.getArtType() + "\n\n" +
            "📝 Message:\n" + request.getMessage() + "\n\n" +
            "Regards,\nYour YuviArt Website 💫"
        );

        mailSender.send(message);
    }
    
}
	
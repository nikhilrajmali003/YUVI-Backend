package com.yuviart.controller;

import com.yuviart.model.Order;
import com.yuviart.service.OrderService;
import com.yuviart.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;
    private final PaymentService paymentService;
    
    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order created = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<List<Order>> getOrdersByEmail(@PathVariable String email) {
        return ResponseEntity.ok(orderService.getOrdersByEmail(email));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam Order.OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @PostMapping("/payment/razorpay")
    public ResponseEntity<Map<String, String>> createRazorpayOrder(@RequestBody Map<String, Object> payload) {
        try {
            java.math.BigDecimal amount = new java.math.BigDecimal(payload.get("amount").toString());
            return ResponseEntity.ok(paymentService.createRazorpayOrder(amount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/payment/stripe")
    public ResponseEntity<Map<String, String>> createStripePayment(@RequestBody Map<String, Object> payload) {
        try {
            java.math.BigDecimal amount = new java.math.BigDecimal(payload.get("amount").toString());
            return ResponseEntity.ok(paymentService.createStripePaymentIntent(amount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
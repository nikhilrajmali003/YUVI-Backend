package com.yuviart.controller;

import com.yuviart.model.Order;
import com.yuviart.model.Order.OrderStatus;
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
 // Add these methods to your existing OrderController class:

    // Get orders by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable Order.OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }
    
    // Get pending orders
    @GetMapping("/pending")
    public ResponseEntity<List<Order>> getPendingOrders() {
        return ResponseEntity.ok(orderService.getPendingOrders());
    }
    
    // Get completed orders
    @GetMapping("/completed")
    public ResponseEntity<List<Order>> getCompletedOrders() {
        return ResponseEntity.ok(orderService.getCompletedOrders());
    }
    
    // Get cancelled orders
    @GetMapping("/cancelled")
    public ResponseEntity<List<Order>> getCancelledOrders() {
        return ResponseEntity.ok(orderService.getCancelledOrders());
    }
    
    // Delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
    
    // Cancel order
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
    
    // Get order statistics
    @GetMapping("/statistics")
    public ResponseEntity<OrderService.OrderStatistics> getOrderStatistics() {
        return ResponseEntity.ok(orderService.getOrderStatistics());
    }
}
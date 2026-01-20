package com.yuviart.service;

import com.yuviart.model.Order;
import com.yuviart.model.OrderItem;
import com.yuviart.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Order createOrder(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }

        BigDecimal total = order.getItems().stream()
            .map(item -> {
                if (item.getPrice() == null || item.getQuantity() == null) {
                    throw new IllegalArgumentException("Item price and quantity must not be null");
                }
                BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                item.setSubtotal(subtotal);
                item.setOrder(order);
                return subtotal;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);
        
        // Set default status if not provided
        if (order.getStatus() == null) {
            order.setStatus(Order.OrderStatus.PENDING);
        }
        
        Order savedOrder = orderRepository.save(order);

        try {
            emailService.sendOrderConfirmation(savedOrder);
        } catch (Exception e) {
            System.err.println("Email sending failed: " + e.getMessage());
        }

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // ===== NEW METHODS FOR ADMIN DASHBOARD =====
    
    /**
     * Get orders by specific status
     */
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * Get all pending orders (PENDING, CONFIRMED, PROCESSING, SHIPPED)
     */
    public List<Order> getPendingOrders() {
        List<Order> allOrders = orderRepository.findAll();
        return allOrders.stream()
            .filter(order -> order.getStatus() == Order.OrderStatus.PENDING ||
                           order.getStatus() == Order.OrderStatus.CONFIRMED ||
                           order.getStatus() == Order.OrderStatus.PROCESSING ||
                           order.getStatus() == Order.OrderStatus.SHIPPED)
            .collect(Collectors.toList());
    }

    /**
     * Get all completed orders (DELIVERED)
     */
    public List<Order> getCompletedOrders() {
        return orderRepository.findByStatus(Order.OrderStatus.DELIVERED);
    }

    /**
     * Get all cancelled orders
     */
    public List<Order> getCancelledOrders() {
        return orderRepository.findByStatus(Order.OrderStatus.CANCELLED);
    }

    /**
     * Delete an order by ID
     */
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    /**
     * Cancel an order
     */
    @Transactional
    public Order cancelOrder(Long id) {
        Order order = getOrderById(id);
        
        // Only allow cancellation if order is not already delivered
        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel a delivered order");
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    /**
     * Get order statistics
     */
    public OrderStatistics getOrderStatistics() {
        List<Order> allOrders = orderRepository.findAll();
        
        long totalOrders = allOrders.size();
        long pendingCount = getPendingOrders().size();
        long completedCount = getCompletedOrders().size();
        long cancelledCount = getCancelledOrders().size();
        
        BigDecimal totalRevenue = allOrders.stream()
            .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new OrderStatistics(totalOrders, pendingCount, completedCount, cancelledCount, totalRevenue);
    }

    // Inner class for statistics
    public static class OrderStatistics {
        private long totalOrders;
        private long pendingOrders;
        private long completedOrders;
        private long cancelledOrders;
        private BigDecimal totalRevenue;

        public OrderStatistics(long totalOrders, long pendingOrders, long completedOrders, 
                             long cancelledOrders, BigDecimal totalRevenue) {
            this.totalOrders = totalOrders;
            this.pendingOrders = pendingOrders;
            this.completedOrders = completedOrders;
            this.cancelledOrders = cancelledOrders;
            this.totalRevenue = totalRevenue;
        }

        // Getters
        public long getTotalOrders() { return totalOrders; }
        public long getPendingOrders() { return pendingOrders; }
        public long getCompletedOrders() { return completedOrders; }
        public long getCancelledOrders() { return cancelledOrders; }
        public BigDecimal getTotalRevenue() { return totalRevenue; }
    }
}
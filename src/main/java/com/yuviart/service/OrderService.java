package com.yuviart.service;

import com.yuviart.model.Order;
import com.yuviart.model.OrderItem;
import com.yuviart.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

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
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }
    
    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}

package com.yuviart.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🖼️ Optional: name or description of the product
    private String productName;

    // 💵 Price per unit
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    // 📦 Quantity of items ordered
    @Column(nullable = false)
    private Integer quantity;

    // 💰 Subtotal = price × quantity
    @Column(precision = 19, scale = 2)
    private BigDecimal subtotal;

    // 🔗 Relationship back to parent Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // ---------- Constructors ----------
    public OrderItem() {}

    public OrderItem(String productName, BigDecimal price, Integer quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        calculateSubtotal();
    }

    // ---------- Utility ----------
    @PrePersist
    @PreUpdate
    private void calculateSubtotal() {
        if (price != null && quantity != null) {
            this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
        }
    }

    // ---------- Getters and Setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getPrice() { return price; }  // ✅ Fixes “getPrice() undefined” error
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}

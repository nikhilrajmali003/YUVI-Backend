package com.yuviart.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDTO {
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;
    private String paymentMethod;

    public OrderDTO() {
    }

    public OrderDTO(String customerName, String customerEmail, String customerPhone,
                    String shippingAddress, List<OrderItemDTO> items, 
                    BigDecimal totalAmount, String paymentMethod) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.shippingAddress = shippingAddress;
        this.items = items;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Inner class
    public static class OrderItemDTO {
        private Long artworkId;
        private Integer quantity;
        private BigDecimal price;

        public OrderItemDTO() {
        }

        public OrderItemDTO(Long artworkId, Integer quantity, BigDecimal price) {
            this.artworkId = artworkId;
            this.quantity = quantity;
            this.price = price;
        }

        // Getters and Setters
        public Long getArtworkId() {
            return artworkId;
        }

        public void setArtworkId(Long artworkId) {
            this.artworkId = artworkId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}
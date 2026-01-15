package com.yuviart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long orderId;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentId;
    private String transactionId;
}

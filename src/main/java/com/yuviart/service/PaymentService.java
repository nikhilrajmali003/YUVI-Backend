package com.yuviart.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {
    
    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;
    
    @Value("${stripe.api.key}")
    private String stripeApiKey;
    
    public Map<String, String> createRazorpayOrder(BigDecimal amount) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_" + System.currentTimeMillis());
        
        Order order = client.orders.create(orderRequest);
        
        Map<String, String> response = new HashMap<>();
        response.put("orderId", order.get("id"));
        response.put("amount", order.get("amount").toString());
        response.put("currency", order.get("currency"));
        
        return response;
    }
    
    public Map<String, String> createStripePaymentIntent(BigDecimal amount) throws Exception {
        Stripe.apiKey = stripeApiKey;
        
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(amount.multiply(BigDecimal.valueOf(100)).longValue())
            .setCurrency("inr")
            .build();
        
        PaymentIntent intent = PaymentIntent.create(params);
        
        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        response.put("paymentIntentId", intent.getId());
        
        return response;
    }
}
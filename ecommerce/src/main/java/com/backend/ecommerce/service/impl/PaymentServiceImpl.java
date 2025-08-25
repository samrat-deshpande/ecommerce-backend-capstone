package com.backend.ecommerce.service.impl;

import com.backend.ecommerce.entity.*;
import com.backend.ecommerce.repository.PaymentRepository;
import com.backend.ecommerce.repository.OrderRepository;
import com.backend.ecommerce.repository.UserRepository;
import com.backend.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Map<String, Object> processPayment(String orderId, String userId, Map<String, Object> paymentData) {
        // Extract data from paymentData
        String paymentMethod = (String) paymentData.get("paymentMethod");
        BigDecimal amount = new BigDecimal(paymentData.get("amount").toString());

        // Validate order exists
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create payment record
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setOrderId(orderId);
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(paymentMethod.toUpperCase()));
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setTransactionId(generateTransactionId());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        // Set payment details based on method
        if (paymentMethod.equalsIgnoreCase("CREDIT_CARD")) {
            payment.setCardLastFour((String) paymentData.get("cardLastFour"));
            payment.setCardBrand((String) paymentData.get("cardBrand"));
            payment.setCardExpiryMonth((Integer) paymentData.get("cardExpiryMonth"));
            payment.setCardExpiryYear((Integer) paymentData.get("cardExpiryYear"));
        }

        // Set billing address
        payment.setBillingAddress((String) paymentData.get("billingAddress"));
        payment.setBillingCity((String) paymentData.get("billingCity"));
        payment.setBillingState((String) paymentData.get("billingState"));
        payment.setBillingCountry((String) paymentData.get("billingCountry"));
        payment.setBillingZipCode((String) paymentData.get("billingZipCode"));

        // Process payment based on method
        try {
            switch (payment.getPaymentMethod()) {
                case CREDIT_CARD:
                    processCreditCardPayment(payment, paymentData);
                    break;
                case DIGITAL_WALLET:
                    processDigitalWalletPayment(payment, paymentData);
                    break;
                case BANK_TRANSFER:
                    processBankTransferPayment(payment, paymentData);
                    break;
                default:
                    throw new RuntimeException("Unsupported payment method");
            }
        } catch (Exception e) {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason(e.getMessage());
            payment.setGatewayErrorMessage(e.getMessage());
            payment.setUpdatedAt(LocalDateTime.now());
            Payment savedPayment = paymentRepository.save(payment);
            
            Map<String, Object> response = new HashMap<>();
            response.put("payment", savedPayment);
            response.put("success", false);
            response.put("error", e.getMessage());
            return response;
        }

        Payment savedPayment = paymentRepository.save(payment);
        
        Map<String, Object> response = new HashMap<>();
        response.put("payment", savedPayment);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> processCreditCardPayment(String orderId, String userId, Map<String, Object> cardData) {
        return processPayment(orderId, userId, cardData);
    }

    @Override
    public Map<String, Object> processDigitalWalletPayment(String orderId, String userId, Map<String, Object> walletData) {
        return processPayment(orderId, userId, walletData);
    }

    @Override
    public Map<String, Object> processBankTransferPayment(String orderId, String userId, Map<String, Object> bankData) {
        return processPayment(orderId, userId, bankData);
    }

    @Override
    public Map<String, Object> getPaymentById(String paymentId, String userId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("payment", payment);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("payment", payment);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getUserPaymentHistory(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> paymentsPage = paymentRepository.findByUserId(userId, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("payments", paymentsPage.getContent());
        response.put("totalPages", paymentsPage.getTotalPages());
        response.put("totalElements", paymentsPage.getTotalElements());
        response.put("currentPage", page);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getUserPaymentHistory(String userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("payments", payments);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getPaymentsByStatus(String userId, String status) {
        Payment.PaymentStatus paymentStatus = Payment.PaymentStatus.valueOf(status.toUpperCase());
        List<Payment> payments = paymentRepository.findByStatus(paymentStatus);
        
        Map<String, Object> response = new HashMap<>();
        response.put("payments", payments);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getPaymentsByMethod(String userId, String paymentMethod) {
        Payment.PaymentMethod method = Payment.PaymentMethod.valueOf(paymentMethod.toUpperCase());
        List<Payment> payments = paymentRepository.findByPaymentMethod(method);
        
        Map<String, Object> response = new HashMap<>();
        response.put("payments", payments);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> refundPayment(String paymentId, String userId, Map<String, Object> refundData) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        if (payment.getStatus() != Payment.PaymentStatus.SUCCESSFUL) {
            throw new RuntimeException("Only successful payments can be refunded");
        }

        BigDecimal refundAmount = new BigDecimal(refundData.get("amount").toString());
        if (refundAmount.compareTo(payment.getAmount()) > 0) {
            throw new RuntimeException("Refund amount cannot exceed payment amount");
        }

        // TODO: Process refund through payment gateway
        // This would typically involve:
        // 1. Calling the payment gateway's refund API
        // 2. Handling the response
        // 3. Updating payment status

        if (refundAmount.compareTo(payment.getAmount()) == 0) {
            payment.setStatus(Payment.PaymentStatus.REFUNDED);
        } else {
            payment.setStatus(Payment.PaymentStatus.PARTIALLY_REFUNDED);
        }

        payment.setUpdatedAt(LocalDateTime.now());
        Payment savedPayment = paymentRepository.save(payment);
        
        Map<String, Object> response = new HashMap<>();
        response.put("payment", savedPayment);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> partialRefundPayment(String paymentId, String userId, Double amount, String reason) {
        Map<String, Object> refundData = new HashMap<>();
        refundData.put("amount", amount);
        refundData.put("reason", reason);
        return refundPayment(paymentId, userId, refundData);
    }

    @Override
    public Map<String, Object> cancelPayment(String paymentId, String userId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        if (payment.getStatus() != Payment.PaymentStatus.PENDING) {
            throw new RuntimeException("Only pending payments can be cancelled");
        }

        payment.setStatus(Payment.PaymentStatus.CANCELLED);
        payment.setUpdatedAt(LocalDateTime.now());
        
        Payment savedPayment = paymentRepository.save(payment);
        
        Map<String, Object> response = new HashMap<>();
        response.put("payment", savedPayment);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> validatePaymentMethod(String paymentMethod, Map<String, Object> paymentData) {
        try {
            Payment.PaymentMethod.valueOf(paymentMethod.toUpperCase());
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("message", "Payment method is valid");
            return response;
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("message", "Invalid payment method");
            return response;
        }
    }

    @Override
    public Map<String, Object> validateCreditCard(Map<String, Object> cardData) {
        String cardNumber = (String) cardData.get("cardNumber");
        String expiryMonth = (String) cardData.get("expiryMonth");
        String expiryYear = (String) cardData.get("expiryYear");
        
        // TODO: Implement credit card validation logic
        // This would typically involve:
        // 1. Luhn algorithm check
        // 2. Expiry date validation
        // 3. Card type detection
        
        boolean isValid = cardNumber != null && cardNumber.length() >= 13 && cardNumber.length() <= 19;
        
        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);
        response.put("message", isValid ? "Credit card is valid" : "Credit card is invalid");
        return response;
    }

    @Override
    public Map<String, Object> getPaymentReceipt(String paymentId, String userId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        Map<String, Object> receipt = new HashMap<>();
        receipt.put("paymentId", paymentId);
        receipt.put("transactionId", payment.getTransactionId());
        receipt.put("amount", payment.getAmount());
        receipt.put("paymentMethod", payment.getPaymentMethod());
        receipt.put("status", payment.getStatus());
        receipt.put("processedAt", payment.getProcessedAt());
        receipt.put("orderId", payment.getOrderId());
        receipt.put("success", true);
        
        return receipt;
    }

    @Override
    public Map<String, Object> resendPaymentReceipt(String paymentId, String userId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        // TODO: Implement email sending logic
        // This would typically involve:
        // 1. Getting user email from payment
        // 2. Sending receipt email
        // 3. Logging the action
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Payment receipt email sent");
        return response;
    }

    @Override
    public Map<String, Object> getUserPaymentStats(String userId) {
        List<Payment> userPayments = paymentRepository.findByUserId(userId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPayments", userPayments.size());
        stats.put("totalAmount", userPayments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        stats.put("successfulPayments", (int) userPayments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.SUCCESSFUL)
                .count());
        stats.put("failedPayments", (int) userPayments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.FAILED)
                .count());
        stats.put("success", true);
        
        return stats;
    }

    @Override
    public Map<String, Object> getPaymentForAdmin(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("payment", payment);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getAllPayments(int page, int size, String status, String userId, String paymentMethod, String startDate, String endDate) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> paymentsPage = paymentRepository.findAll(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("payments", paymentsPage.getContent());
        response.put("totalPages", paymentsPage.getTotalPages());
        response.put("totalElements", paymentsPage.getTotalElements());
        response.put("currentPage", page);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> retryFailedPayment(String paymentId, String userId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        if (payment.getStatus() != Payment.PaymentStatus.FAILED) {
            throw new RuntimeException("Only failed payments can be retried");
        }

        // TODO: Implement retry logic
        // This would typically involve:
        // 1. Resetting payment status to PENDING
        // 2. Attempting payment processing again
        // 3. Updating payment record
        
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setUpdatedAt(LocalDateTime.now());
        
        Payment savedPayment = paymentRepository.save(payment);
        
        Map<String, Object> response = new HashMap<>();
        response.put("payment", savedPayment);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getAvailablePaymentMethods() {
        // TODO: Implement dynamic payment method availability
        // This could be based on user location, order amount, etc.
        List<Payment.PaymentMethod> methods = List.of(
            Payment.PaymentMethod.CREDIT_CARD,
            Payment.PaymentMethod.DIGITAL_WALLET,
            Payment.PaymentMethod.BANK_TRANSFER
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("paymentMethods", methods);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> checkPaymentGatewayStatus() {
        // TODO: Implement gateway health check
        // This would typically involve:
        // 1. Calling a health check endpoint
        // 2. Checking response time
        // 3. Validating authentication
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "HEALTHY");
        response.put("responseTime", 150);
        response.put("success", true);
        return response;
    }

    @Override
    public String generateTransactionId() {
        String timestamp = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "TXN-" + timestamp + "-" + random;
    }

    @Override
    public Map<String, Object> calculatePaymentFees(Double amount, String paymentMethod) {
        // TODO: Implement dynamic fee calculation
        // This could be based on payment method, amount, user tier, etc.
        
        BigDecimal feeAmount;
        switch (Payment.PaymentMethod.valueOf(paymentMethod.toUpperCase())) {
            case CREDIT_CARD:
                feeAmount = BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(0.029)).add(BigDecimal.valueOf(0.30)); // 2.9% + $0.30
                break;
            case DIGITAL_WALLET:
                feeAmount = BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(0.025)); // 2.5%
                break;
            case BANK_TRANSFER:
                feeAmount = BigDecimal.valueOf(0.50); // Fixed fee
                break;
            default:
                feeAmount = BigDecimal.ZERO;
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("amount", amount);
        response.put("paymentMethod", paymentMethod);
        response.put("feeAmount", feeAmount);
        response.put("success", true);
        return response;
    }

    private void processCreditCardPayment(Payment payment, Map<String, Object> paymentData) {
        // TODO: Integrate with actual payment gateway
        // This would typically involve:
        // 1. Calling the payment gateway's API
        // 2. Handling the response
        // 3. Updating payment status based on response
        
        // Simulate successful payment for now
        payment.setStatus(Payment.PaymentStatus.SUCCESSFUL);
        payment.setProcessedAt(LocalDateTime.now());
        payment.setGatewayResponse("SUCCESS");
    }

    private void processDigitalWalletPayment(Payment payment, Map<String, Object> paymentData) {
        // TODO: Integrate with digital wallet providers
        payment.setStatus(Payment.PaymentStatus.SUCCESSFUL);
        payment.setProcessedAt(LocalDateTime.now());
        payment.setGatewayResponse("SUCCESS");
    }

    private void processBankTransferPayment(Payment payment, Map<String, Object> paymentData) {
        // TODO: Integrate with bank transfer services
        payment.setStatus(Payment.PaymentStatus.PENDING); // Bank transfers are typically pending
        payment.setGatewayResponse("PENDING");
    }
}

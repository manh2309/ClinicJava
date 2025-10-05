package org.example.clinicjava.service;

import org.example.clinicjava.dto.request.PaymentCallbackRequest;
import org.example.clinicjava.dto.response.ApiResponse;

public interface PaymentService {
    ApiResponse<Object> handlePaymentCallback(PaymentCallbackRequest request);
    ApiResponse<Object> confirmCashPayment(Long paymentId);
    ApiResponse<Object> choosePaymentMethod(Long paymentId, Long method, Long accountId);
}

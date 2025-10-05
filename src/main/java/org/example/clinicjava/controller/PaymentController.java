package org.example.clinicjava.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clinicjava.dto.request.ChoosePaymentMethodRequest;
import org.example.clinicjava.dto.request.PaymentCallbackRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.service.PaymentService;
import org.example.clinicjava.ultils.CommonUtils;
import org.example.clinicjava.ultils.account.CustomUserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/v1/payments")
public class PaymentController {
    PaymentService paymentService;
    // Callback cho các cổng thanh toán online
    @PostMapping("/callback")
    public ApiResponse<Object> paymentCallback(@RequestBody PaymentCallbackRequest request) {
        return paymentService.handlePaymentCallback(request);
    }

    // Xác nhận thanh toán tiền mặt
    @PutMapping("/confirm-cash/{paymentId}")
    public ApiResponse<Object> confirmCashPayment(@PathVariable Long paymentId) {
        return paymentService.confirmCashPayment(paymentId);
    }

    // Chọn phương thức thanh toaán
    @PutMapping("/method/{paymentId}")
    public ApiResponse<Object> choosePaymentMethod(@PathVariable Long paymentId,
                                                   @RequestBody ChoosePaymentMethodRequest request) {
        CustomUserDetails userDetails = CommonUtils.getUserDetails();
        return paymentService.choosePaymentMethod(paymentId, request.getMethod(), userDetails.getAccountId());
    }
}

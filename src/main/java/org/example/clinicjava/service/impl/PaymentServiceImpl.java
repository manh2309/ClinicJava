package org.example.clinicjava.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.clinicjava.constant.Constant;
import org.example.clinicjava.dto.request.PaymentCallbackRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.entity.Payment;
import org.example.clinicjava.exception.AppException;
import org.example.clinicjava.exception.StatusCode;
import org.example.clinicjava.repository.AccountRepository;
import org.example.clinicjava.repository.PaymentRepository;
import org.example.clinicjava.service.NotificationService;
import org.example.clinicjava.service.PaymentService;
import org.example.clinicjava.ultils.CommonUtils;
import org.example.clinicjava.ultils.account.CustomUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final AccountRepository accountRepository;
    @Override
    public ApiResponse<Object> handlePaymentCallback(PaymentCallbackRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND)));

        if ("SUCCESS".equalsIgnoreCase(request.getStatus())) {
            payment.setStatus(Constant.PAYMENT_STATUS.PAID);
            payment.setTransactionCode(request.getTransactionCode());
        } else {
            payment.setStatus(Constant.PAYMENT_STATUS.CANCELLED);
        }
        payment.setModifiedDate(LocalDateTime.now());
        payment = paymentRepository.save(payment);
        List<Long> accountAdmin = accountRepository.findByRoleId(Constant.ROLE_NAME.ROLE_ADMIN.getCode());
        List<Long> recipients = new ArrayList<>();
        recipients.add(payment.getCreatedBy());
        recipients.addAll(accountAdmin);
        notificationService.sendNotificationToMany(
                recipients,
                "Thanh toán thành công",
                "Thanh toán cho lịch hẹn #" + payment.getAppointmentId() + " đã được xác nhận.",
                "PAYMENT_SUCCESS"
        );
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(Constant.MESSAGE.PAYMENT_SUCCESS)
                .result(payment.getStatus())
                .build();
    }

    @Override
    public ApiResponse<Object> confirmCashPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND)));

        if (!Constant.PAYMENT_METHOD.CASH.equals(payment.getMethod())) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.PAYMENT_NOT_CASH));
        }

        payment.setStatus(Constant.PAYMENT_STATUS.PAID);
        payment.setModifiedDate(LocalDateTime.now());

        CustomUserDetails userDetails = CommonUtils.getUserDetails();
        if (userDetails != null) {
            payment.setModifiedBy(userDetails.getAccountId());
        }

        payment = paymentRepository.save(payment);
        List<Long> accountAdmin = accountRepository.findByRoleId(Constant.ROLE_NAME.ROLE_ADMIN.getCode());
        List<Long> recipients = new ArrayList<>();
        recipients.add(payment.getCreatedBy());
        recipients.addAll(accountAdmin);
        notificationService.sendNotificationToMany(
                recipients,
                "Thanh toán tiền mặt thành công",
                "Thanh toán cho lịch hẹn #" + payment.getAppointmentId() + " đã được xác nhận bởi bác sĩ.",
                "PAYMENT_CASH"
        );
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(Constant.MESSAGE.PAYMENT_CASH_SUCCESS)
                .result(payment.getStatus())
                .build();
    }

    @Override
    public ApiResponse<Object> choosePaymentMethod(Long paymentId, Long method, Long accountId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND)));

        // validate phương thức
        List<Long> validMethods = List.of(
                Constant.PAYMENT_METHOD.BANK_TRANSFER,
                Constant.PAYMENT_METHOD.MOMO,
                Constant.PAYMENT_METHOD.VNPAY,
                Constant.PAYMENT_METHOD.CASH
        );
        if (!validMethods.contains(method)) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.PAYMENT_METHOD_NOT_FOUND));
        }

        payment.setMethod(method);
        payment.setModifiedBy(accountId);
        payment.setModifiedDate(LocalDateTime.now());
        paymentRepository.save(payment);

        // Nếu là online thì trả mock link
        String paymentUrl = null;
        if (method.equals(Constant.PAYMENT_METHOD.MOMO)) {
            paymentUrl = "https://momo.mock/checkout?paymentId=" + paymentId;
        } else if (method.equals(Constant.PAYMENT_METHOD.VNPAY)) {
            paymentUrl = "https://vnpay.mock/checkout?paymentId=" + paymentId;
        } else if (method.equals(Constant.PAYMENT_METHOD.BANK_TRANSFER)) {
            paymentUrl = "https://bank.mock/checkout?paymentId=" + paymentId;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("paymentId", paymentId);
        result.put("method", method);
        result.put("paymentUrl", paymentUrl);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(result)
                .build();
    }
}

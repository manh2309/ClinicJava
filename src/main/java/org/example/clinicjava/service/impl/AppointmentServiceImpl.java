package org.example.clinicjava.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clinicjava.configuration.security.JwtUtil;
import org.example.clinicjava.constant.Constant;
import org.example.clinicjava.dto.request.CreateAppointmentRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.dto.response.AppointmentResponse;
import org.example.clinicjava.dto.response.PageResponse;
import org.example.clinicjava.entity.Account;
import org.example.clinicjava.entity.Appointment;
import org.example.clinicjava.entity.Payment;
import org.example.clinicjava.exception.AppException;
import org.example.clinicjava.exception.StatusCode;
import org.example.clinicjava.mapper.AppointmentMapper;
import org.example.clinicjava.repository.AccountRepository;
import org.example.clinicjava.repository.AppointmentRepository;
import org.example.clinicjava.repository.PaymentRepository;
import org.example.clinicjava.service.AppointmentService;
import org.example.clinicjava.service.NotificationService;
import org.example.clinicjava.ultils.CommonUtils;
import org.example.clinicjava.ultils.account.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    AppointmentRepository appointmentRepository;
    AccountRepository accountRepository;
    AppointmentMapper appointmentMapper;
    PaymentRepository paymentRepository;
    NotificationService notificationService;
    JwtUtil jwtUtil;
    private static final BigDecimal DEFAULT_FEE = BigDecimal.valueOf(300000);
    @Override
    public ApiResponse<Object> createAppointment(CreateAppointmentRequest request, Boolean isSaveDraft) {
        Account doctorsAccount = accountRepository.findByIdAndRoleDoctor(request.getDoctorId(), Constant.ROLE_NAME.ROLE_DOCTOR.getCode())
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND)));

        Appointment entity = appointmentMapper.toEntity(request);
        CustomUserDetails userDetails = CommonUtils.getUserDetails();
        if (userDetails != null) {
            entity.setPatientId(userDetails.getAccountId());
        }
        entity.setDoctorId(doctorsAccount.getAccountId());
        entity.setIsActive(Constant.ACTIVE.IS_ACTIVE);
        entity.setFee(DEFAULT_FEE);
        entity.setStatus(Constant.APOINTMENT_STATUS.PENDING);
        if (Boolean.TRUE.equals(isSaveDraft)) {
            entity.setIsDraft(1L); // lưu nháp
        } else {
            entity.setIsDraft(0L); // chính thức
        }
        appointmentRepository.save(entity);

        Payment payment = Payment.builder()
                .appointmentId(entity.getAppointmentId())
                .amount(String.valueOf(DEFAULT_FEE))
                .method(Constant.PAYMENT_STATUS.UNDEFINED)             // chưa chọn phương thức
                .status(Constant.PAYMENT_STATUS.UNPAID)                // trạng thái ban đầu
                .createdDate(LocalDateTime.now())
                .createdBy(userDetails != null ? userDetails.getAccountId() : null)
                .build();

        paymentRepository.save(payment);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(Constant.MESSAGE.APPOINTMENT_CREATE_SUCCESS)
                .build();
    }

    @Override
    public ApiResponse<Object> getMyAppointments(Pageable pageable, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String role = jwtUtil.getRoleFromToken(token);
        CustomUserDetails userDetails = CommonUtils.getUserDetails();
        Page<AppointmentResponse> result = appointmentRepository
                .searchAppointment(userDetails.getAccountId(), role, pageable).map(appointmentMapper::toDto);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(new PageResponse<>(result.getContent(), result.getTotalPages(), result.getTotalElements()))
                .build();
    }

    @Override
    public ApiResponse<Object> cancelAppointments(Long appointmentId, Long patientId) {
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND)));

        if(!existingAppointment.getPatientId().equals(patientId)){
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.APPOINTMENT_NOT_EQUAL));
        }

        if(!List.of(Constant.APOINTMENT_STATUS.PENDING, Constant.APOINTMENT_STATUS.CONFIRMED).contains(existingAppointment.getStatus())){
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.APPOINTMENT_NOT_CANCELLED));
        }
        existingAppointment.setStatus(Constant.APOINTMENT_STATUS.CANCELLED);
        appointmentRepository.save(existingAppointment);

        Long doctorId = existingAppointment.getDoctorId();
        List<Long> accountAdmin = accountRepository.findByRoleId(Constant.ROLE_NAME.ROLE_ADMIN.getCode());

        List<Long> recipients = new ArrayList<>();
        recipients.add(doctorId);
        recipients.addAll(accountAdmin);
        notificationService.sendNotificationToMany(
                recipients,
                "Lịch hẹn #" + appointmentId + " đã bị hủy",
                "Bệnh nhân đã hủy lịch hẹn #" + appointmentId,
                "APPOINTMENT_CANCELLED"
        );
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(Constant.MESSAGE.APPOINTMENT_CANCELLED_SUCCESS)
                .build();
    }
}

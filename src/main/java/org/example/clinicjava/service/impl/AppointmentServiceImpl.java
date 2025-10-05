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
import java.util.Objects;

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
            entity.setCreatedBy(userDetails.getAccountId());
            entity.setCreatedDate(LocalDateTime.now());
        }
        entity.setDoctorId(doctorsAccount.getAccountId());
        entity.setIsActive(Constant.ACTIVE.IS_ACTIVE);
        entity.setFee(DEFAULT_FEE);
        entity.setStatus(Constant.APOINTMENT_STATUS.PENDING);
        if (Boolean.TRUE.equals(isSaveDraft)) {
            entity.setIsDraft(Constant.SAVE.IS_SAVE_DRAFT); // lưu nháp
        } else {
            entity.setIsDraft(Constant.SAVE.IS_SAVE); // chính thức
        }
        entity = appointmentRepository.save(entity);

        Payment payment = Payment.builder()
                .appointmentId(entity.getAppointmentId())
                .amount(String.valueOf(DEFAULT_FEE))
                .method(Constant.PAYMENT_METHOD.UNDEFINED)             // chưa chọn phương thức
                .status(Constant.PAYMENT_STATUS.UNPAID)                // trạng thái ban đầu
                .createdDate(LocalDateTime.now())
                .createdBy(userDetails != null ? userDetails.getAccountId() : null)
                .build();

        paymentRepository.save(payment);
        List<Long> accountAdmin = accountRepository.findByRoleId(Constant.ROLE_NAME.ROLE_ADMIN.getCode());

        List<Long> recipients = new ArrayList<>();
        recipients.add(doctorsAccount.getAccountId());
        recipients.addAll(accountAdmin);
        notificationService.sendNotificationToMany(
                recipients,
                "Lịch hẹn mới #" + entity.getAppointmentId(),
                "Bệnh nhân " + userDetails.getUsername() + " vừa đặt lịch khám mới.",
                "APPOINTMENT_CREATED"
        );
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

        if(!Constant.ACTIVE.IS_ACTIVE.equals(existingAppointment.getIsActive())
            || !List.of(Constant.APOINTMENT_STATUS.PENDING, Constant.APOINTMENT_STATUS.CONFIRMED).contains(existingAppointment.getStatus())){
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.APPOINTMENT_NOT_CANCELLED));
        }
        existingAppointment.setStatus(Constant.APOINTMENT_STATUS.CANCELLED);
        existingAppointment.setModifiedDate(LocalDateTime.now());
        existingAppointment.setModifiedBy(patientId);
        appointmentRepository.save(existingAppointment);

        // update payment liên quan (nếu có)
        paymentRepository.findByAppointmentId(appointmentId).ifPresent(payment -> {
            if (!payment.getStatus().equals(Constant.PAYMENT_STATUS.CANCELLED)) {
                payment.setStatus(Constant.PAYMENT_STATUS.CANCELLED);
                payment.setModifiedDate(LocalDateTime.now());
                payment.setModifiedBy(patientId);
                paymentRepository.save(payment);
            }
        });

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

    @Override
    public ApiResponse<Object> confirmAppointments(Long appointmentId, Long accountId) {
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND)));

        Account adminAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND)));

        if(!Constant.ACTIVE.IS_ACTIVE.equals(existingAppointment.getIsActive())
                || !Objects.equals(Constant.APOINTMENT_STATUS.PENDING, existingAppointment.getStatus())
                || !Constant.SAVE.IS_SAVE.equals(existingAppointment.getIsDraft())){
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.APPOINTMENT_NOT_CONFIRM));
        }
        existingAppointment.setStatus(Constant.APOINTMENT_STATUS.CONFIRMED);
        existingAppointment.setModifiedDate(LocalDateTime.now());
        existingAppointment.setModifiedBy(adminAccount.getAccountId());
        appointmentRepository.save(existingAppointment);
        Long doctorId = existingAppointment.getDoctorId();
        List<Long> accountAdmin = accountRepository.findByRoleId(Constant.ROLE_NAME.ROLE_ADMIN.getCode());

        List<Long> recipients = new ArrayList<>();
        recipients.add(doctorId);
        recipients.addAll(accountAdmin);
        notificationService.sendNotificationToMany(
                recipients,
                "Lịch hẹn #" + appointmentId + " đã được xác nhận",
                "Hệ thống đã được xác nhận lịch hẹn #" + appointmentId,
                "APPOINTMENT_CONFIRMATION"
        );
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(Constant.MESSAGE.APPOINTMENT_CONFIRM_SUCCESS)
                .build();
    }

    @Override
    public ApiResponse<Object> completeAppointment(Long appointmentId) {
        // Lấy appointment từ DB
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND)));

        CustomUserDetails userDetails = CommonUtils.getUserDetails();

        if (!appointment.getDoctorId().equals(userDetails.getAccountId())) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.DOCTOT_FORBIDDEN));
        }

        if (!Constant.ACTIVE.IS_ACTIVE.equals(appointment.getIsActive())
             && !Constant.APOINTMENT_STATUS.CONFIRMED.equals(appointment.getStatus())) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.APPOINTMENT_NOT_COMPLETED));
        }

        appointment.setStatus(Constant.APOINTMENT_STATUS.COMPLETED);
        appointment.setModifiedDate(LocalDateTime.now());
        appointmentRepository.save(appointment);

        notificationService.sendNotification(
                appointment.getPatientId(),
                "Lịch hẹn #" + appointmentId + " đã hoàn thành",
                "Bác sĩ đã đánh dấu lịch hẹn #" + appointmentId + " là hoàn thành",
                "APPOINTMENT_COMPLETED"
        );

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(Constant.MESSAGE.APPOINTMENT_COMPLETED_SUCCESS)
                .build();
    }
}

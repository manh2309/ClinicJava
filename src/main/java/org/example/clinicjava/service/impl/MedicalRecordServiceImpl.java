package org.example.clinicjava.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.clinicjava.constant.Constant;
import org.example.clinicjava.dto.request.CreateMedicalRecordRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.dto.response.MedicalRecordResponse;
import org.example.clinicjava.dto.response.PageResponse;
import org.example.clinicjava.entity.Appointment;
import org.example.clinicjava.entity.MedicalRecord;
import org.example.clinicjava.exception.AppException;
import org.example.clinicjava.exception.StatusCode;
import org.example.clinicjava.mapper.MedicalRecordMapper;
import org.example.clinicjava.repository.AppointmentRepository;
import org.example.clinicjava.repository.MedicalRecordRepository;
import org.example.clinicjava.service.MedicalRecordService;
import org.example.clinicjava.ultils.CommonUtils;
import org.example.clinicjava.ultils.account.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordMapper medicalRecordMapper;

    @Override
    public ApiResponse<Object> createMedicalRecord(CreateMedicalRecordRequest request) {
        CustomUserDetails user = CommonUtils.getUserDetails();

        // Chỉ bác sĩ được tạo hồ sơ
        if (!Constant.ROLE_NAME.ROLE_DOCTOR.getName().equals(user.getRole())) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.MEDICAL_ROLE_FORBIDDEN));
        }

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.APPOINTMENT_NOT_FOUND)));

        // Chỉ tạo nếu appointment đã hoàn thành
        if (!Constant.APOINTMENT_STATUS.COMPLETED.equals(appointment.getStatus())) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.APPOINTMENT_NOT_COMPLETED_V1));
        }

        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(request);
        medicalRecord.setRecordDate(LocalDateTime.now());
        medicalRecord.setIsActive(Constant.ACTIVE.IS_ACTIVE);
        medicalRecord.setCreatedBy(user.getAccountId());
        medicalRecord.setCreatedDate(LocalDateTime.now());
        medicalRecordRepository.save(medicalRecord);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(Constant.MESSAGE.MEDICALRECORD_SUCCESS)
                .result(medicalRecord)
                .build();
    }

    @Override
    public ApiResponse<Object> getMedicalRecordById(Long id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND)));

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .result(medicalRecord)
                .build();
    }

    @Override
    public ApiResponse<Object> getMedicalRecordsByPatient(Long patientId) {
        List<MedicalRecord> medicalRecord = medicalRecordRepository.findByPatientId(patientId);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .result(medicalRecord)
                .build();
    }

    @Override
    public ApiResponse<Object> searchListMedical(Pageable pageable) {
        CustomUserDetails user = CommonUtils.getUserDetails();

        Page<MedicalRecordResponse> medicalRecord = switch (user.getRole()) {
            case "ROLE_PATIENT" -> medicalRecordRepository.searchMedicalRecordByPatientId(user.getAccountId(), pageable).map(medicalRecordMapper::toDto);

            case "ROLE_DOCTOR" -> medicalRecordRepository.searchMedicalRecordByDoctorId(user.getAccountId(), pageable).map(medicalRecordMapper::toDto);

            default -> medicalRecordRepository.findAll(pageable).map(medicalRecordMapper::toDto);
        };

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .result(new PageResponse<>(medicalRecord.getContent(), medicalRecord.getTotalPages(), medicalRecord.getTotalElements()))
                .build();
    }
}

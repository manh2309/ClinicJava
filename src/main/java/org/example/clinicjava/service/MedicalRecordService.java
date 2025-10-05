package org.example.clinicjava.service;

import org.example.clinicjava.dto.request.CreateMedicalRecordRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface MedicalRecordService {
    ApiResponse<Object> createMedicalRecord(CreateMedicalRecordRequest request);
    ApiResponse<Object> getMedicalRecordById(Long id);
    ApiResponse<Object> getMedicalRecordsByPatient(Long patientId);
    ApiResponse<Object> searchListMedical(Pageable pageable);
}
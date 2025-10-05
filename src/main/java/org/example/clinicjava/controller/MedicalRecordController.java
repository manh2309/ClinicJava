package org.example.clinicjava.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clinicjava.dto.request.CreateMedicalRecordRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.service.MedicalRecordService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/v1/medical-records")
public class MedicalRecordController {
    MedicalRecordService medicalRecordService;
    @GetMapping("/{id}")
    public ApiResponse<Object> getMedicalRecordById(@PathVariable Long id) {
        return medicalRecordService.getMedicalRecordById(id);
    }

    @GetMapping("/patient/{patientId}")
    public ApiResponse<Object> getMedicalRecordsByPatient(@PathVariable Long patientId) {
        return medicalRecordService.getMedicalRecordsByPatient(patientId);
    }

    @PostMapping("/create")
    public ApiResponse<Object> createMedicalRecord(@RequestBody CreateMedicalRecordRequest request) {
        return medicalRecordService.createMedicalRecord(request);
    }

    @GetMapping("/searchList")
    public ApiResponse<Object> getMedicalRecordsBySearchList(Pageable pageable) {
        return medicalRecordService.searchListMedical(pageable);
    }
}

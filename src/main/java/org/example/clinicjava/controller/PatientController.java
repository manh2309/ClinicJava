package org.example.clinicjava.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.service.AccountService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/v1/patients")
public class PatientController {
    AccountService accountService;

    @GetMapping("/list")
    public ApiResponse<Object> getListPatient(Pageable pageable) {
        return accountService.getPatientsForDoctor(pageable);
    }

    @GetMapping("/list-patient-admin")
    public ApiResponse<Object> getListPatientForAdmin(Pageable pageable) {
        return accountService.getPatientsForAdmin(pageable);
    }
}

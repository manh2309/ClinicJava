package org.example.clinicjava.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clinicjava.dto.request.CreateAppointmentRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.service.AppointmentService;
import org.example.clinicjava.ultils.CommonUtils;
import org.example.clinicjava.ultils.account.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/v1/appointments")
@Transactional
public class AppointmentController {
    AppointmentService appointmentService;

    @PostMapping("/create")
    public ApiResponse<Object> createAppointment(
            @RequestBody CreateAppointmentRequest request,
            @RequestParam(name = "isSaveDraft", defaultValue = "false") Boolean isSaveDraft) {
        return appointmentService.createAppointment(request, isSaveDraft);
    }

    @GetMapping("/list")
    public ApiResponse<Object> getAppointments(HttpServletRequest request, Pageable pageable) {
        return appointmentService.getMyAppointments(pageable, request);
    }

    @PutMapping("/cancel/{id}")
    public ApiResponse<Object> cancelAppointment(@PathVariable("id") Long appointmentId) {
        CustomUserDetails userDetails = CommonUtils.getUserDetails();
        return appointmentService.cancelAppointments(appointmentId, userDetails.getAccountId());
    }
}

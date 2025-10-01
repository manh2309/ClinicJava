package org.example.clinicjava.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.clinicjava.dto.request.CreateAppointmentRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface AppointmentService {

    ApiResponse<Object> createAppointment(CreateAppointmentRequest request, Boolean isSaveDraft);
    ApiResponse<Object> getMyAppointments(Pageable pageable, HttpServletRequest httpServletRequest);
    ApiResponse<Object> cancelAppointments(Long appointmentId, Long patientId);
    ApiResponse<Object> confirmAppointments(Long appointmentId, Long accountId);
    ApiResponse<Object> completeAppointment(Long appointmentId);

}

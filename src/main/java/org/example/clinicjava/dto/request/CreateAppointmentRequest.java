package org.example.clinicjava.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppointmentRequest {
    private Long doctorId;
    private Long appointmentId;
    private LocalDateTime appointmentDate;
    private String note;
    private String patientName;
    private int patientAge;
}

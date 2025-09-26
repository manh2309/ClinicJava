package org.example.clinicjava.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppointmentResponse {
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private String patientName;
    private int patientAge;
    private LocalDateTime appointmentDate;
    private BigDecimal fee;
    private String note;
    private LocalDateTime createdDate;
}

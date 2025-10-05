package org.example.clinicjava.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordResponse {
    private Long medicalRecordId;
    private Long appointmentId;
    private Long doctorId;
    private Long patientId;
    private String patientName;
    private Integer patientAge;
    private String symptoms;
    private String diagnosis;
    private String treatment;
    private LocalDateTime recordDate;
}

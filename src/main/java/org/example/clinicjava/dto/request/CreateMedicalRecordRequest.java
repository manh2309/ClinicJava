package org.example.clinicjava.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMedicalRecordRequest {
    private Long appointmentId;
    private Long patientId;
    private String patientName;
    private Integer patientAge;
    private String symptoms;
    private String diagnosis;
    private String treatment;
}

package org.example.clinicjava.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "medical_record")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEDICAL_RECORD_ID")
    private Long medicalRecordId;

    @Column(name = "PATIENT_ID")
    private Long patientId;

    @Column(name = "DOCTOR_ID")
    private Long doctorId;

    @Column(name = "APPOINTMENT_ID")
    private Long appointmentId;

    @Column(name = "PATIENT_NAME")
    private String patientName;

    @Column(name = "PATIENT_AGE")
    private Integer patientAge;

    @Column(name = "SYMPTOMS", columnDefinition = "TEXT")
    private String symptoms;

    @Column(name = "DIAGNOSIS", columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "TREATMENT", columnDefinition = "TEXT")
    private String treatment;

    @Column(name = "RECORD_DATE")
    private LocalDateTime recordDate;

    @Column(name = "IS_ACTIVE")
    private Long isActive;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_BY")
    private Long modifiedBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;
}

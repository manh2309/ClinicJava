package org.example.clinicjava.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "appointments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPOINTMENT_ID")
    private Long appointmentId;

    @Column(name = "PATIENT_ID")
    private Long patientId;

    @Column(name = "DOCTOR_ID")
    private Long doctorId;

    @Column(name = "APPOINTMENT_DATE")
    private LocalDateTime appointmentDate;

    @Column(name = "PATIENT_NAME")
    private String patientName;

    @Column(name = "PATIENT_AGE")
    private Integer patientAge;

    @Column(name = "FEE")
    private BigDecimal fee;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "IS_ACTIVE")
    private Long isActive;

    @Column(name = "IS_DRAFT")
    private Long isDraft;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;

    @Column(name = "MODIFIED_BY")
    private Long modifiedBy;
}

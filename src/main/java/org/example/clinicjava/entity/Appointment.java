package org.example.clinicjava.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "SCHEDULE_TIME")
    private LocalDateTime scheduleTime;

    @Column(name = "PATIENT_NAME")
    private String patientName;

    @Column(name = "PATIENT_AGE")
    private Integer patientAge;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;
}

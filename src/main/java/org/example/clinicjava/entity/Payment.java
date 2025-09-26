package org.example.clinicjava.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "payments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Column(name = "APPOINTMENT_ID")
    private Long appointmentId;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "METHOD")
    private Long method;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "TRANSACTION_CODE")
    private String transactionCode;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;

    @Column(name = "MODIFIED_BY")
    private Long modifiedBy;
}

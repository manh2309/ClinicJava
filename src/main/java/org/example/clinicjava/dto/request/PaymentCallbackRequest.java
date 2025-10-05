package org.example.clinicjava.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCallbackRequest {
    private Long paymentId;
    private String transactionCode;
    private String status; // SUCCESS | FAILED | CANCELLED
}

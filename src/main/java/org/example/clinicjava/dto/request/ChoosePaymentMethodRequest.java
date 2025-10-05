package org.example.clinicjava.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChoosePaymentMethodRequest {
    private Long method; // 1: BANK_TRANSFER, 2: MOMO, 3: VNPAY, 4: CASH
}

package org.example.clinicjava.dto.response;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonPropertyOrder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    private Long accountId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private Long isActive;
    private Long createdBy;
    private LocalDateTime createdDate;
    private Long modifiedBy;
    private LocalDateTime modifiedDate;
    private Long roleId;

    public AccountResponse(Long accountId, String fullName, String phone, Long totalAppointments) {
        this.accountId = accountId;
        this.fullName = fullName;
        this.phone = phone;
    }
}

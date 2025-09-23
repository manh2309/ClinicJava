package org.example.clinicjava.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdateAccountRequest {
    private String fullName;
    private String email;
    private String phone;
}

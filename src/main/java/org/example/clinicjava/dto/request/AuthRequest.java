package org.example.clinicjava.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.clinicjava.constant.Constant;
import org.example.clinicjava.ultils.validator.ValidPassword;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthRequest {
    @NotBlank(message = Constant.ERROR_MESSAGE.ACCOUNT_USERNAME_REQUIRED)
    private String username;
    @ValidPassword
    private String password;
    @NotBlank(message = Constant.ERROR_MESSAGE.ACCOUNT_FULL_NAME_REQUIRED)
    private String fullName;
}

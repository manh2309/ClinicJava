package org.example.clinicjava.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.clinicjava.constant.Constant;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AccountRequest {
    @NotBlank(message = Constant.ERROR_MESSAGE.ACCOUNT_FULL_NAME_REQUIRED)
    private String username;
    @NotBlank(message = Constant.ERROR_MESSAGE.ACCOUNT_USERNAME_REQUIRED)
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = Constant.ERROR_MESSAGE.ACCOUNT_FULL_NAME_REGEX)
    private String fullName;

    @NotBlank(message = Constant.ERROR_MESSAGE.ACCOUNT_EMAIL_REQUIRED)
    @Email(message = Constant.ERROR_MESSAGE.ACCOUNT_EMAIL_FORMAT)
    private String email;
    @Pattern(
            regexp = "^(\\+84|0)[0-9]{9}$",
            message = Constant.ERROR_MESSAGE.PHONE_REGEX
    )
    private String phone;
    private Long roleId;
}

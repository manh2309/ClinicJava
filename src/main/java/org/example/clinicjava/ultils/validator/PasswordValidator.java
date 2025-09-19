package org.example.clinicjava.ultils.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.clinicjava.constant.Constant;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if(password == null || password.isBlank()) {
            buildContraintValidation(constraintValidatorContext, Constant.ERROR_MESSAGE.ACCOUNT_PASSWORD_REQUIRED);
            return false;
        }

        if (!password.matches(".*[@$!%*?&].*")) {
            buildContraintValidation(constraintValidatorContext, Constant.ERROR_MESSAGE.ACCOUNT_PASSWORD_REGEX);
            return false;
        }
        return true;
    }

    private void buildContraintValidation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}

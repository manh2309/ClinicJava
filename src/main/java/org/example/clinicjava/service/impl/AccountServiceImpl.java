package org.example.clinicjava.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clinicjava.constant.Constant;
import org.example.clinicjava.dto.request.AccountRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.entity.Account;
import org.example.clinicjava.entity.Role;
import org.example.clinicjava.exception.AppException;
import org.example.clinicjava.exception.StatusCode;
import org.example.clinicjava.repository.AccountRepository;
import org.example.clinicjava.repository.RoleRepository;
import org.example.clinicjava.service.AccountService;
import org.example.clinicjava.ultils.account.CustomUserDetails;
import org.example.clinicjava.ultils.email.EmailService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AccountServiceImpl implements AccountService {
    AccountRepository accountRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    EmailService emailService;
    @Override
    public ApiResponse<Object> createAccount(AccountRequest request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.ACCOUNT_EXISTS));
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String password = generatePassword(8);
        Role role = roleRepository.findByRoleName(Constant.ROLE_NAME.ROLE_DOCTOR)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.ROLE_EXISTS)));
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(password));
        account.setFullName(request.getFullName());
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setRoleId(role.getRoleId());
        account.setIsActive(1L);
        account.setCreatedBy(userDetails.getAccountId());
        account.setCreatedDate(LocalDateTime.now());

        accountRepository.save(account);
        String subject = "Tài khoản bác sĩ được tạo thành công";
        String body = String.format(
                Constant.MESSAGE.MAIL_BODY,
                request.getFullName(),
                request.getUsername(),
                password
        );

        emailService.sendEmail(request.getEmail(), subject, body);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result("Tài khoản bác sĩ được tạo thành công")
                .build();
    }

    private String generatePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        SecureRandom random = new SecureRandom();
        return random.ints(length, 0, chars.length())
                .mapToObj(i -> String.valueOf(chars.charAt(i)))
                .collect(Collectors.joining());
    }
}

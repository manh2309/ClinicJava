package org.example.clinicjava.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clinicjava.constant.Constant;
import org.example.clinicjava.dto.request.AccountRequest;
import org.example.clinicjava.dto.request.UpdateAccountRequest;
import org.example.clinicjava.dto.response.AccountResponse;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.dto.response.PageResponse;
import org.example.clinicjava.entity.Account;
import org.example.clinicjava.entity.Role;
import org.example.clinicjava.exception.AppException;
import org.example.clinicjava.exception.StatusCode;
import org.example.clinicjava.mapper.AccountMapper;
import org.example.clinicjava.repository.AccountRepository;
import org.example.clinicjava.repository.RoleRepository;
import org.example.clinicjava.service.AccountService;
import org.example.clinicjava.ultils.CommonUtils;
import org.example.clinicjava.ultils.account.CustomUserDetails;
import org.example.clinicjava.ultils.email.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
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
    AccountMapper accountMapper;
    @Override
    public ApiResponse<Object> createAccount(AccountRequest request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.ACCOUNT_EXISTS));
        }

        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.ACCOUNT_EMAIL_UNIQUE));
        }
        CustomUserDetails userDetails  = CommonUtils.getUserDetails();

        String password = generatePassword(8);
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.ROLE_EXISTS)));
        if(!List.of(Constant.ROLE_NAME.ROLE_ADMIN.getName(), Constant.ROLE_NAME.ROLE_ADMIN.getName()).contains(role.getRoleName())) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.ROLE_NOT_CREATED));
        }

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(password));
        account.setFullName(request.getFullName());
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setRoleId(role.getRoleId());
        account.setIsActive(1L);
        if (userDetails != null) {
            account.setCreatedBy(userDetails.getAccountId());
            account.setCreatedDate(LocalDateTime.now());
        }

        accountRepository.save(account);
        String subject = String.format(Constant.MESSAGE.DOCTOR_CREATE_ACCOUNT,
                role.getRoleName().equals(Constant.ROLE_NAME.ROLE_ADMIN.getName()) ? "admin" : "bác sĩ");
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
                .result(subject)
                .build();
    }

    @Override
    public ApiResponse<Object> searchList(Pageable pageable) {
        Page<AccountResponse> result = accountRepository.searchList(pageable).map(accountMapper::toDto);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(new PageResponse<>(result.getContent(), result.getTotalPages(), result.getTotalElements()))
                .build();
    }

    @Override
    public ApiResponse<Object> updateAccount(Long accountId, UpdateAccountRequest request) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND)));
        if (accountRepository.findByEmailAndAccountId(request.getEmail(), accountId)) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.ACCOUNT_EMAIL_DUPPLICATE));
        }
        accountMapper.updateEntityFromDto(request, existingAccount);

        CustomUserDetails userDetails = CommonUtils.getUserDetails();
        if (userDetails != null) {
            existingAccount.setModifiedBy(userDetails.getAccountId());
            existingAccount.setModifiedDate(LocalDateTime.now());
        }

        accountRepository.save(existingAccount);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(Constant.MESSAGE.UPDATE_ACCOUNT)
                .build();
    }

    @Override
    public ApiResponse<Object> deleteAccount(Long accountId) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND)));
        existingAccount.setIsActive(0L);

        CustomUserDetails userDetails  = CommonUtils.getUserDetails();
        if (userDetails != null) {
            existingAccount.setModifiedBy(userDetails.getAccountId());
            existingAccount.setModifiedDate(LocalDateTime.now());
        }

        accountRepository.save(existingAccount);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(Constant.MESSAGE.DELETE_ACCOUNT)
                .build();
    }

    @Override
    public ApiResponse<Object> getPatientsForDoctor(Pageable pageable) {
        CustomUserDetails user = CommonUtils.getUserDetails();

        Page<AccountResponse> patients =
                accountRepository.findPatientsByDoctorId(user.getAccountId(), pageable).map(accountMapper::toDto);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .result(new PageResponse<>(patients.getContent(), patients.getTotalPages(), patients.getTotalElements()))
                .build();
    }

    @Override
    public ApiResponse<Object> getPatientsForAdmin(Pageable pageable) {

        Page<AccountResponse> patients =
                accountRepository.findPatientsById(pageable).map(accountMapper::toDto);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .result(new PageResponse<>(patients.getContent(), patients.getTotalPages(), patients.getTotalElements()))
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

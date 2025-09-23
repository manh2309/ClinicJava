package org.example.clinicjava.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clinicjava.configuration.security.JwtUtil;
import org.example.clinicjava.constant.Constant;
import org.example.clinicjava.dto.request.AuthRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.entity.Account;
import org.example.clinicjava.entity.Role;
import org.example.clinicjava.entity.TokenBlackList;
import org.example.clinicjava.exception.AppException;
import org.example.clinicjava.exception.StatusCode;
import org.example.clinicjava.repository.AccountRepository;
import org.example.clinicjava.repository.RoleRepository;
import org.example.clinicjava.repository.TokenBlacklistRepository;
import org.example.clinicjava.service.AuthenticationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    AccountRepository accountRepository;
    RoleRepository roleRepository;
    TokenBlacklistRepository tokenBlacklistRepository;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;
    @Override
    public ApiResponse<Object> register(AuthRequest request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.ACCOUNT_EXISTS));
        }
        Role role = roleRepository.findByRoleName(Constant.ROLE_NAME.ROLE_PATIENT)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.ROLE_EXISTS)));
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setFullName(request.getFullName());
        account.setRoleId(role.getRoleId());
        account.setIsActive(1L);
        account.setCreatedDate(LocalDateTime.now());
        accountRepository.save(account);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result("Tạo tài khoản thành công")
                .build();
    }

    @Override
    public ApiResponse<Object> login(AuthRequest request) {
        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(StatusCode.DATA_NOT_EXISTED.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND)));
        Role role = roleRepository.findById(account.getRoleId()).orElseThrow();
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new AppException(StatusCode.DATA_NOT_EXISTED.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND));
        }
        String token = jwtUtil.generateToken(account, role.getRoleName());
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(token)
                .build();
    }

    @Override
    public ApiResponse<Object> logout(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Instant expiration = jwtUtil.getClaimFromToken(token, claims -> claims.getExpiration().toInstant());
        LocalDateTime expireAt = LocalDateTime.ofInstant(expiration, ZoneId.systemDefault());

        TokenBlackList blacklist = TokenBlackList.builder()
                .token(token)
                .expireAt(expireAt)
                .createdDate(LocalDateTime.now())
                .build();

        tokenBlacklistRepository.save(blacklist);

        return ApiResponse.builder()
                .code(200)
                .message("Đăng xuất thành công")
                .build();
    }


}

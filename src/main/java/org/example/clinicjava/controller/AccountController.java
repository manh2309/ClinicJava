package org.example.clinicjava.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clinicjava.dto.request.AccountRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.service.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/v1/account")
public class AccountController {
    AccountService accountService;

    @PostMapping("/create")
    public ApiResponse<Object> createAccount(@Valid @RequestBody AccountRequest request) {
        return accountService.createAccount(request);
    }
}

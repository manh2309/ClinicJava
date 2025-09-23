package org.example.clinicjava.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clinicjava.dto.request.AccountRequest;
import org.example.clinicjava.dto.request.UpdateAccountRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.service.AccountService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list")
    public ApiResponse<Object> listAccounts(Pageable pageable) {
        return accountService.searchList(pageable);
    }

    @PutMapping("/update/{id}")
    public ApiResponse<Object> updateAccounts(@PathVariable("id") Long accountId, @RequestBody UpdateAccountRequest request) {
        return accountService.updateAccount(accountId, request);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Object> deleteAccounts(@PathVariable("id") Long accountId) {
        return accountService.deleteAccount(accountId);
    }
}

package org.example.clinicjava.service;

import org.example.clinicjava.dto.request.AccountRequest;
import org.example.clinicjava.dto.request.UpdateAccountRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.entity.Account;
import org.springframework.data.domain.Pageable;

public interface AccountService {
    ApiResponse<Object> createAccount(AccountRequest request);
    ApiResponse<Object> searchList(Pageable pageable);
    ApiResponse<Object> updateAccount(Long account, UpdateAccountRequest request);
    ApiResponse<Object> deleteAccount(Long accountId);
}

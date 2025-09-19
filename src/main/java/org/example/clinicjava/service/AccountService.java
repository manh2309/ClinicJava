package org.example.clinicjava.service;

import org.example.clinicjava.dto.request.AccountRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.entity.Account;

public interface AccountService {
    ApiResponse<Object> createAccount(AccountRequest request);
}

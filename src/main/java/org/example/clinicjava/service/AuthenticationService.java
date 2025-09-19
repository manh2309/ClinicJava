package org.example.clinicjava.service;

import org.example.clinicjava.dto.request.AuthRequest;
import org.example.clinicjava.dto.response.ApiResponse;

public interface AuthenticationService {
    ApiResponse<Object> register(AuthRequest request);
    ApiResponse<Object> login(AuthRequest request);
}

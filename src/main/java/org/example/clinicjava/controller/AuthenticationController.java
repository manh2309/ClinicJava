package org.example.clinicjava.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clinicjava.dto.request.AuthRequest;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.dto.response.AuthResponse;
import org.example.clinicjava.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/v1/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthRequest authRequest) {
        authenticationService.register(authRequest);
        return ResponseEntity.ok("Register successful!");
    }

    @PostMapping("/login")
    public ApiResponse<Object> login(@RequestBody AuthRequest request) {
        return authenticationService.login(request);
    }
}

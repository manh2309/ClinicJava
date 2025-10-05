package org.example.clinicjava.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.repository.NotificationRepository;
import org.example.clinicjava.service.NotificationService;
import org.example.clinicjava.ultils.CommonUtils;
import org.example.clinicjava.ultils.account.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/v1/notfication")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/my")
    public ApiResponse<Object> getMyNotifications(Pageable pageable) {
        CustomUserDetails user = CommonUtils.getUserDetails();
        return notificationService.findByAccountIdOrderByCreatedDateDesc(user.getAccountId(),pageable);
    }

    @PutMapping("/read/{id}")
    public ApiResponse<Object> markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }

    @PutMapping("/read-all")
    public ApiResponse<Object> markAllAsRead() {
        return notificationService.markAllAsRead();
    }

}

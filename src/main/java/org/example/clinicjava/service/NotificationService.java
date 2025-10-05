package org.example.clinicjava.service;

import org.example.clinicjava.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    void sendNotification(Long accountId, String title, String message, String type);
    void sendNotificationToMany(List<Long> accountIds, String title, String message, String type);
    ApiResponse<Object> findByAccountIdOrderByCreatedDateDesc(Long accountId, Pageable pageable);
    ApiResponse<Object> markAllAsRead();
    ApiResponse<Object> markAsRead(Long accountId);

}

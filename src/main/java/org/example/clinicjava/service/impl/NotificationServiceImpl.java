package org.example.clinicjava.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.clinicjava.constant.Constant;
import org.example.clinicjava.dto.response.ApiResponse;
import org.example.clinicjava.dto.response.NotificationResponse;
import org.example.clinicjava.dto.response.PageResponse;
import org.example.clinicjava.entity.Account;
import org.example.clinicjava.entity.Notification;
import org.example.clinicjava.exception.AppException;
import org.example.clinicjava.exception.StatusCode;
import org.example.clinicjava.mapper.NotificationMapper;
import org.example.clinicjava.repository.AccountRepository;
import org.example.clinicjava.repository.NotificationRepository;
import org.example.clinicjava.service.NotificationService;
import org.example.clinicjava.ultils.CommonUtils;
import org.example.clinicjava.ultils.account.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationMapper notificationMapper;
    private final AccountRepository accountRepository;

    @Override
    public void sendNotification(Long accountId, String title, String message, String type) {
        Notification notification = Notification.builder()
                .accountId(accountId)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .sendDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        // gửi realtime qua WebSocket
        messagingTemplate.convertAndSend("/topic/notifications/" + accountId, notification);
    }

    @Override
    public void sendNotificationToMany(List<Long> accountIds, String title, String message, String type) {
            for(Long accountId : accountIds) {
                sendNotification(accountId, title, message, type);
            }
    }

    @Override
    public ApiResponse<Object> findByAccountIdOrderByCreatedDateDesc(Long accountId, Pageable pageable) {
        Page<NotificationResponse> resultNotification = notificationRepository.findByAccountIdOrderByCreatedDateDesc(accountId, pageable).map(notificationMapper::toDto);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(new PageResponse<>(resultNotification.getContent(), resultNotification.getTotalPages(), resultNotification.getTotalElements()))
                .build();
    }

    @Override
    public ApiResponse<Object> markAllAsRead() {
        CustomUserDetails userDetails = CommonUtils.getUserDetails();
        Long accountId = userDetails.getAccountId();

        notificationRepository.markAllAsRead(accountId);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(Constant.MESSAGE.NOTIFICATION_IS_ALL_READ)
                .build();
    }

    @Override
    public ApiResponse<Object> markAsRead(Long accountId) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(Constant.ERROR_MESSAGE.DATA_NOT_FOUND)));
        notificationRepository.markAsRead(existingAccount.getAccountId());

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(Constant.MESSAGE.NOTIFICATION_IS_READ)
                .build();
    }
}

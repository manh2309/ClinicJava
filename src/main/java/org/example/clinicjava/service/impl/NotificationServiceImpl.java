package org.example.clinicjava.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.clinicjava.entity.Notification;
import org.example.clinicjava.repository.NotificationRepository;
import org.example.clinicjava.service.NotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

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
}

package org.example.clinicjava.service;

import java.util.List;

public interface NotificationService {
    void sendNotification(Long accountId, String title, String message, String type);
    void sendNotificationToMany(List<Long> accountIds, String title, String message, String type);
}

package org.example.clinicjava.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long notificationId;
    private Long accountId;
    private String title;
    private String message;
    private String type;
    private Boolean isRead;
    private LocalDateTime sendDate;
    private LocalDateTime createdDate;
}

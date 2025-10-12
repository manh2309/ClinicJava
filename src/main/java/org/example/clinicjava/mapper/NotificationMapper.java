package org.example.clinicjava.mapper;

import org.example.clinicjava.dto.response.NotificationResponse;
import org.example.clinicjava.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {})
public interface NotificationMapper extends EntityMapper<NotificationResponse, Notification> {

    NotificationResponse toDto(Notification notification);
}

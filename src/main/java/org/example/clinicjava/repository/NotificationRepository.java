package org.example.clinicjava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.example.clinicjava.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByAccountIdAndIsReadFalse(Long accountId);

    Page<Notification> findByAccountIdOrderByCreatedDateDesc(Long accountId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.notificationId = :id")
    void markAsRead(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.accountId = :accountId")
    void markAllAsRead(@Param("accountId") Long accountId);
}

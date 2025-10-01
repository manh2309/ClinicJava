package org.example.clinicjava.repository;

import org.example.clinicjava.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(value = "SELECT p FROM Payment p WHERE p.appointmentId = :appointmentId ")
    Optional<Payment> findByAppointmentId(@Param("appointmentId") Long appointmentId);
}

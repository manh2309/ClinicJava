package org.example.clinicjava.repository;

import org.example.clinicjava.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("""
                SELECT a 
                FROM Appointment a
                WHERE 
                    (:roleName = 'ROLE_PATIENT' AND a.patientId = :accountId)
                    OR (:roleName = 'ROLE_DOCTOR' AND a.doctorId = :accountId)
                    OR (:roleName = 'ROLE_ADMIN')
            """)
    Page<Appointment> searchAppointment(@Param("accountId") Long accountId,
                                        @Param("roleName") String roleName,
                                        Pageable pageable);
}

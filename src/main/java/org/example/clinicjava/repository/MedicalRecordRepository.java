package org.example.clinicjava.repository;

import org.example.clinicjava.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientId(Long patientId);

    @Query(value = " SELECT m FROM MedicalRecord m WHERE m.isActive = 1 " +
                   " AND m.patientId = :patientId ")
    Page<MedicalRecord> searchMedicalRecordByPatientId(@Param("patientId") Long patientId, Pageable pageable);

    @Query(value = " SELECT m FROM MedicalRecord m WHERE m.isActive = 1 " +
            " AND m.doctorId = :doctorId ")
    Page<MedicalRecord> searchMedicalRecordByDoctorId(@Param("doctorId") Long doctorId, Pageable pageable);
}

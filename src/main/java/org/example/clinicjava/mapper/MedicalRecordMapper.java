package org.example.clinicjava.mapper;

import org.example.clinicjava.dto.request.CreateMedicalRecordRequest;
import org.example.clinicjava.dto.response.MedicalRecordResponse;
import org.example.clinicjava.entity.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        uses = {})
public interface MedicalRecordMapper extends EntityMapper<MedicalRecordResponse, MedicalRecord>{

    @Mapping(target = "medicalRecordId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    MedicalRecord toEntity(CreateMedicalRecordRequest createMedicalRecordRequest);
}

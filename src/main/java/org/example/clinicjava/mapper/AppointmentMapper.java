package org.example.clinicjava.mapper;

import org.example.clinicjava.dto.request.CreateAppointmentRequest;
import org.example.clinicjava.dto.response.AppointmentResponse;
import org.example.clinicjava.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        uses = {})
public interface AppointmentMapper extends EntityMapper<AppointmentResponse, Appointment> {

    @Mapping(target = "appointmentId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Appointment toEntity(CreateAppointmentRequest dto);

    AppointmentResponse toDto(Appointment entity);
}

package org.example.clinicjava.mapper;

import org.example.clinicjava.dto.request.CreateAppointmentRequest;
import org.example.clinicjava.dto.response.AppointmentResponse;
import org.example.clinicjava.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        uses = {})
public interface AppointmentMapper extends EntityMapper<AppointmentResponse, Appointment> {
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    Appointment toEntity(CreateAppointmentRequest dto);

    AppointmentResponse toDto(Appointment entity);
}

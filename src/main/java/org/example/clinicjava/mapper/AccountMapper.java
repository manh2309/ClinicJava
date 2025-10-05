package org.example.clinicjava.mapper;

import org.example.clinicjava.dto.request.UpdateAccountRequest;
import org.example.clinicjava.dto.response.AccountResponse;
import org.example.clinicjava.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        uses = {})
public interface AccountMapper extends EntityMapper<AccountResponse, Account>{

    AccountResponse toDto(Account entity);

    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEntityFromDto(UpdateAccountRequest dto, @MappingTarget Account entity);

}

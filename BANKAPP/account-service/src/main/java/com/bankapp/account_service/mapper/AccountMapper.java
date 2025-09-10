package com.bankapp.account_service.mapper;

import com.bankapp.account_service.dto.AccountCreateDTO;
import com.bankapp.account_service.dto.AccountResponseDTO;
import com.bankapp.account_service.model.Account;
import com.bankapp.account_service.model.AccountStatus;
import com.bankapp.account_service.model.AccountType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    // DTO -> Entity (string -> enum)
    @Mapping(target = "accountType", expression = "java(mapToAccountType(dto.getAccountType()))")
    @Mapping(target = "status", expression = "java(mapToAccountStatus(dto.getStatus()))")
    Account toEntity(AccountCreateDTO dto);

    // Entity -> DTO (enum -> string)
    @Mapping(target = "accountType", expression = "java(mapToString(account.getAccountType()))")
    @Mapping(target = "status", expression = "java(mapToString(account.getStatus()))")
    AccountResponseDTO toResponseDTO(Account account);

    // ----------------- Helpers -----------------
    default AccountType mapToAccountType(String type) {
        return type != null ? AccountType.valueOf(type.toUpperCase()) : null;
    }

    default AccountStatus mapToAccountStatus(String status) {
        return status != null ? AccountStatus.valueOf(status.toUpperCase()) : null;
    }

    default String mapToString(Enum<?> e) {
        return e != null ? e.name() : null;
    }
}

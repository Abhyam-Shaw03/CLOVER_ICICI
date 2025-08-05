package com.bankapp.admin_service.mapper;

import com.bankapp.admin_service.dto.AdminCreateDTO;
import com.bankapp.admin_service.dto.AdminResponseDTO;
import com.bankapp.admin_service.dto.AdminUpdateDTO;
import com.bankapp.admin_service.model.Admin;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    Admin toEntity(AdminCreateDTO dto);

    AdminResponseDTO toResponseDTO(Admin admin);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdminFromDTO(AdminUpdateDTO dto, @MappingTarget Admin admin);

}


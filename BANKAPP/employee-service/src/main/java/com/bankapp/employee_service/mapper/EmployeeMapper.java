package com.bankapp.employee_service.mapper;

import com.bankapp.employee_service.dto.EmployeeCreateDTO;
import com.bankapp.employee_service.dto.EmployeeResponseDTO;
import com.bankapp.employee_service.dto.EmployeeUpdateDTO;
import com.bankapp.employee_service.model.Employee;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    Employee toEntity(EmployeeCreateDTO dto);

    EmployeeResponseDTO toResponseDTO(Employee employee);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeFromDTO(EmployeeUpdateDTO dto, @MappingTarget Employee employee);

}


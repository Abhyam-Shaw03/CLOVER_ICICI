package com.bankapp.customer_service.mapper;

import com.bankapp.customer_service.dto.CustomerCreateDTO;
import com.bankapp.customer_service.dto.CustomerResponseDTO;
import com.bankapp.customer_service.dto.CustomerUpdateDTO;
import com.bankapp.customer_service.model.Customer;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toEntity(CustomerCreateDTO dto);

    CustomerResponseDTO toResponseDTO(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomerFromDTO(CustomerUpdateDTO dto, @MappingTarget Customer customer);

}

/*
        NOTES:-

    ✅ @Mapper(componentModel = "spring")
Tells MapStruct to generate an implementation of this interface and register it as a Spring Bean.

Now you can @Autowired or @Inject this mapper in your services.

✅ Customer toEntity(CustomerCreateDTO dto);
Maps CustomerCreateDTO to Customer entity.

Used during customer creation when saving data into the database.

MapStruct automatically matches fields with the same names.

✅ CustomerResponseDTO toResponseDTO(Customer customer);
Maps Customer entity to CustomerResponseDTO.

Used when returning data to the frontend to hide sensitive or internal fields.

Only exposes the necessary data.

✅ @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
This configures the next method to:

Ignore null fields from the DTO while mapping.

Useful during partial updates — if a field is null in the CustomerUpdateDTO, it will not overwrite the corresponding field in the entity.

✅ void updateCustomerFromDTO(CustomerUpdateDTO dto, @MappingTarget Customer customer);
Updates an existing Customer object with non-null values from CustomerUpdateDTO.

The @MappingTarget annotation means MapStruct will modify the existing Customer object rather than create a new one.

Often used in PUT or PATCH operations to update only selected fields.
*/

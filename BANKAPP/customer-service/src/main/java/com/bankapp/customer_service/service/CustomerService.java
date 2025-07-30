package com.bankapp.customer_service.service;

import com.bankapp.customer_service.dto.CustomerCreateDTO;
import com.bankapp.customer_service.dto.CustomerResponseDTO;
import com.bankapp.customer_service.dto.CustomerUpdateDTO;
import com.bankapp.customer_service.dto.UserRegisterDTO;
import com.bankapp.customer_service.feign.UserServiceClient;
import com.bankapp.customer_service.mapper.CustomerMapper;
import com.bankapp.customer_service.model.Customer;
import com.bankapp.customer_service.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CustomerService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private UserServiceClient userServiceClient;

    // CREATE CUSTOMER
    public CustomerResponseDTO createCustomer(CustomerCreateDTO createDTO) {
        Customer customer = customerMapper.toEntity(createDTO);
        customer.setRegistrationDate(LocalDate.now());
        customer.setActive(true);

        // GENERATING CUSTOM CUSTOMER-ID
        String customerId = generateCustomerId(customer);
        customer.setCustomerId(customerId);

        // ENCODING & SETTING PASSWORD
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        Customer saved = customerRepo.save(customer);

        // Create UserRegisterDTO and call user-service
        UserRegisterDTO userDto = new UserRegisterDTO();
        userDto.setUserId(customerId);
        userDto.setPassword(customer.getPassword());
        userDto.setRole("CUSTOMER");

        userServiceClient.registerUser(userDto); // sends data to user-service

        return customerMapper.toResponseDTO(saved);
    }

    // UPDATE CUSTOMER
    public CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO updateDTO) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customerMapper.updateCustomerFromDTO(updateDTO, customer);
        Customer updated = customerRepo.save(customer);
        return customerMapper.toResponseDTO(updated);
    }

    private String generateCustomerId(Customer customer) {
        String firstTwoFirstName = customer.getFirstName().substring(0, 2).toUpperCase();
        String firstTwoLastName = customer.getLastName().substring(0, 2).toUpperCase();
        String birthYear = String.valueOf(customer.getDateOfBirth().getYear());
        String lastTwoBirthYear = birthYear.substring(birthYear.length() - 2);
        String lastTwoAadhar = customer.getAadharNumber().substring(customer.getAadharNumber().length() - 2);
        String lastTwoPhone = customer.getPhoneNumber().substring(customer.getPhoneNumber().length() - 2);

        return "C" + firstTwoFirstName + firstTwoLastName + lastTwoBirthYear + lastTwoAadhar + lastTwoPhone;
    }

}

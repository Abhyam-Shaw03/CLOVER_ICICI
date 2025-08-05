package com.bankapp.employee_service.service;

import com.bankapp.employee_service.dto.EmployeeCreateDTO;
import com.bankapp.employee_service.dto.EmployeeResponseDTO;
import com.bankapp.employee_service.dto.UserRegisterDTO;
import com.bankapp.employee_service.feign.UserServiceClient;
import com.bankapp.employee_service.mapper.EmployeeMapper;
import com.bankapp.employee_service.model.Employee;
import com.bankapp.employee_service.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmployeeService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    public EmployeeResponseDTO createEmployee(EmployeeCreateDTO employeeCreateDTO) {
        Employee employee = employeeMapper.toEntity(employeeCreateDTO);
        employee.setActive(true);
        employee.setJoinDate(LocalDate.now());

        // GENERATING CUSTOM EMPLOYEE-ID
        String employeeId = generateEmployeeId(employee);
        employee.setEmployeeId(employeeId);

        // ENCODING & SETTING PASSWORD
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        Employee saved = employeeRepository.save(employee);

        // Create UserRegisterDTO and call user-service
        UserRegisterDTO userDto = new UserRegisterDTO();
        userDto.setUserId(employeeId);
        userDto.setPassword(employee.getPassword());
        userDto.setRole("EMPLOYEE");

        userServiceClient.createUser(userDto); // sends data to user-service

        return employeeMapper.toResponseDTO(saved);

    }

    // GENERATING OF EMPLOYEE ID
    private String generateEmployeeId(Employee employee) {
        String firstTwoFirstName = employee.getFirstName().substring(0, 2).toUpperCase();
        String firstTwoLastName = employee.getLastName().substring(0, 2).toUpperCase();
        String birthYear = String.valueOf(employee.getDateOfBirth().getYear());
        String lastTwoBirthYear = birthYear.substring(birthYear.length() - 2);
        String lastTwoAadhar = employee.getAadharNumber().substring(employee.getAadharNumber().length() - 2);
        String lastTwoPhone = employee.getPhoneNumber().substring(employee.getPhoneNumber().length() - 2);

        return "E" + firstTwoFirstName + firstTwoLastName + lastTwoBirthYear + lastTwoAadhar + lastTwoPhone;
    }
}

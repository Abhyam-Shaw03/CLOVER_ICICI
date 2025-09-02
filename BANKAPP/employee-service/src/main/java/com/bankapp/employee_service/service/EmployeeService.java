package com.bankapp.employee_service.service;

import com.bankapp.employee_service.dto.*;
import com.bankapp.employee_service.feign.UserServiceClient;
import com.bankapp.employee_service.mapper.EmployeeMapper;
import com.bankapp.employee_service.model.Employee;
import com.bankapp.employee_service.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Creates a new employee record and registers a corresponding user in the user-service.
     * It generates a unique employee ID, hashes the password, and saves the employee
     * to the database. It then uses a Feign client to create a user record.
     *
     * @param employeeCreateDTO The DTO containing employee creation details.
     * @return An EmployeeResponseDTO of the newly created employee.
     */

    public EmployeeResponseDTO createEmployee(EmployeeCreateDTO employeeCreateDTO) {
        Employee employee = employeeMapper.toEntity(employeeCreateDTO);
        employee.setActive(true);
        employee.setJoinDate(LocalDate.now());

        String employeeId = generateEmployeeId(employee);
        employee.setEmployeeId(employeeId);

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        Employee saved = employeeRepository.save(employee);

        UserRegisterDTO userDto = new UserRegisterDTO();
        userDto.setUserId(employeeId);
        userDto.setPassword(employee.getPassword());
        userDto.setRole("EMPLOYEE");

        userServiceClient.createUser(userDto);

        return employeeMapper.toResponseDTO(saved);
    }

    /**
     * Retrieves a list of all employee records from the database.
     * This method is typically restricted to administrators.
     *
     * @return A list of EmployeeResponseDTOs for all employees.
     */
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Fetches the details of a single employee by their user ID.
     *
     * @param userId The unique ID of the employee.
     * @return An EmployeeResponseDTO for the specified employee.
     * @throws EntityNotFoundException if the employee is not found.
     */
    public EmployeeResponseDTO getEmployeeDetails(String userId) {
        Employee employee = employeeRepository.findByEmployeeId(userId);
        if (employee == null) {
            throw new EntityNotFoundException("Employee not found with id: " + userId);
        }
        return employeeMapper.toResponseDTO(employee);
    }

    /**
     * Updates an existing employee's details based on their user ID.
     *
     * @param userId The unique ID of the employee.
     * @param employeeUpdateDTO The DTO containing the updated employee information.
     * @return An EmployeeResponseDTO of the updated employee.
     * @throws EntityNotFoundException if the employee is not found.
     */
    @Transactional
    public EmployeeResponseDTO updateEmployeeDetails(String userId, EmployeeUpdateDTO employeeUpdateDTO) {
        Employee existingEmployee = employeeRepository.findByEmployeeId(userId);
        if (existingEmployee == null) {
            throw new EntityNotFoundException("Employee not found with id: " + userId);
        }

        employeeMapper.updateEmployeeFromDTO(employeeUpdateDTO, existingEmployee);

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return employeeMapper.toResponseDTO(updatedEmployee);
    }

    /**
     * Updates an employee's password based on their user ID.
     * The new password is first encoded before being saved.
     *
     * @param userId The unique ID of the employee.
     * @param passwordDto The DTO containing the old and new passwords.
     * @return An EmployeeResponseDTO of the updated employee.
     * @throws EntityNotFoundException if the employee is not found.
     * @throws SecurityException if the old password does not match.
     */

    public EmployeeResponseDTO updateEmployeePassword(String userId, EmployeePasswordUpdateDTO passwordDto) {
        Employee existingEmployee = employeeRepository.findByEmployeeId(userId);
        if (existingEmployee == null) {
            throw new EntityNotFoundException("Employee not found with id: " + userId);
        }

        // Validate the old password
        if (!passwordEncoder.matches(passwordDto.getOldPassword(), existingEmployee.getPassword())) {
            throw new SecurityException("Old password does not match.");
        }

        // Hash the new password
        String encodedPassword = passwordEncoder.encode(passwordDto.getNewPassword());
        existingEmployee.setPassword(encodedPassword);

        NewPasswordDTO newPasswordDTO = new NewPasswordDTO(encodedPassword); // PUTTING THE NEW PASSWORD IN A DTO

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        // Update the password in the user-service via Feign client
        userServiceClient.updateUserPassword(userId, newPasswordDTO);
        return employeeMapper.toResponseDTO(updatedEmployee);
    }

    /**
     * Deletes an employee record from the database based on their user ID.
     *
     * @param userId The unique ID of the employee to delete.
     * @throws EntityNotFoundException if the employee is not found.
     */

    public void deleteEmployee(String userId) {
        Employee employee = employeeRepository.findByEmployeeId(userId);
        if (employee == null) {
            throw new EntityNotFoundException("Employee not found with id: " + userId);
        }

        userServiceClient.deleteUser(userId);
        employeeRepository.delete(employee);
    }

    /**
     * Generates a unique employee ID based on the employee's personal details.
     *
     * @param employee The employee entity.
     * @return A unique employee ID string.
     */
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

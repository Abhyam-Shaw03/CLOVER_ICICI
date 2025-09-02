package com.bankapp.employee_service.controller;

import com.bankapp.employee_service.dto.EmployeeCreateDTO;
import com.bankapp.employee_service.dto.EmployeePasswordUpdateDTO;
import com.bankapp.employee_service.dto.EmployeeResponseDTO;
import com.bankapp.employee_service.dto.EmployeeUpdateDTO;
import com.bankapp.employee_service.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/signup")
    public EmployeeResponseDTO createEmployee(@RequestBody EmployeeCreateDTO employeeDto){
        return employeeService.createEmployee(employeeDto);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
    @GetMapping("/profile/{userId}")
    public EmployeeResponseDTO getEmployeeDetails(@PathVariable String userId) {
        return employeeService.getEmployeeDetails(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PreAuthorize("#userId == authentication.principal")
    @PutMapping("/updateProfile/{userId}")
    public EmployeeResponseDTO updateEmployeeDetails(@PathVariable String userId, @RequestBody EmployeeUpdateDTO employeeDto) {
        return employeeService.updateEmployeeDetails(userId, employeeDto);
    }

    @PreAuthorize("#userId == authentication.principal")
    @PutMapping("/updatePassword/{userId}")
    public EmployeeResponseDTO updateEmployeePassword(@PathVariable String userId, @RequestBody EmployeePasswordUpdateDTO passwordDto) {
        return employeeService.updateEmployeePassword(userId, passwordDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public void deleteEmployee(@PathVariable String userId) {
        employeeService.deleteEmployee(userId);
    }
}

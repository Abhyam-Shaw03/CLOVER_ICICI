package com.bankapp.employee_service.controller;

import com.bankapp.employee_service.dto.EmployeeCreateDTO;
import com.bankapp.employee_service.dto.EmployeeResponseDTO;
import com.bankapp.employee_service.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/signup")
    public EmployeeResponseDTO createEmployee(@RequestBody EmployeeCreateDTO employeeDto){
        return employeeService.createEmployee(employeeDto);
    }
}

package com.bankapp.customer_service.controller;

import com.bankapp.customer_service.dto.*;
import com.bankapp.customer_service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @PostMapping("/signup")
    public CustomerResponseDTO createCustomer(@RequestBody CustomerCreateDTO customerDto){
        return customerService.createCustomer(customerDto);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @GetMapping("/all")
    public List<CustomerResponseDTO> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE') or #customerId == authentication.principal")
    @GetMapping("/profile/{customerId}")
    public CustomerResponseDTO getCustomerDetails(@PathVariable String customerId){
        return customerService.getCustomerDetails(customerId);
    }

    @PreAuthorize("#customerId == authentication.principal")
    @PutMapping("/updateProfile/{customerId}")
    public CustomerResponseDTO updateCustomerDetails(@PathVariable String customerId, @RequestBody CustomerUpdateDTO customerUpdateDTO){
        return customerService.updateCustomerDetails(customerId, customerUpdateDTO);
    }

    @PreAuthorize("#customerId == authentication.principal")
    @PutMapping("/updatePassword/{customerId}")
    public String updateCustomerPassword(@PathVariable String customerId, @RequestBody CustomerPasswordUpdateDTO customerPasswordUpdateDTO){
        return customerService.updateCustomerPassword(customerId, customerPasswordUpdateDTO);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public void deleteCustomer(@PathVariable String userId) {
        customerService.deleteCustomer(userId);
    }
}

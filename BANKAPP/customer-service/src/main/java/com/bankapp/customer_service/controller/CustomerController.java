package com.bankapp.customer_service.controller;

import com.bankapp.customer_service.dto.*;
import com.bankapp.customer_service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/signup")
    public CustomerResponseDTO createCustomer(@RequestBody CustomerCreateDTO customerDto){
        return customerService.createCustomer(customerDto);
    }

    @GetMapping("/all")
    public List<CustomerResponseDTO> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/profile/{customerId}")
    public CustomerResponseDTO getCustomerDetails(@PathVariable String customerId){
        return customerService.getCustomerDetails(customerId);
    }

    @PutMapping("/updateProfile/{customerId}")
    public CustomerResponseDTO updateCustomerDetails(@PathVariable String customerId, @RequestBody CustomerUpdateDTO customerUpdateDTO){
        return customerService.updateCustomerDetails(customerId, customerUpdateDTO);
    }

    @PutMapping("/updatePassword/{customerId}")
    public String updateCustomerPassword(@PathVariable String customerId, @RequestBody CustomerPasswordUpdateDTO customerPasswordUpdateDTO){
        return customerService.updateCustomerPassword(customerId, customerPasswordUpdateDTO);
    }

    @DeleteMapping("/delete/{userId}")
    public void deleteEmployee(@PathVariable String userId) {
        customerService.deleteCustomer(userId);
    }
}

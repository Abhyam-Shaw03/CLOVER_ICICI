package com.bankapp.customer_service.controller;

import com.bankapp.customer_service.dto.CustomerCreateDTO;
import com.bankapp.customer_service.dto.CustomerResponseDTO;
import com.bankapp.customer_service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/signup")
    public CustomerResponseDTO createCustomer(@RequestBody CustomerCreateDTO customerDto){
        return customerService.createCustomer(customerDto);
    }
}

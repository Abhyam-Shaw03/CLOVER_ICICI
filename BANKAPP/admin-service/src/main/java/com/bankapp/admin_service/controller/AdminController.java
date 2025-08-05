package com.bankapp.admin_service.controller;

import com.bankapp.admin_service.dto.AdminCreateDTO;
import com.bankapp.admin_service.dto.AdminResponseDTO;
import com.bankapp.admin_service.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/signup")
    public AdminResponseDTO createAdmin(@RequestBody AdminCreateDTO adminCreateDTO){
        return adminService.createAdmin(adminCreateDTO);
    }
}

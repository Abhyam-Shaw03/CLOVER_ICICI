package com.bankapp.admin_service.controller;

import com.bankapp.admin_service.dto.AdminCreateDTO;
import com.bankapp.admin_service.dto.AdminPasswordUpdateDTO;
import com.bankapp.admin_service.dto.AdminResponseDTO;
import com.bankapp.admin_service.dto.AdminUpdateDTO;
import com.bankapp.admin_service.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/signup")
    public AdminResponseDTO createAdmin(@RequestBody AdminCreateDTO adminCreateDTO){
        return adminService.createAdmin(adminCreateDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
    @GetMapping("/profile/{userId}")
    public AdminResponseDTO getAdminDetails(@PathVariable String userId) {
        return adminService.getAdminDetails(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<AdminResponseDTO> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @PreAuthorize("#userId == authentication.principal")
    @PutMapping("/updateProfile/{userId}")
    public AdminResponseDTO updateAdminDetails(@PathVariable String userId, @RequestBody AdminUpdateDTO adminUpdateDTO) {
        return adminService.updateAdminDetails(userId, adminUpdateDTO);
    }

    @PreAuthorize("#userId == authentication.principal")
    @PutMapping("/updatePassword/{userId}")
    public String updateAdminPassword(@PathVariable String userId, @RequestBody AdminPasswordUpdateDTO passwordDto) {
        return adminService.updateAdminPassword(userId, passwordDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public void deleteAdmin(@PathVariable String userId) {
        adminService.deleteAdmin(userId);
    }
}
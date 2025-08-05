package com.bankapp.admin_service.service;

import com.bankapp.admin_service.dto.AdminCreateDTO;
import com.bankapp.admin_service.dto.AdminResponseDTO;
import com.bankapp.admin_service.dto.UserRegisterDTO;
import com.bankapp.admin_service.feign.UserServiceClient;
import com.bankapp.admin_service.mapper.AdminMapper;
import com.bankapp.admin_service.model.Admin;
import com.bankapp.admin_service.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private AdminMapper adminMapper;

    public AdminResponseDTO createAdmin(AdminCreateDTO adminCreateDTO) {
        Admin admin = adminMapper.toEntity(adminCreateDTO);
        admin.setJoinDate(LocalDate.now());
        admin.setActive(true);

        // GENERATING CUSTOM ADMIN ID
        String adminId = generateAdminId(admin);
        admin.setAdminId(adminId);

        // SETTING ENCODED PASSWORD
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        Admin saved = adminRepository.save(admin); // ADMIN SAVED TO ADMIN-DB

        // Create UserRegisterDTO and call user-service
        UserRegisterDTO userDto = new UserRegisterDTO();
        userDto.setUserId(adminId);
        userDto.setPassword(admin.getPassword());
        userDto.setRole("ADMIN");

        userServiceClient.createUser(userDto); // sends data to user-service

        return adminMapper.toResponseDTO(saved);
    }

    // GENERATING OF ADMIN ID
    private String generateAdminId(Admin admin) {
        String firstTwoFirstName = admin.getFirstName().substring(0, 2).toUpperCase();
        String firstTwoLastName = admin.getLastName().substring(0, 2).toUpperCase();
        String birthYear = String.valueOf(admin.getDateOfBirth().getYear());
        String lastTwoBirthYear = birthYear.substring(birthYear.length() - 2);
        String lastTwoAadhar = admin.getAadharNumber().substring(admin.getAadharNumber().length() - 2);
        String lastTwoPhone = admin.getPhoneNumber().substring(admin.getPhoneNumber().length() - 2);

        return "A" + firstTwoFirstName + firstTwoLastName + lastTwoBirthYear + lastTwoAadhar + lastTwoPhone;
    }
}

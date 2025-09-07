package com.bankapp.admin_service.service;

import com.bankapp.admin_service.dto.*;
import com.bankapp.admin_service.feign.UserServiceClient;
import com.bankapp.admin_service.mapper.AdminMapper;
import com.bankapp.admin_service.model.Admin;
import com.bankapp.admin_service.repository.AdminRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Creates a new admin record and registers a corresponding user in the user-service.
     * It generates a unique admin ID, hashes the password, and saves the admin
     * to the database. It then uses a Feign client to create a user record.
     *
     * @param adminCreateDTO The DTO containing admin creation details.
     * @return An AdminResponseDTO of the newly created admin.
     */

    public AdminResponseDTO createAdmin(AdminCreateDTO adminCreateDTO) {
        Admin admin = adminMapper.toEntity(adminCreateDTO);
        admin.setJoinDate(LocalDate.now());
        admin.setActive(true);

        // GENERATING CUSTOM ADMIN ID
        String adminId = generateAdminId(admin);
        admin.setAdminId(adminId);

        // SETTING ENCODED PASSWORD
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        Admin saved = adminRepository.save(admin);

        // Create UserRegisterDTO and call user-service
        UserRegisterDTO userDto = new UserRegisterDTO();
        userDto.setUserId(adminId);
        userDto.setPassword(admin.getPassword());
        userDto.setRole("ADMIN");

        userServiceClient.createUser(userDto);

        return adminMapper.toResponseDTO(saved);
    }

    /**
     * Retrieves a list of all admin records from the database.
     *
     * @return A list of AdminResponseDTOs for all admins.
     */
    public List<AdminResponseDTO> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(adminMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Fetches the details of a single admin by their user ID.
     *
     * @param userId The unique ID of the admin.
     * @return An AdminResponseDTO for the specified admin.
     * @throws EntityNotFoundException if the admin is not found.
     */
    public AdminResponseDTO getAdminDetails(String userId) {
        Admin admin = adminRepository.findByAdminId(userId);
        if (admin == null) {
            throw new EntityNotFoundException("Admin not found with id: " + userId);
        }
        return adminMapper.toResponseDTO(admin);
    }

    /**
     * Updates an existing admin's details based on their user ID.
     *
     * @param userId The unique ID of the admin.
     * @param adminUpdateDTO The DTO containing the updated admin information.
     * @return An AdminResponseDTO of the updated admin.
     * @throws EntityNotFoundException if the admin is not found.
     */

    public AdminResponseDTO updateAdminDetails(String userId, AdminUpdateDTO adminUpdateDTO) {
        Admin existingAdmin = adminRepository.findByAdminId(userId);
        if (existingAdmin == null) {
            throw new EntityNotFoundException("Admin not found with id: " + userId);
        }

        adminMapper.updateAdminFromDTO(adminUpdateDTO, existingAdmin);

        Admin updatedAdmin = adminRepository.save(existingAdmin);
        return adminMapper.toResponseDTO(updatedAdmin);
    }

    /**
     * Updates an admin's password based on their user ID.
     * The new password is first encoded before being saved.
     *
     * @param userId The unique ID of the admin.
     * @param passwordDto The DTO containing the old and new passwords.
     * @return A String of the updated admin.
     * @throws EntityNotFoundException if the admin is not found.
     * @throws SecurityException if the old password does not match.
     */

    public String updateAdminPassword(String userId, AdminPasswordUpdateDTO passwordDto) {
        try {
            Admin existingAdmin = adminRepository.findByAdminId(userId);
            if (existingAdmin == null) {
                return "Error: Admin not found with ID: " + userId;
            }

            // Validate the old password
            if (!passwordEncoder.matches(passwordDto.getOldPassword(), existingAdmin.getPassword())) {
                return "Error: Old password does not match.";
            }

            // Hash the new password
            String encodedPassword = passwordEncoder.encode(passwordDto.getNewPassword());
            existingAdmin.setPassword(encodedPassword);

            NewPasswordDTO newPasswordDTO = new NewPasswordDTO(encodedPassword);

            // Update the password in the user-service via Feign client
            userServiceClient.updateUserPassword(userId, newPasswordDTO);

            adminRepository.save(existingAdmin);

            return "Password updated successfully for admin with ID: " + userId;

        } catch (Exception e) {
            // Catch any other exceptions and return a generic error message
            return "An error occurred while updating the password: " + e.getMessage();
        }
    }

    /**
     * Deletes an admin record from the database based on their user ID.
     *
     * @param userId The unique ID of the admin to delete.
     * @throws EntityNotFoundException if the admin is not found.
     */

    public void deleteAdmin(String userId) {
        Admin admin = adminRepository.findByAdminId(userId);
        if (admin == null) {
            throw new EntityNotFoundException("Admin not found with id: " + userId);
        }

        userServiceClient.deleteUser(userId);
        adminRepository.delete(admin);
    }

    /**
     * Generates a unique admin ID based on the admin's personal details.
     *
     * @param admin The admin entity.
     * @return A unique admin ID string.
     */
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

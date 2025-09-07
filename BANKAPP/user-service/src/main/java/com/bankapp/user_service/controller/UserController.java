package com.bankapp.user_service.controller;

import com.bankapp.user_service.dto.NewPasswordDTO;
import com.bankapp.user_service.dto.UserLoginDTO;
import com.bankapp.user_service.dto.UserRegisterDTO;
import com.bankapp.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping("/register")
    public void createUser(@Valid @RequestBody UserRegisterDTO dto) {
        userService.createUser(dto);
//        return ResponseEntity.ok("User created successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String token = userService.verifyUser(userLoginDTO);

            if (token == null || token.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password");
            }

            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed due to server error: " + e.getMessage());
        }
    }

    @PreAuthorize("#userId == authentication.name")
    @PutMapping("/updatePassword/{userId}")
    public void updateUserPassword(@PathVariable String userId, @Valid @RequestBody NewPasswordDTO newPasswordDTO) {
        userService.updateUserPassword(userId, newPasswordDTO.getNewPassword());
//        return ResponseEntity.ok("Password updated successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @DeleteMapping("/deleteUser/{userId}")
    public void deleteUser(@PathVariable String userId){
        try {
            userService.deleteUser(userId);
        } catch (RuntimeException e) {
        }
    }

}

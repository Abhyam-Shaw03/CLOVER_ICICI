package com.bankapp.user_service.controller;

import com.bankapp.user_service.dto.UserLoginDTO;
import com.bankapp.user_service.dto.UserRegisterDTO;
import com.bankapp.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRegisterDTO dto) {
        userService.createUser(dto);
        return ResponseEntity.ok("User created successfully");
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO){
//        String token = userService.verifyUser(userLoginDTO);
//        return ResponseEntity.ok(token);
//    }

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


}

package com.bankapp.user_service.service;

import com.bankapp.user_service.dto.UserLoginDTO;
import com.bankapp.user_service.dto.UserRegisterDTO;
import com.bankapp.user_service.model.Role;
import com.bankapp.user_service.model.User;
import com.bankapp.user_service.model.UserPrincipal;
import com.bankapp.user_service.repository.UserRepository;
import com.bankapp.user_service.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    public void createUser(UserRegisterDTO dto) {
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setPassword(dto.getPassword()); // already encoded

        try {
            user.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role value: " + dto.getRole());
        }
        userRepository.save(user);
    }

    public String verifyUser(UserLoginDTO userlogin) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userlogin.getUserId(), userlogin.getPassword())); // here we are verifying only the username and password entered.
        if(authentication.isAuthenticated()){
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String role = userPrincipal.getAuthorities().iterator().next().getAuthority(); // e.g., "ROLE_CUSTOMER"
            return jwtService.generateToken(userlogin.getUserId(), role); // ✅ Pass role to JWT
        }else{
            throw new RuntimeException("Invalid login credentials");
        }
    }
}

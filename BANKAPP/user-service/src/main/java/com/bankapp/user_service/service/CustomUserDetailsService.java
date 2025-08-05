//package com.bankapp.user_service.service;
//
//import com.bankapp.user_service.dto.UserLoginResponseDTO;
//import com.bankapp.user_service.model.User;
//import com.bankapp.user_service.model.UserPrincipal;
//import com.bankapp.user_service.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        User user = userRepository.findByUserId(userId);
//        if(user == null){
//            System.out.println("User Not found..!!");
//            throw new UsernameNotFoundException("User Not Found..!");
//        }
//
//        return new UserPrincipal(user);
//    }
//}

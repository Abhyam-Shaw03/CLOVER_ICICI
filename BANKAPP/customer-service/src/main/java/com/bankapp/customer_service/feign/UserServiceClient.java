package com.bankapp.customer_service.feign;

import com.bankapp.customer_service.dto.NewPasswordDTO;
import com.bankapp.customer_service.dto.UserRegisterDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", configuration = FeignClientInterceptor.class) // later remove URL for Eureka
public interface UserServiceClient {

    @PostMapping("/user/register")
    void createUser(@RequestBody UserRegisterDTO userDto);

    @PutMapping("/user/updatePassword/{userId}")
    void updateUserPassword(@PathVariable String userId, @RequestBody NewPasswordDTO dto);

    @DeleteMapping("/user/deleteUser/{userId}")
    void deleteUser(@PathVariable String userId);
}

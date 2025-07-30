package com.bankapp.customer_service.feign;

import com.bankapp.customer_service.dto.UserRegisterDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://localhost:8083") // later remove URL for Eureka
public interface UserServiceClient {

    @PostMapping("/users/register/user")
    void registerUser(@RequestBody UserRegisterDTO userDto);
}

package com.bankapp.customer_service.feign;

import com.bankapp.customer_service.dto.AccountCreateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service", configuration = FeignClientInterceptor.class) // later remove URL for Eureka
public interface AccountServiceClient {

    @PostMapping("/accounts/create")
    void createAccount(@RequestBody AccountCreateDTO dto);
}

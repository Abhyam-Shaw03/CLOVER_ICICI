package com.bankapp.employee_service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients(basePackages = "com.bankapp.employee_service.feign")
@EnableDiscoveryClient
public class EmployeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}

	// Optional LoadBalancer bean for Feign + OkHttp
//	@Bean
//	public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(
//			ObjectProvider<ServiceInstanceListSupplier> supplierProvider) {
//		// Replace "employee-service" with the serviceId you want to load balance
//		return new RandomLoadBalancer(supplierProvider, "employee-service");
//	}

//	@Bean
//	public ReactorLoadBalancer<ServiceInstance> randomUserServiceLoadBalancer(
//			ObjectProvider<ServiceInstanceListSupplier> supplierProvider) {
//		return new RandomLoadBalancer(supplierProvider, "user-service");
//	}

}

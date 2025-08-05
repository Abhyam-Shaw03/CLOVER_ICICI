package com.bankapp.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApigatewayApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(ApigatewayApplication.class);

	public static void main(String[] args) {
		logger.info("Starting ApiGatewayApplication...");
		SpringApplication.run(ApigatewayApplication.class, args);
		logger.info("ApiGatewayApplication started successfully.");
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Application is fully up and running!");
	}
}

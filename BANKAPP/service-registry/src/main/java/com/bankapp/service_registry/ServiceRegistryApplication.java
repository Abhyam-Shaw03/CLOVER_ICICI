package com.bankapp.service_registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(ServiceRegistryApplication.class);

	public static void main(String[] args) {
		logger.info("Starting ServiceRegistryApplication...");
		SpringApplication.run(ServiceRegistryApplication.class, args);
		logger.info("ServiceRegistryApplication started successfully.");
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Application is fully up and running!");
	}
}

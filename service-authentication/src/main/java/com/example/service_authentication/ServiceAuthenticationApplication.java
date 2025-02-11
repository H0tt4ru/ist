package com.example.service_authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients(basePackages = "com.example.service_authentication.client")
@SpringBootApplication
@ComponentScan(basePackages = "com.example")
@EntityScan(basePackages = "com.example.base_domain.entities")
@EnableJpaRepositories(basePackages = "com.example.base_domain.repositories")
public class ServiceAuthenticationApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceAuthenticationApplication.class, args);
	}
}

package com.example.service_wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
@EntityScan(basePackages = "com.example.base_domain.entities")
@EnableJpaRepositories(basePackages = "com.example.base_domain.repositories")
public class ServiceWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceWalletApplication.class, args);
	}

}

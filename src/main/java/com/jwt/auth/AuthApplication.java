package com.jwt.auth;

import com.jwt.auth.dto.RegistrationRequest;
import com.jwt.auth.entity.enums.Role;
import com.jwt.auth.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}


	@Bean
	public CommandLineRunner run(AuthenticationService authenticationService) {
		return args -> {
			var administrator = RegistrationRequest.builder()
					.firstName("Administrator")
					.lastName("Administrator")
					.email("administrator@gmail.com")
					.password("password")
					.role(Role.ADMIN)
					.build();

			System.out.println("Administrator Token: " + authenticationService.register(administrator).getAccessToken());

			var manager = RegistrationRequest.builder()
					.firstName("Manager")
					.lastName("Manager")
					.email("manager@gmail.com")
					.password("password")
					.role(Role.MANAGER)
					.build();

			System.out.println("Manager Token: " + authenticationService.register(manager).getAccessToken());

			var user = RegistrationRequest.builder()
					.firstName("User")
					.lastName("User")
					.email("user@gmail.com")
					.password("password")
					.role(Role.USER)
					.build();

			System.out.println("User Token: " + authenticationService.register(user).getAccessToken());

		};
	}
}

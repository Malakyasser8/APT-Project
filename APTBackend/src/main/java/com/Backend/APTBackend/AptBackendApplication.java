package com.Backend.APTBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@RestController
public class AptBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AptBackendApplication.class, args);
	}

	@GetMapping("/")
	public String get() {
		return "K";
	}
}

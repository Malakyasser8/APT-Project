package com.Backend.APTBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AptBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AptBackendApplication.class, args);
	}
	@GetMapping("/")
	public String get(){
		return "K";
	}
}

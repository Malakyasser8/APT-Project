package com.apt.APTProjectBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication( exclude = { SecurityAutoConfiguration.class } )
//@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@RestController
public class AptProjectBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AptProjectBackendApplication.class, args);
	}

	@GetMapping("/")
    public String getAllUsers(){
        return "jaja";
    }
}

package com.Backend.APTBackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Backend.APTBackend.models.User;
import com.Backend.APTBackend.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<List<User>>(userService.allUsers(), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody User userBody) {
        if (userService.existsByUsername(userBody.getUsername())) {
            return new ResponseEntity<String>("Username already exists", HttpStatus.CONFLICT);
        }
        if (userService.existsByEmail(userBody.getEmail())) {
            return new ResponseEntity<String>("Email already exists", HttpStatus.CONFLICT);
        }
        User user = userService.createUser(userBody.getUsername(), userBody.getEmail(), userBody.getPassword());
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User userBody) {
        if (userBody.getUsername() == null || userBody.getPassword() == null) {
            return new ResponseEntity<String>("Missing username or password",
                    HttpStatus.CONFLICT);
        }
        User user = userService.loginUser(userBody.getUsername(), userBody.getPassword());
        if (user == null) {
            return new ResponseEntity<String>("Incorrect username or password",
                    HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<String>("User logged in successfully", HttpStatus.OK);
        }
    }

}

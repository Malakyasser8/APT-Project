package com.Backend.APTBackend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Backend.APTBackend.models.User;
import com.Backend.APTBackend.security.JwtToken;
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
            return new ResponseEntity<String>("Username already exists", HttpStatus.BAD_REQUEST);
        }
        if (userService.existsByEmail(userBody.getEmail())) {
            return new ResponseEntity<String>("Email already exists", HttpStatus.BAD_REQUEST);
        }
        User user = userService.createUser(userBody.getUsername(), userBody.getEmail(), userBody.getPassword());
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User userBody) {
        if (userBody.getUsername() == null || userBody.getPassword() == null) {
            return new ResponseEntity<String>("Missing username or password",
                    HttpStatus.BAD_REQUEST);
        }
        User user = userService.loginUser(userBody.getUsername(), userBody.getPassword());
        if (user == null) {
            return new ResponseEntity<String>("Incorrect username or password",
                    HttpStatus.BAD_REQUEST);
        } else {

            String token = JwtToken.generateToken(user.get_id());

            userService.addToTokens(user, token);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);

            // System.out.println(user);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body("User logged in successfully");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {

        if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() < 9) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing");
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

        User user = userService.verifyUserToken(authorizationHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");
        }

        boolean isLoggedOut = userService.logoutUser(user, token);
        // System.out.println(isLoggedOut);
        if (isLoggedOut)
            return ResponseEntity.ok()
                    .body("User logged out successfully");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User has already logged out");

    }

}

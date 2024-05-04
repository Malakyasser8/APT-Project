package com.Backend.APTBackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Backend.APTBackend.models.User;
import com.Backend.APTBackend.repositories.UserRepository;
import com.Backend.APTBackend.security.JwtToken;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder encoder = new PasswordEncoder() {
        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            return passwordEncoder.matches(rawPassword, encodedPassword);
        }

        @Override
        public String encode(CharSequence rawPassword) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            return passwordEncoder.encode(rawPassword);
        }
    };

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Boolean addToTokens(User user, String token) {
        user.addToTokens(token);
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User verifyUserToken(String authorizationHeader) {
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

        String userId = JwtToken.getIdFromToken(token);
        if (userId == null) {
            return null;
        }
        Optional<User> optionalUser = userRepository.findBy_id(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user;
        }
        return null;
    }

    public User createUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        String hashedPassword = encoder.encode(password);
        user.setPassword(hashedPassword);

        // Save the user
        try {
            User savedUser = userRepository.save(user);
            return savedUser;
        } catch (Exception e) {
            return null;
        }
    }

    public User loginUser(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // System.out.println();
            if (encoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public boolean logoutUser(User user, String token) {
        try {
            boolean isRemoved = user.removeFromTokens(token);
            if (!isRemoved)
                return false;
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

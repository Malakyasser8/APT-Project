package com.Backend.APTBackend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Backend.APTBackend.models.File;
import com.Backend.APTBackend.models.User;
import com.Backend.APTBackend.security.JwtToken;
import com.Backend.APTBackend.services.UserService;
import com.Backend.APTBackend.services.FileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @PostMapping("/create")
    public ResponseEntity<?> createFile(@Valid @RequestBody File filename,@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() < 9) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        User user = userService.verifyUserToken(authorizationHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");
        }

        File file = fileService.createFile(user,filename.getFilename());
        return new ResponseEntity<File>(file, HttpStatus.OK);
    }

    @PostMapping("/rename/{filename}")
    public ResponseEntity<?> renameFile(@PathVariable String filename,@Valid @RequestBody File newFilename,@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() < 9) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        User user = userService.verifyUserToken(authorizationHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");
        }
        System.out.println(newFilename);
        Boolean status= fileService.renameFile(user,filename,newFilename.getFilename());
        
        return new ResponseEntity<Boolean>(status, HttpStatus.OK);
    }
    @GetMapping("/open/{filename}")
    public ResponseEntity<?> openFile(@PathVariable String filename,@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() < 9) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        User user = userService.verifyUserToken(authorizationHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");
        }

       File file = fileService.openFile(user,filename);
        return new ResponseEntity<File>(file, HttpStatus.OK);
    }
    @PostMapping("/delete")
    public ResponseEntity<?> getFile(@Valid @RequestBody File filename,@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() < 9) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        User user = userService.verifyUserToken(authorizationHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");
        }

       boolean deleted = fileService.deleteFile(user,filename.getFilename());
        return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
    }

    @GetMapping("/owned")
    public ResponseEntity<?> getOwnerFiles(@RequestParam String id,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() < 9) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        User user = userService.verifyUserToken(authorizationHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");
        }
       
        return new ResponseEntity<List<File>>(fileService.getFilesUserOwned(id , pageNum , pageSize), HttpStatus.OK);
    }

    @GetMapping("/shared")
    public ResponseEntity<?> getSharedFiles(@RequestParam String id,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() < 9) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        User user = userService.verifyUserToken(authorizationHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");
        }
       
        return new ResponseEntity<List<File>>(fileService.getUserSharedFiles(id , pageNum , pageSize), HttpStatus.OK);
    }
}

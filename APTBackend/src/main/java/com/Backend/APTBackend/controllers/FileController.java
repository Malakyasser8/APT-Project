package com.Backend.APTBackend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.Backend.APTBackend.models.File;
import com.Backend.APTBackend.models.User;
import com.Backend.APTBackend.services.UserService;
import com.Backend.APTBackend.services.FileService;

import jakarta.validation.Valid;
import lombok.Data;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @PostMapping("/create")
    public ResponseEntity<?> createFile(@Valid @RequestBody File filename,
            @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() < 9) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        User user = userService.verifyUserToken(authorizationHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");
        }

        File file = fileService.createFile(user, filename.getFilename());
        return new ResponseEntity<File>(file, HttpStatus.OK);
    }

    @PostMapping("/rename/{fileId}")
    public ResponseEntity<?> renameFile(@PathVariable String fileId, @Valid @RequestBody String newFilename,
            @RequestHeader("Authorization") String authorizationHeader)
            throws JsonMappingException, JsonProcessingException {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() < 9) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        User user = userService.verifyUserToken(authorizationHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");
        }
        System.out.println("ana fi rename");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(newFilename);
        String filename = jsonNode.get("filename").asText();
        System.out.println(filename);
        Boolean status = fileService.renameFile(user, fileId, filename);

        return new ResponseEntity<Boolean>(status, HttpStatus.OK);
    }

    @GetMapping("/open/{fileId}")
    public ResponseEntity<?> openFile(@PathVariable String fileId,
            @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() < 9) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        User user = userService.verifyUserToken(authorizationHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");
        }

        File file = fileService.openFile(user, fileId);
        return new ResponseEntity<File>(file, HttpStatus.OK);
    }

    @PostMapping("/delete/{fileId}")
    public ResponseEntity<?> getFile(@PathVariable String fileId,
            @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() < 9) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        User user = userService.verifyUserToken(authorizationHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");
        }

        boolean deleted = fileService.deleteFile(user, fileId);
        return new ResponseEntity<Boolean>(deleted, HttpStatus.OK);
    }

    // Rawan: Access Control, Share File
    @PostMapping("/sharetoEditor/{fileId}")
    public ResponseEntity<?> shareFiletoEditor(@PathVariable String fileId, @RequestBody User userToShareWith,
            @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() < 9) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        User user = userService.verifyUserToken(authorizationHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");
        }

        boolean shared = fileService.shareFile(fileId, userToShareWith, "editor");
        return new ResponseEntity<Boolean>(shared, HttpStatus.OK);
    }

    @PostMapping("/sharetoViewer/{fileId}")
    public ResponseEntity<?> shareFiletoViewer(@PathVariable String fileId, @RequestBody User userToShareWith,
            @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() < 9) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        User user = userService.verifyUserToken(authorizationHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");
        }

        boolean shared = fileService.shareFile(fileId, userToShareWith, "viewer");
        return new ResponseEntity<Boolean>(shared, HttpStatus.OK);
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

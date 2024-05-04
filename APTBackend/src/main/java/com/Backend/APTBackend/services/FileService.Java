package com.Backend.APTBackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Backend.APTBackend.models.File;
import com.Backend.APTBackend.repositories.FileRepository;

@Service
public class FileService {

    @Autowired
    private FileRepository FileRepository;
};
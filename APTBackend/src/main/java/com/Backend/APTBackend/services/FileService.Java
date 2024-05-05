package com.Backend.APTBackend.services;


import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Backend.APTBackend.models.File;
import com.Backend.APTBackend.models.User;
import com.Backend.APTBackend.repositories.FileRepository;

@Service
public class FileService {

    @Autowired
    private FileRepository FileRepository;

    public File createFile(User user,String filename) {
        File file = new File();
        file.setFilename(filename);
        file.setOwner(user);
        // java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
        // file.setCreated_at(sqlDate);
        try {
            File savedFile = FileRepository.save(file);
            return savedFile;
        } catch (Exception e) {
            return null;
        }
    }
    public boolean renameFile(User user, String filename ,String newFilename){
        List<File> userFiles = FileRepository.findByOwnerId(user.get_id());
        System.out.println("rmmmmmmm");
        System.out.println(userFiles);
        File fileToRename = null;
        for (File file : userFiles) {
            if (file.getFilename().equals(filename)) {
                fileToRename = file;
                break;
            }
        }
        fileToRename.setFilename(newFilename);
        try {
            FileRepository.save(fileToRename);
            return true; // Return true if renaming is successful
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return false; // Return false if an error occurs
        }
    }
    public File openFile(User user,String filename)
    {
        List<File> userFiles = FileRepository.findByOwnerId(user.get_id());
        System.out.println("rmmmmmmm");
        System.out.println(userFiles);
        File openFile = null;
        for (File file : userFiles) {
            if (file.getFilename().equals(filename)) {
                openFile = file;
                break;
            }
        }
        return openFile; 
    }
    public Boolean deleteFile(User user,String filename)
    {
        List<File> userFiles = FileRepository.findByOwnerId(user.get_id());
        System.out.println("rmmmmmmm");
        System.out.println(userFiles);
        for (File file : userFiles) {
            if (file.getFilename().equals(filename)) {
                FileRepository.delete(file);
                System.out.println("File deleted successfully: " + filename);
                return true;
            }
        }
        
        return false;
    }
         
    

};






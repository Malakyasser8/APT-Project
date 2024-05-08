package com.Backend.APTBackend.services;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Backend.APTBackend.models.User;
import com.Backend.APTBackend.models.File;
import com.Backend.APTBackend.repositories.FileRepository;

@Service
public class FileService {
    @Autowired
    private FileRepository FileRepository;

    public File createFile(User user, String filename) {
        List<File> userFiles = FileRepository.findByOwnerId(user.get_id());
        File openFile = null;
        for (File file : userFiles) {
            if (file.getFilename().equals(filename)) {
                openFile = file;
                break;
            }
        }
        File file = new File();
        if (openFile != null) {
            file.setFilename(filename + '1');
        } else {
            file.setFilename(filename);
        }
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

    public boolean renameFile(User user, String fileId, String newFilename) {
        File foundFile = FileRepository.findByFileId(fileId);
        if (foundFile == null) {
            return false;
        } else {
            if (isOwner(fileId, user) || isEditor(fileId, user) || isViewer(fileId,
                    user)) {
                foundFile.setFilename(newFilename);
                try {
                    FileRepository.save(foundFile);
                    return true; // Return true if renaming is successful
                } catch (Exception e) {
                    e.printStackTrace(); // Handle exception
                    return false; // Return false if an error occurs
                }
            }
            return false;
        }
        // List<File> userFiles = FileRepository.findByOwnerId(user.get_id());
        // File fileToRename = null;
        // for (File file : userFiles) {
        // if (file.getFilename().equals(filename)) {
        // fileToRename = file;
        // break;
        // }
        // }
        // fileToRename.setFilename(newFilename);
        // try {
        // FileRepository.save(fileToRename);
        // return true; // Return true if renaming is successful
        // } catch (Exception e) {
        // e.printStackTrace(); // Handle exception
        // return false; // Return false if an error occurs
        // }
    }

    public File openFile(User user, String fileId) {
        File foundFile = FileRepository.findByFileId(fileId);
        if (foundFile == null) {
            return null;
        } else {
            if (isOwner(fileId, user) || isEditor(fileId, user) || isViewer(fileId, user)) {
                return foundFile;
            }
            return null;
        }

        // List<File> userFiles = FileRepository.findByOwnerId(user.get_id());
        // System.out.println("rmmmmmmm");
        // System.out.println(userFiles);
        // File openFile = null;
        // for (File file : userFiles) {
        // if (file.getFilename().equals(filename)) {
        // openFile = file;
        // break;
        // }
        // }
        // return openFile;
    }

    public String deleteFile(User user, String fileId) {
        File foundFile = FileRepository.findByFileId(fileId);
        if (foundFile == null) {
            System.out.println("fileid");
            System.out.println(fileId);
            return "file not found";
        } else {
            if (isOwner(fileId, user)) {
                FileRepository.delete(foundFile);
                return "deleted";
            }
            return "owner";
        }
        // List<File> userFiles = FileRepository.findByOwnerId(user.get_id());
        // System.out.println("rmmmmmmm");
        // System.out.println(userFiles);
        // for (File file : userFiles) {
        // if (file.getFilename().equals(filename)) {
        // FileRepository.delete(file);
        // System.out.println("File deleted successfully: " + filename);
        // return true;
        // }
        // }

        // return false;
    }

    public List<File> getFilesUserOwned(String owner , int pageNum , int  pageSize){
        List<File> owned = FileRepository.findByOwnerId(owner);
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, owned.size());
        return owned.subList(fromIndex, toIndex);
    }

    public List<File> getUserSharedFiles(String editor , int pageNum , int  pageSize){
        List<File> editorFiles = FileRepository.findByEditorsId(editor);
        List<File> viewer = FileRepository.findByViewersId(editor);
        int fromIndex = (pageNum - 1) * pageSize;

        editorFiles.addAll(viewer);
        int toIndex = Math.min(fromIndex + pageSize, editorFiles.size());
        return editorFiles.subList(fromIndex, toIndex);

    }



    // Rawan: Access Control
    public boolean isOwner(String fileId, User user) {
        File foundFile = FileRepository.findByFileId(fileId);
        if (foundFile == null) {
            return false;
        } else {
            return foundFile.getOwner().get_id().equals(user.get_id());
        }
    }

    public boolean isEditor(String fileId, User user) {
        File foundFile = FileRepository.findByFileId(fileId);
        if (foundFile == null) {
            return false;
        } else {
            for (User editorId : foundFile.getEditors()) {
                if (editorId.get_id().equals(user.get_id())) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isViewer(String fileId, User user) {
        File foundFile = FileRepository.findByFileId(fileId);
        if (foundFile == null) {
            return false;
        } else {
            for (User viewerId : foundFile.getViewers()) {
                if (viewerId.get_id().equals(user.get_id())) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean shareFile(String fileId, User user, String role) {
        File foundFile = FileRepository.findByFileId(fileId);
        if (foundFile == null) {
            return false;
        } else {
            if (role.equals("editor")) {
                boolean isEditorIdPresent = false;
                for (User editor : foundFile.getEditors()) {
                    if (editor.get_id().equals(user.get_id())) {
                        isEditorIdPresent = true;
                        break;
                    }
                }
                if (!isEditorIdPresent) {
                    foundFile.getViewers().removeIf(viewer -> viewer.get_id().equals(user.get_id()));
                    foundFile.getEditors().add(user);
                } else {
                    return false;
                }
            } else if (role.equals("viewer")) {
                boolean isViewerIdPresent = false;
                for (User viewer : foundFile.getViewers()) {
                    if (viewer.get_id().equals(user.get_id())) {
                        isViewerIdPresent = true;
                        break;
                    }
                }
                if (!isViewerIdPresent) {
                    foundFile.getEditors().removeIf(editor -> editor.get_id().equals(user.get_id()));
                    foundFile.getViewers().add(user);
                } else {
                    return false;
                }
            } else {
                return false;
            }
            FileRepository.save(foundFile);
            return true;
        }
    }

}

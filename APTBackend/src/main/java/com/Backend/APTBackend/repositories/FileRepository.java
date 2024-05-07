package com.Backend.APTBackend.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.Backend.APTBackend.models.File;

@Repository
public interface FileRepository extends MongoRepository<File, ObjectId> {

    Optional<File> findByFilename(String filename);

    Boolean existsByFilename(String filename);

    @Query("{'owner._id': ?0}")
    List<File> findByOwnerId(String ownerId);

    @Query("{'editors._id': ?0}")
    List<File> findByEditorsId(String userId);
    @Query("{'viewers._id': ?0}")
    List<File> findByViewersId(String userId);
    
    
    // Rawan: Access Control, find file by id
    @Query("{'_id': ?0}")
    File findByFileId(String fileId);
}

package com.Backend.APTBackend.repositories;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Backend.APTBackend.models.File;




@Repository
public interface FileRepository extends MongoRepository<File, ObjectId> {

    Optional<File> findByFilename(String filename);
    Boolean existsByFilename(String filename);

    
    
}

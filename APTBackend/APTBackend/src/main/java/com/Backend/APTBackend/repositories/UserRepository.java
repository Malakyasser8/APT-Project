package com.Backend.APTBackend.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Backend.APTBackend.models.User;

@Repository
public interface UserRepository extends MongoRepository<User,ObjectId>{

    
}
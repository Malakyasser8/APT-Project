package com.apt.APTProjectBackend.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apt.APTProjectBackend.models.User;

@Repository
public interface UserRepository extends MongoRepository<User,ObjectId>{

    
}


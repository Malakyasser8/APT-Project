package com.Backend.APTBackend.repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Backend.APTBackend.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findBy_id(String id);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
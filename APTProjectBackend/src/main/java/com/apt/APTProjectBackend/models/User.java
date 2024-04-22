package com.apt.APTProjectBackend.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User {
	private ObjectId _id;
	private String username;
	private String password;
	private String email;
	

}
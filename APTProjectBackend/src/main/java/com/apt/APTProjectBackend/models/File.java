package com.apt.APTProjectBackend.models;

import java.sql.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
	@Id
	private ObjectId _id;
	private String filename;
	private Date created_at;
	private String content;
	@DocumentReference
	private List<User> owners_ids;
	@DocumentReference
	private List<User> editors_ids;
	@DocumentReference
	private List<User> viewers_ids;

	
}
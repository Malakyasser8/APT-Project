package com.Backend.APTBackend.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.validation.annotation.Validated;

import com.mongodb.lang.NonNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "file")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
@Setter
public class File {
	@Id
	private String _id;

	@NotBlank
	@NotEmpty
	@NonNull
	@Size(min = 1)
	private String filename;

	//private Date created_at;

	private String content="";

	@DocumentReference
	private User owner;

	@DocumentReference
	private List<User> editors;

	@DocumentReference
	private List<User> viewers;

	

}

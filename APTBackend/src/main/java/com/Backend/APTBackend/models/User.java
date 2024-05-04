package com.Backend.APTBackend.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "user")
@Data
@Validated
@AllArgsConstructor
@Getter
@Setter
public class User {

	@Id
	private String _id;

	@NotBlank
	@NotNull
	@NotEmpty
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Size(min = 8)
	@NotNull
	@NotEmpty
	private String password;

	@NotBlank
	@NotNull
	@NotEmpty
	@Size(max = 50)
	@Email
	private String email;

	private List<String> tokens;

	public User() {
		this.tokens = new ArrayList<>();
	}

	public boolean addToTokens(String token) {
		return tokens.add(token);
	}

	public boolean removeFromTokens(String token) {
		return tokens.remove(token);
	}
}

package com.sumit.electronic.store.dtos;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.sumit.electronic.store.entities.Role;
import com.sumit.electronic.store.validate.ImageNameValid;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

	private String userId;
	
	@Size(min =3, max= 15, message="Invalid Name !!")
	@NotBlank
	@ApiModelProperty(value = "user_name", name="username", required=true, notes=" user name of new user")
	private String name;
	
//	@Email(message = "Invalid User EMail")
	@Pattern(regexp = "^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$")
	@NotBlank
	private String email;
	
	@NotBlank(message = "Invalid User Password")
	private String password;
	
	@ImageNameValid
	private String imageName;
	
	@Size(min = 4, max = 6, message = "Invalid user gender")
	private String gender;
	
	@NotBlank(message = "Write about something !!")
	private String about;
	
	
	private Set<RoleDto> roles = new HashSet<>();
}

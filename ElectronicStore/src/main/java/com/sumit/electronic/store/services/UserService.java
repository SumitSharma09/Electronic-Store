package com.sumit.electronic.store.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.sumit.electronic.store.dtos.PageableResponse;
import com.sumit.electronic.store.dtos.UserDto;
import com.sumit.electronic.store.entities.User;

public interface UserService {
	
	//create the user
	
	UserDto createUser(UserDto userDto);
	
	//update the user
	
	UserDto updateUser(UserDto userDto, String userId);
	
	//get a single user 
	UserDto getUser(String userId);
	
	//get a single user from email id
	UserDto getUserEmail(String email);
	
	//get a all user
//	List<UserDto> getAllUser(int pageNumber, int pageSize , String sortBy, String sortDir);
	
	PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize , String sortBy, String sortDir);
	
	
	//delete the user
	void deleteUser(String userId) throws IOException;
	
	//get a single user from search
	
	List<UserDto> getBySearch(String name);

	Optional<User> findUserByEmailOptional(String email);
	

}

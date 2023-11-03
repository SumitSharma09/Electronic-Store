package com.sumit.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sumit.electronic.store.dtos.ApiResponseMessage;
import com.sumit.electronic.store.dtos.ImageResponse;
import com.sumit.electronic.store.dtos.PageableResponse;
import com.sumit.electronic.store.dtos.UserDto;
import com.sumit.electronic.store.services.FileService;
import com.sumit.electronic.store.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.*;

@RestController
@RequestMapping("/users")
@Api(value= "UserController", description= "REST APIs related to perform users operations !!")
public class UserController {
	
	@Autowired
	private UserService userservice;
	
	@Autowired
	private FileService fileservice;
	
	@Value("${user.profile.image.path}")
	String imageUploadPath;
	
	private Logger logger = LoggerFactory.getLogger(UserService.class);
	
	
	// create
	@PostMapping
	@ApiOperation(value= "create a users")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "success | OK"),
			@ApiResponse(code = 401, message= "not authorized !!"),
			@ApiResponse(code= 201, message = "new user created !!")
	})
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		UserDto userDto1 = userservice.createUser(userDto);
		
		return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
	}
	
	// update 
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser( @PathVariable("userId") String userId, @Valid @RequestBody UserDto userDto){
		UserDto updatedUserDto = userservice.updateUser(userDto, userId);
		
		return new ResponseEntity<>(updatedUserDto,HttpStatus.OK);
		
	}
	// delete a user
	@DeleteMapping("{userId}") 
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) throws IOException{
		userservice.deleteUser(userId);
		ApiResponseMessage message = ApiResponseMessage.builder().message("User has been deleted Successfully")
				.success(true).status(HttpStatus.OK).build();
		return new ResponseEntity<>(message ,HttpStatus.OK);
	}
	
	
//	@GetMapping
//	public ResponseEntity<List<UserDto>> getAllUsers(
//			@RequestParam(value= "pageNumber", defaultValue = "0", required = false) int pageNumber,
//			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
//			@RequestParam(value= "sortBy", defaultValue = "name", required = false) String sortBy,
//			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
//			
//			
//			){
//		return new ResponseEntity<>(userservice.getAllUser(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
//	}
	
	
	@GetMapping
	@ApiOperation(value= "get all users", response = ResponseEntity.class,tags= {"user-controller", "user apis"})
	public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
			@RequestParam(value= "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value= "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			
			
			){
		return new ResponseEntity<>(userservice.getAllUser(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable String userId){
		return new ResponseEntity<>(userservice.getUser(userId), HttpStatus.OK);
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getemail(@PathVariable String userEmail){
		return new ResponseEntity<>(userservice.getUserEmail(userEmail), HttpStatus.OK);
	}
	
	@GetMapping("/name/{keywords}")
	public ResponseEntity<List<UserDto>> getBysearch(@PathVariable String keywords){
		return new ResponseEntity<>(userservice.getBySearch(keywords), HttpStatus.OK);
	}
	
	//upload user image
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException{
		String imageName = fileservice.uploadFile(image, imageUploadPath);
		UserDto user = userservice.getUser(userId);
		user.setImageName(imageName);
		UserDto userDto = userservice.updateUser(user, userId);
		ImageResponse imageResponse = ImageResponse.builder().ImageName(imageName).success(true).status(HttpStatus.CREATED).build();
		return new ResponseEntity<> (imageResponse, HttpStatus.CREATED);
			
	}
	// serve a image
	@GetMapping("/image/{userId}")
	public void serveImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
		UserDto user = userservice.getUser(userId);
		logger.info("user image name is: {}", user.getImageName());
		InputStream resource = fileservice.getResources(imageUploadPath, user.getImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
		
	}
	
	

}

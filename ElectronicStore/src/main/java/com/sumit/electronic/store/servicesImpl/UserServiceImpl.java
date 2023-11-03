package com.sumit.electronic.store.servicesImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.*;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.sumit.electronic.store.dtos.PageableResponse;
import com.sumit.electronic.store.dtos.UserDto;
import com.sumit.electronic.store.entities.Role;
import com.sumit.electronic.store.entities.User;
import com.sumit.electronic.store.exception.ResourceNotFoundException;
import com.sumit.electronic.store.helper.helper;
import com.sumit.electronic.store.repo.RoleRepository;
import com.sumit.electronic.store.repo.UserRepository;
import com.sumit.electronic.store.services.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Value("${user.profile.image.path}")
	public String imagePath;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Value("${normal.role.id}")
	private String normalRoleId;
	
	@Autowired
	private RoleRepository roleRepository;

	private Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Override
	public UserDto createUser(UserDto userDto) {
		// TODO Auto-generated method stub
		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);
		userDto.setPassword(encoder.encode(userDto.getPassword()));
		
		// dto-> entity
		User user = dtoToEntity(userDto);
		
		Role role = roleRepository.findById(normalRoleId).get();
		user.getRoles().add(role);

		User savedUser = userRepository.save(user);
		
		//entiy -> dto
		UserDto newDto = entityToDto(savedUser);
		
		return newDto;
	}


	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		// TODO Auto-generated method stub
		
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("This userId is not found"));
		user.setName(userDto.getName());
		user.setPassword(userDto.getPassword());
		user.setGender(userDto.getGender());
		user.setAbout(userDto.getAbout());
		user.setEmail(userDto.getEmail());
		user.setImageName(userDto.getImageName());
		
		User updatedUser = userRepository.save(user);
		UserDto userDto1 = entityToDto(updatedUser);
		return userDto1;
	}

	@Override
	public UserDto getUser(String userId) {
		// TODO Auto-generated method stub
		User user2 = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("this is not found"));
		return entityToDto(user2);
	}

	@Override
	public UserDto getUserEmail(String email) {
		// TODO Auto-generated method stub
		User user  = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("this is not found a email"));
		return entityToDto(user);
	}

//	@Override
//	public List<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
//		// TODO Auto-generated method stub
//		
//	//	Sort sort = Sort.by(sortBy);
//		
//		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
//		
//		//    pageNumber default starts from 0
//		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
//		
//		Page<User> page =  userRepository.findAll(pageable);
//		
//		List<User> users = page.getContent();
//		
//	//	List<User> users = userRepository.findAll();
//		List<UserDto> userDto = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
//		return userDto;
//	}
	
	
	@Override
	public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		
	//	Sort sort = Sort.by(sortBy);
		
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		
		//    pageNumber default starts from 0
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<User> page =  userRepository.findAll(pageable);
		
//		List<User> users = page.getContent();
//		
//	//	List<User> users = userRepository.findAll();
//		List<UserDto> userDto = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
//		
//		PageableResponse<UserDto> response = new PageableResponse<>();
//		
//		response.setContent(userDto);
//		response.setLastPage(page.isLast());
//		response.setPageNumber(page.getNumber());
//		response.setPageSize(page.getSize());
//		response.setTotalElements(page.getTotalElements());
//		response.setTotalPages(page.getTotalPages());
//		
		PageableResponse<UserDto> response = helper.getPageableResponse(page, UserDto.class);
		
		return response;
	}

	@Override
	public void deleteUser(String userId) throws IOException {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("This userId is not found"));
		
		
		//DELETE USER PROFILE IMAGE
		  // images/user/abc.png
		 String fullPath = imagePath + user.getImageName();
		 try {
		 Path path = Paths.get(fullPath);
		 Files.delete(path);
		 }catch(NoSuchFileException ex) {
			 logger.info("user image not found in folder");
			 
		 }catch(IOException e)
		 {
			 throw new RuntimeException(e);
		 }
		userRepository.delete(user);
		
	}

	@Override
	public List<UserDto> getBySearch(String keywords) {
		// TODO Auto-generated method stub
		List<User> users = userRepository.findByNameContaining(keywords);
		List<UserDto> userDto = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
		return userDto;
	}
	
	
	// user convert to dto to entity
	
	
	private UserDto entityToDto(User savedUser) {
		// TODO Auto-generated method stub
		// **********************************manually be create a mapping to entity to Dto classes**************************************
//		UserDto userDto = UserDto.builder()
//		.userId(savedUser.getUserId())
//		.name(savedUser.getName())
//		.email(savedUser.getEmail())
//		.password(savedUser.getPassword())
//		.about(savedUser.getAbout())
//		.gender(savedUser.getGender())
//		.imageName(savedUser.getImageName())
//		.build();
		
		return mapper.map(savedUser, UserDto.class);
	}


	private User dtoToEntity(UserDto userDto) {
		// TODO Auto-generated method stub
//		User user = User.builder()
//		.userId(userDto.getUserId())
//		.name(userDto.getName())
//		.email(userDto.getEmail())
//		.password(userDto.getPassword())
//		.about(userDto.getAbout())
//		.imageName(userDto.getImageName())
//		.gender(userDto.getGender()).build();
//		
		return mapper.map(userDto, User.class);
	}


	@Override
	public Optional<User> findUserByEmailOptional(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}



}

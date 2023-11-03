package com.sumit.electronic.store.repo;

import java.util.List;
import java.util.Optional;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sumit.electronic.store.entities.User;

public interface UserRepository  extends JpaRepository<User, String>{
	

	Optional<User> findByEmail(String email);
	Optional<User> findByEmailAndPassword(String email, String password);
	List<User> findByNameContaining(String keywords);
	
	

}

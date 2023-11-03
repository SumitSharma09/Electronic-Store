package com.sumit.electronic.store.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumit.electronic.store.dtos.CategoryDto;
import com.sumit.electronic.store.entities.Category;
import com.sumit.electronic.store.entities.User;

public interface CategoryRepository extends JpaRepository<Category, String>{
	
	
	
	Optional<Category> findByTitle(String email);
	List<Category> findByTitleContains(String keywords);
}

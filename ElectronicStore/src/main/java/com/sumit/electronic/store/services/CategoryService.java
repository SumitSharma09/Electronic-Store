package com.sumit.electronic.store.services;

import java.util.List;

import com.sumit.electronic.store.dtos.CategoryDto;
import com.sumit.electronic.store.dtos.PageableResponse;
import com.sumit.electronic.store.entities.Category;

public interface CategoryService {
	
	// create
	CategoryDto create(CategoryDto categoryDto);
	
	//update
	CategoryDto update(CategoryDto categoryDto, String categoryId);

	//delete
	void delete(String categoryId);
	
	//get a single category
	
	CategoryDto getCategory(String categoryId);

	// get all
	PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

	

	List<CategoryDto> getBySearch(String title);
	
	
	
	
	
}

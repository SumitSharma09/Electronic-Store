package com.sumit.electronic.store.servicesImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.boot.model.source.internal.hbm.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.sumit.electronic.store.dtos.CategoryDto;
import com.sumit.electronic.store.dtos.PageableResponse;
import com.sumit.electronic.store.dtos.UserDto;
import com.sumit.electronic.store.entities.Category;
import com.sumit.electronic.store.entities.User;
import com.sumit.electronic.store.exception.ResourceNotFoundException;
import com.sumit.electronic.store.helper.helper;
import com.sumit.electronic.store.repo.CategoryRepository;
import com.sumit.electronic.store.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper mapper;
	

	@Override
	public CategoryDto create(CategoryDto categoryDto) {
		// TODO Auto-generated method stub
		String categoryId = UUID.randomUUID().toString();
		categoryDto.setCategoryId(categoryId);
		Category category = mapper.map(categoryDto, Category.class);
		Category savedCategory = categoryRepository.save(category);
		return mapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto update(CategoryDto categoryDto, String categoryId) {
		// TODO Auto-generated method stub
	      Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("This categoryId is not found"));
	      category.setTitle(categoryDto.getTitle());
	      category.setDescription(categoryDto.getDescription());
	      category.setCoverImage(categoryDto.getCoverImage());
	      Category updatedCategory = categoryRepository.save(category);
	   	return mapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public void delete(String categoryId) {
		// TODO Auto-generated method stub
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("This categoryId is not found"));
		categoryRepository.delete(category);
		
	}

	@Override
	public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
	    Pageable pageable = PageRequest.of(pageNumber, pageSize); 
		Page<Category> page = categoryRepository.findAll(pageable);
		PageableResponse<CategoryDto> pageableResponse = helper.getPageableResponse(page, CategoryDto.class);
		return pageableResponse ;
	}

	@Override
	public CategoryDto getCategory(String categoryId) {
		// TODO Auto-generated method stub	
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("This categoryId is not found"));	
		return mapper.map(category, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getBySearch(String title) {
		// TODO Auto-generated method stub
		List<Category> category = categoryRepository.findByTitleContains(title);
//		List<CategoryDto> userDto = category.stream().map(category -> entityToDto(user)).collect(Collectors.toList());
		List<CategoryDto> cd = category.stream().map(categories -> entityToDto(categories)).collect(Collectors.toList());		
		return cd;
		
	}

	private CategoryDto entityToDto(Category savedcategories) {
		// TODO Auto-generated method stub
		return mapper.map(savedcategories, CategoryDto.class);
	}
	
	

}

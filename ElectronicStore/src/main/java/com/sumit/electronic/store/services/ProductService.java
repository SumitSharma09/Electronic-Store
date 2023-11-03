package com.sumit.electronic.store.services;

import java.util.List;

import com.sumit.electronic.store.dtos.PageableResponse;
import com.sumit.electronic.store.dtos.ProductDto;

public interface ProductService {
	
	// create 
	ProductDto create(ProductDto productDto);
	//update
	ProductDto update(ProductDto productDto, String productId);
	// delete
	void delete(String productId);
	//get single 
	ProductDto get(String productId);
	//get all
	 PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
	//get all: live
	PageableResponse<ProductDto> getAlllive(int pageNumber, int pageSize, String sortBy, String sortDir);
	//search Product
	PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);
	//other methods
	
	// create product with category
	
	ProductDto createWithCategory(ProductDto productDto, String categoryId);
	

}

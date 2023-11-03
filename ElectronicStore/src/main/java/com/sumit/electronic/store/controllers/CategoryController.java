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
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.sumit.electronic.store.dtos.CategoryDto;
import com.sumit.electronic.store.dtos.ImageResponse;
import com.sumit.electronic.store.dtos.PageableResponse;
import com.sumit.electronic.store.dtos.ProductDto;
import com.sumit.electronic.store.dtos.UserDto;
import com.sumit.electronic.store.services.CategoryService;
import com.sumit.electronic.store.services.FileService;
import com.sumit.electronic.store.services.ProductService;
import com.sumit.electronic.store.services.UserService;


@RestController
@RequestMapping("/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryservice;
	
	@Autowired
	private ProductService productservice;
	
	@Autowired
	private FileService fileservice;
	
	@Value("${user.profile.image.path}")
	String imageUploadPath;
	
	// create
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
		// call service to save object
		
		CategoryDto categoryDto1 = categoryservice.create(categoryDto);
		return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
		
	}
	//update 
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory( @PathVariable("categoryId") String categoryId, @Valid @RequestBody CategoryDto categoryDto){
		CategoryDto updatedCategoryDto = categoryservice.update(categoryDto, categoryId);
		
		return new ResponseEntity<>(updatedCategoryDto,HttpStatus.OK);
		
	}
	// delete methods
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("{categoryId}") 
	public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) throws IOException{
		categoryservice.delete(categoryId);
		ApiResponseMessage message = ApiResponseMessage.builder().message("Category has been deleted Successfully")
				.success(true).status(HttpStatus.OK).build();
		return new ResponseEntity<>(message ,HttpStatus.OK);
	}
	
	// get all
	
	@GetMapping
	public ResponseEntity<PageableResponse<CategoryDto>> getAll(
			@RequestParam(value= "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value= "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			
			
			){
		return new ResponseEntity<>(categoryservice.getAll(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
	}
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryId){
		return new ResponseEntity<>(categoryservice.getCategory(categoryId), HttpStatus.OK);
	}
	
	@GetMapping("title/{keywords}")
	public ResponseEntity<List<CategoryDto>> getBySearch(@PathVariable String keywords){
		return new ResponseEntity<>(categoryservice.getBySearch(keywords), HttpStatus.OK);
	}
	@PostMapping("/image/{categoryId}")
	public ResponseEntity<ImageResponse> uploadImage(@RequestParam("coverImage") MultipartFile image, @PathVariable String categoryId) throws IOException{
		String imageName = fileservice.uploadFile(image, imageUploadPath);
		CategoryDto category = categoryservice.getCategory(categoryId);
		category.setCoverImage(imageName);
		CategoryDto categoryDto = categoryservice.update(category, categoryId);
		ImageResponse imageResponse = ImageResponse.builder().ImageName(imageName).success(true).status(HttpStatus.CREATED).build();
		return new ResponseEntity<> (imageResponse, HttpStatus.CREATED);
			
	}
	// serve a image
	@GetMapping("/image/{categoryId}")
	public void serveImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
		CategoryDto category = categoryservice.getCategory(categoryId);
		logger.info("user image name is: {}", category.getCategoryId());
		InputStream resource = fileservice.getResources(imageUploadPath, category.getCoverImage());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
		
	}
	// create  product with category
	
	@PostMapping("/{categoryId}/products")
	public ResponseEntity<ProductDto> createProductWithCategory(
			@PathVariable("categoryId") String categoryId, @RequestBody ProductDto dto
			){
		ProductDto productWithCategory = productservice.createWithCategory(dto, categoryId);
		return new ResponseEntity<>(productWithCategory, HttpStatus.OK);
		
	}
	
	
	
	
	 private Logger logger = LoggerFactory.getLogger(CategoryService.class);
	 
	 

}

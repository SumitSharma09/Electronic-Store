package com.sumit.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

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
import com.sumit.electronic.store.dtos.ImageResponse;
import com.sumit.electronic.store.dtos.PageableResponse;
import com.sumit.electronic.store.dtos.ProductDto;
import com.sumit.electronic.store.dtos.UserDto;
import com.sumit.electronic.store.entities.Product;
import com.sumit.electronic.store.services.FileService;
import com.sumit.electronic.store.services.ProductService;
import com.sumit.electronic.store.services.UserService;



@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productservice;
	
	@Autowired
	private FileService fileservice;
	
	@Value("${product.image.path}")
	private String imagePath;

	private Logger logger = LoggerFactory.getLogger(ProductService.class);
	
	// create
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
		ProductDto createdProduct = productservice.create(productDto);
		return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId,
			@RequestBody ProductDto productDto) {
		ProductDto updateProduct = productservice.update(productDto, productId);
		return new ResponseEntity<>(updateProduct, HttpStatus.OK);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId) {
		productservice.delete(productId);
		ApiResponseMessage responseMessage = ApiResponseMessage.builder()
				.message("This is ID is succeesfully Deleted !!!").status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<>(responseMessage, HttpStatus.OK);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {
	 ProductDto getproduct =  productservice.get(productId);
		
		return new ResponseEntity<>(getproduct, HttpStatus.CREATED);
	}
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAll(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
	   	PageableResponse<ProductDto> pageableResponse = productservice.getAll(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}

	@GetMapping("search/{live}")
	public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
	   	PageableResponse<ProductDto> pageableResponse = productservice.getAll(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}
	@GetMapping("/search/{query}")
	public ResponseEntity<PageableResponse<ProductDto>> searchProduct( @PathVariable String query, 
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		PageableResponse<ProductDto> pageableResponse = productservice.searchByTitle(query,pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}
	// upload images
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/image/{productId}")
	public ResponseEntity<ImageResponse> uploadProductImage(@PathVariable String productId, @RequestParam("productImage") MultipartFile image) throws IOException{
		String filename = fileservice.uploadFile(image, imagePath);
		ProductDto productDto = productservice.get(productId);
		productDto.setProductImageName(filename);
		ProductDto updatedProduct = productservice.update(productDto, productId);
		
		ImageResponse response = ImageResponse.builder().ImageName(updatedProduct.getProductImageName()).message("Product image is successfuly uploaded").status(HttpStatus.CREATED).success(true).build();		
		
				return new ResponseEntity<>(response, HttpStatus.CREATED);
		
	}
	
	// serve a image
		@GetMapping("/image/{productId}")
		public void serveImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
			ProductDto product = productservice.get(productId);
			logger.info("productImage image name is: {}", product.getProductImageName());
			InputStream resource = fileservice.getResources(imagePath, product.getProductImageName());
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			StreamUtils.copy(resource, response.getOutputStream());
			
		}

}

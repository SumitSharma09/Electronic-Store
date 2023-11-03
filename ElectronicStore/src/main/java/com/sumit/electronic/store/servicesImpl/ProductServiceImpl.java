package com.sumit.electronic.store.servicesImpl;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.sumit.electronic.store.dtos.PageableResponse;
import com.sumit.electronic.store.dtos.ProductDto;
import com.sumit.electronic.store.entities.Category;
import com.sumit.electronic.store.entities.Product;
import com.sumit.electronic.store.entities.User;
import com.sumit.electronic.store.exception.ResourceNotFoundException;
import com.sumit.electronic.store.helper.helper;
import com.sumit.electronic.store.repo.CategoryRepository;
import com.sumit.electronic.store.repo.ProductRepository;
import com.sumit.electronic.store.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public ProductDto create(ProductDto productDto) {
		// TODO Auto-generated method stub
		Product product = mapper.map(productDto, Product.class);
		// productId generated automatically
		String productId = UUID.randomUUID().toString();
		product.setProductId(productId);
		
		product.setAddedDate(new Date(0));
		Product saveProduct = productRepository.save(product);
		return mapper.map(saveProduct, ProductDto.class);
	}

	@Override
	public ProductDto update(ProductDto productDto, String productId) {
		// TODO Auto-generated method stub
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found of given id"));
		
		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setQuantity(productDto.getQuantity());
		product.setStock(productDto.isStock());
		product.setLive(productDto.isLive());
		product.setProductImageName(productDto.getProductImageName());
		
		// save the entity
		
		Product updateProduct = productRepository.save(product);
		
		return mapper.map(updateProduct, ProductDto.class);
	}

	@Override
	public void delete(String productId) {
		// TODO Auto-generated method stub
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found of given id"));
		productRepository.delete(product);
		
	}

	@Override
	public ProductDto get(String productId) {
		// TODO Auto-generated method stub
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found of given id"));
		return mapper.map(product, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort =(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()); 
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findAll(pageable);
		return helper.getPageableResponse(page, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAlllive(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort =(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()); 
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByLiveTrue(pageable);
		return helper.getPageableResponse(page, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort =(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()); 
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByTitleContaining(subTitle, pageable);
		return helper.getPageableResponse(page, ProductDto.class);
	}

	@Override
	public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
		// TODO Auto-generated method stub
		// firstly fetch the category
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found !!"));
		// TODO Auto-generated method stub
		Product product = mapper.map(productDto, Product.class);
		// productId generated automatically
		String productId = UUID.randomUUID().toString();
		product.setProductId(productId);
		
		product.setAddedDate(new Date(0));
		product.setCategory(category);
		Product saveProduct = productRepository.save(product);
		return mapper.map(saveProduct, ProductDto.class);
		
	}
	
}

package com.sumit.electronic.store.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sumit.electronic.store.dtos.ProductDto;
import com.sumit.electronic.store.entities.Product;

public interface ProductRepository extends JpaRepository<Product, String>{
	
	//search
	Page<Product> findByTitleContaining(String subTitle, Pageable pageable);
	Page<Product> findByLiveTrue(Pageable pageable);
	
	//other methods
	//custom finder methods
	//query methods

}

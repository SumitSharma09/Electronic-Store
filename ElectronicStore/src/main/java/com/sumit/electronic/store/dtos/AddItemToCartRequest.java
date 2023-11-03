package com.sumit.electronic.store.dtos;

import java.util.List;

import com.sumit.electronic.store.entities.Category;
import com.sumit.electronic.store.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddItemToCartRequest {

	private String productId;
	private int quantity;
	
	
}

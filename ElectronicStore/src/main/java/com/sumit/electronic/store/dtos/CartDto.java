package com.sumit.electronic.store.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sumit.electronic.store.entities.Category;
import com.sumit.electronic.store.entities.Product;
import com.sumit.electronic.store.entities.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
	
	private String cartId;
	private Date createdAt;
	private UserDto user;
	private List<CartItemDto> items = new ArrayList<>();

}

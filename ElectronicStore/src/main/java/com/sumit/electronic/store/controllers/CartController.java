package com.sumit.electronic.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumit.electronic.store.dtos.AddItemToCartRequest;
import com.sumit.electronic.store.dtos.ApiResponseMessage;
import com.sumit.electronic.store.dtos.CartDto;
import com.sumit.electronic.store.services.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	
	// add items  to cart
	@PostMapping("/{userId}")
	public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request){
		CartDto cartDto =cartService.addItemToCart(userId, request);
		return new ResponseEntity<>(cartDto, HttpStatus.OK);
	}
	@DeleteMapping("/{userId}/items/{itemsId}")
	public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId , @PathVariable int itemId){
		cartService.removeItemFromCart(userId, itemId);
		ApiResponseMessage response = ApiResponseMessage.builder().message("Item is removed !!").success(true).status(HttpStatus.OK).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId , @PathVariable int itemId){
		cartService.clearCart(userId);
		ApiResponseMessage response = ApiResponseMessage.builder().message("Not cart is blank !!").success(true).status(HttpStatus.OK).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@GetMapping("/{userId}")
	public ResponseEntity<CartDto> getCart(@PathVariable String userId){
		CartDto cartDto =cartService.getCartByUser(userId);
		return new ResponseEntity<>(cartDto, HttpStatus.OK);
	}
}

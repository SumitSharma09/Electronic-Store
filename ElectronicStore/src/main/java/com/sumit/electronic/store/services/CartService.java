package com.sumit.electronic.store.services;

import com.sumit.electronic.store.dtos.AddItemToCartRequest;
import com.sumit.electronic.store.dtos.CartDto;

public interface CartService {
	
	// add items to cart:
	//case 1: cart user is not available: we will create the cart 
	//case 2: cart available add the items to cart
	
	CartDto addItemToCart(String userId, AddItemToCartRequest request);
	
	
	// remove items from cart 
	void removeItemFromCart(String userId, int cartItem);
	
	//remove all items from cart
	void clearCart(String userId);

	CartDto getCartByUser(String userId);
}

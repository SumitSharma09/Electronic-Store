package com.sumit.electronic.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumit.electronic.store.entities.Cart;
import com.sumit.electronic.store.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

	
}

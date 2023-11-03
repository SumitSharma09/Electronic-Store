package com.sumit.electronic.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumit.electronic.store.entities.OrderItem;

public interface OrderItemsReposiotry extends JpaRepository<OrderItem, Integer>{
	
	

}

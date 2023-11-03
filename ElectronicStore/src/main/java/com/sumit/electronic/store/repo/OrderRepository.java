package com.sumit.electronic.store.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumit.electronic.store.entities.Order;
import com.sumit.electronic.store.entities.User;

public interface OrderRepository extends JpaRepository<Order, String>{
	
	List<Order> findByUser(User user);

}

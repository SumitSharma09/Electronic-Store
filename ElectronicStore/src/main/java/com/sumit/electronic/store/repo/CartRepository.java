package com.sumit.electronic.store.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumit.electronic.store.entities.Cart;
import com.sumit.electronic.store.entities.User;

public interface CartRepository extends JpaRepository<Cart, String>{

	Optional<Cart> findByUser(User user);
}

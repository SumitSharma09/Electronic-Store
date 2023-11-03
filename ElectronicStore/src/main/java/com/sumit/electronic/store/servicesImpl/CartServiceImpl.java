package com.sumit.electronic.store.servicesImpl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumit.electronic.store.dtos.AddItemToCartRequest;
import com.sumit.electronic.store.dtos.CartDto;
import com.sumit.electronic.store.entities.Cart;
import com.sumit.electronic.store.entities.CartItem;
import com.sumit.electronic.store.entities.Product;
import com.sumit.electronic.store.entities.User;
import com.sumit.electronic.store.exception.BadApiRequest;
import com.sumit.electronic.store.exception.ResourceNotFoundException;
import com.sumit.electronic.store.repo.CartItemRepository;
import com.sumit.electronic.store.repo.CartRepository;
import com.sumit.electronic.store.repo.ProductRepository;
import com.sumit.electronic.store.repo.UserRepository;
import com.sumit.electronic.store.services.CartService;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Override
	public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
		// TODO Auto-generated method stub
		int quantity = request.getQuantity();
		String productId = request.getProductId();
		
		if(quantity<=0) {
			throw new BadApiRequest("Requested qunatity is not valid !!");
		}
		
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("PRODUCT ID IS NOT FOUND"));
		//fetch the user from db
		
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not Found"));
		
		
		Cart cart = null;
		
		try {
			 cart = cartRepository.findByUser(user).get();
			
		}catch(NoSuchElementException e) {
			cart = new Cart();
			cart.setCartId(UUID.randomUUID().toString());
			cart.setCreatedAt(new Date());
		}
		
		//perform cart operations 
		// if cart items already present: then update
		AtomicReference<Boolean> updated= new AtomicReference<>(false);
		List<CartItem> items = cart.getItems();
		items = items.stream().map(item -> {
			
			if(item.getProduct().getProductId().equals(productId)) {
				item.setQuantity(quantity);
				item.setTotalPrice(quantity * product.getDiscountedPrice());
				updated.set(true);
			}
			return item;
		}).collect(Collectors.toList());
		
	//	cart.setItems(updatedItems);
		
		
		if(!updated.get()) {
		// create items
		CartItem cartItem = CartItem.builder().quantity(quantity).totalPrice(quantity * product.getDiscountedPrice()).cart(cart).product(product).build();
		
		cart.getItems().add(cartItem);
		}
		cart.setUser(user);
		Cart updatedcart = cartRepository.save(cart);
		return mapper.map(updatedcart, CartDto.class);
	}

	@Override
	public void removeItemFromCart(String userId, int cartItem) {
		// TODO Auto-generated method stub
		// conditions
		
		CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("Cart Item not FOund Exception"));
		cartItemRepository.delete(cartItem1);
	}

	@Override
	public void clearCart(String userId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not Found"));
		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given User are not found"));
		cart.getItems().clear();	
		cartRepository.save(cart);
		
	}

	@Override
	public CartDto getCartByUser(String userId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not Found"));
		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given User are not found"));
		return mapper.map(cart, CartDto.class);
	}
	

	


	
	

}

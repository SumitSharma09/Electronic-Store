package com.sumit.electronic.store.servicesImpl;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.concurrent.atomic.AtomicReference;

import com.sumit.electronic.store.dtos.CreateOrderRequest;
import com.sumit.electronic.store.dtos.OrderDto;
import com.sumit.electronic.store.dtos.PageableResponse;
import com.sumit.electronic.store.entities.User;
import com.sumit.electronic.store.entities.Cart;
import com.sumit.electronic.store.entities.CartItem;
import com.sumit.electronic.store.entities.Order;
import com.sumit.electronic.store.entities.OrderItem;
import com.sumit.electronic.store.exception.BadApiRequest;
import com.sumit.electronic.store.exception.ResourceNotFoundException;
import com.sumit.electronic.store.helper.helper;
import com.sumit.electronic.store.repo.CartRepository;
import com.sumit.electronic.store.repo.OrderRepository;
import com.sumit.electronic.store.repo.UserRepository;
import com.sumit.electronic.store.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private UserRepository userReposiotry;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	
	@Override
	public OrderDto createOrder(CreateOrderRequest orderDto) {
		// TODO Auto-generated method stub
		
        String userId = orderDto.getUserId();
		String cartId = orderDto.getCartId();
		User user = userReposiotry.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User are not found !!"));
		Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart is not found!!"));
		List<CartItem> cartItems = cart.getItems();
		if(cartItems.size()<=0) {
			throw new BadApiRequest("Invalid number of items in cart !!");	
		}
		Order order =Order.builder()
		.billingName(orderDto.getBillingName())
		.billingPhone(orderDto.getBillingPhone())
		.billingAddress(orderDto.getBillingAddress())
		.orderedDate(new Date(0))
		.deliveredDate(null)
		.paymentStatus(orderDto.getPaymentStatus())
		.orderStatus(orderDto.getOrderStatus())
		.orderId(UUID.randomUUID().toString())
		.user(user)
		.build();
		
		AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
		
		List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
			
			//cartItem-> OrderItem
		OrderItem orderItem = OrderItem.builder()
			.quantity(cartItem.getQuantity())
			.product(cartItem.getProduct())
			.totalPrce(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice())
			.order(order)
			.build();
		
		  orderAmount.set(orderAmount.get() + orderItem.getTotalPrce());
			return orderItem;
			
		}).collect(Collectors.toList());
		
		order.setOrderItems(orderItems);
		order.setOrderAmount(orderAmount.get());
		
		cart.getItems().clear();
		cartRepository.save(cart);
		Order savedOrder = orderRepository.save(order);
		
		return mapper.map(savedOrder, OrderDto.class);
	}

	@Override
	public void removeOrder(String orderId) {
		// TODO Auto-generated method stub
		Order oder = orderRepository.findById(orderId).orElseThrow(() ->new ResourceNotFoundException("orderID are not found"));
		orderRepository.delete(oder);
	}

	@Override
	public List<OrderDto> getOrderOfUser(String userId) {
		// TODO Auto-generated method stub
		User user = userReposiotry.findById(userId).orElseThrow(() -> new ResourceNotFoundException("this userId is not found!!"));
		List<Order> orders = orderRepository.findByUser(user);
		List<OrderDto> orderDtos =orders.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());
		return orderDtos;
	}

	@Override
	public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		
		Sort sort =(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Order> page = orderRepository.findAll(pageable);
		return helper.getPageableResponse(page, OrderDto.class); 		
	
	}

}

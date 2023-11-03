package com.sumit.electronic.store.services;

import java.util.List;

import com.sumit.electronic.store.dtos.CreateOrderRequest;
import com.sumit.electronic.store.dtos.OrderDto;
import com.sumit.electronic.store.dtos.PageableResponse;

public interface OrderService {
	
	// create order
	OrderDto createOrder(CreateOrderRequest orderDto);
	//remove order
	void removeOrder(String orderId);
	//get orders of user
	List<OrderDto> getOrderOfUser(String userId);
	//get orders
	PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);
	//other Methods(logic) related to order
	

}

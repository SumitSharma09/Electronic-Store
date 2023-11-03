package com.sumit.electronic.store.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sumit.electronic.store.dtos.ApiResponseMessage;
import com.sumit.electronic.store.dtos.CreateOrderRequest;
import com.sumit.electronic.store.dtos.OrderDto;
import com.sumit.electronic.store.dtos.PageableResponse;
import com.sumit.electronic.store.dtos.ProductDto;
import com.sumit.electronic.store.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	
	@Autowired
	private OrderService orderService;
	//create
	@PostMapping
	public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request){
		OrderDto order = orderService.createOrder(request);
		return new ResponseEntity<>(order, HttpStatus.OK);
		
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId){
		orderService.removeOrder(orderId);
		ApiResponseMessage responseMessage = ApiResponseMessage.builder().status(HttpStatus.OK).message("order is removed !!").success(true).build();
		return new ResponseEntity<>(responseMessage, HttpStatus.OK);
		
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<List<OrderDto>> getOrderOfUser(@PathVariable String userId){
		List<OrderDto> orderOfUser = orderService.getOrderOfUser(userId);
		return new ResponseEntity<>(orderOfUser, HttpStatus.OK);
		
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<OrderDto>> getOrders(
		@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
		@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
		@RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false) String sortBy,
		@RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
	    PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(orders, HttpStatus.OK);
		
	}
	
	
	
}

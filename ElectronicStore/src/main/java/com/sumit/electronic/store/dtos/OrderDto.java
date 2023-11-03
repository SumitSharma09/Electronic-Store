package com.sumit.electronic.store.dtos;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.sumit.electronic.store.entities.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderDto {

	private String orderId;
	private String orderStatus="PENDING";
	private String paymentStatus="NOTPAID";
	private long orderAmount;
	private String billingAddress;
	private String billingPhone;
	private String billingName;
	private Date orderedDate=new Date(0000-00-00);
	private Date deliveredDate;
	
	//user
	private UserDto user;
	
	private List<OrderItemDto> orderItems = new ArrayList<>();
	
	
}

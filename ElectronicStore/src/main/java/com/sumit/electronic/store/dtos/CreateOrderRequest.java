package com.sumit.electronic.store.dtos;

import java.sql.Date;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

	@NotBlank(message = "Cart id is Required !!")
	private String cartId;
	@NotBlank(message = "User id is Required !!")
	private String userId;
	 
	private String orderStatus="PENDING";
	private String paymentStatus="NOTPAID";
	@NotBlank(message = "Address is required !!")
	private String billingAddress;
	@NotBlank(message = "Phone number is required !!")
	private String billingPhone;
	@NotBlank(message = "Billin name  is required !!")
	private String billingName;
	
}

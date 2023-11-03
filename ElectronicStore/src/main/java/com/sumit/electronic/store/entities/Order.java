package com.sumit.electronic.store.entities;

import java.sql.Date;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Entity
@Table(name = "orders")
public class Order {
	
	@Id
	private String orderId;
	
	//PENDING, DISPATCHED, DELIVERED
	//enum
	private String orderStatus;
	
	//NOT-PAID, PAID
	//enum
	//boolean- false=>NOT-PAID  || true=>PAID
	private String paymentStatus;
	
	private long orderAmount;
	
	@Column(length = 1000)
	private String billingAddress;
	private String billingPhone;
	private String billingName;
	private Date orderedDate;
	private Date deliveredDate;
	
	//user
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	
	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();
	
	
	

}

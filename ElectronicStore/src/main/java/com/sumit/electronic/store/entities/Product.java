package com.sumit.electronic.store.entities;

import java.sql.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "products")
public class Product {
	
	@Id
	private String productId;
	private String title;
	@Column(length =10000)
	private String description;
	private int price;
	private int discountedPrice;
	private int quantity;
	private Date addedDate;
	private boolean live;
	private boolean stock;
	private String productImageName;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="category_id")
	private Category category;
	
	@OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
	private CartItem cartItem;
	

}

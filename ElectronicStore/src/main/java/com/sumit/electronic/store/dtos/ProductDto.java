package com.sumit.electronic.store.dtos;

import java.sql.Date;

import javax.persistence.Column;

import com.sumit.electronic.store.entities.Category;

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
public class ProductDto {

	
	private String productId;
	private String title;
	@Column(length = 10000)
	private String description;
	private int price;
	private int discountedPrice;
	private int quantity;
	private Date addedDate;
	private boolean live;
	private boolean stock;
	private String productImageName;
	
//	private CategoryDto category;
}

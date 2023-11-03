package com.sumit.electronic.store.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.sumit.electronic.store.dtos.PageableResponse;
import com.sumit.electronic.store.dtos.UserDto;
import com.sumit.electronic.store.entities.User;

public class helper {
	
	
	public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type){
		
		List<U> entity = page.getContent();
		
		//	List<User> users = userRepository.findAll();
			List<V> userDto = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());
			
			PageableResponse<V> response = new PageableResponse<>();
			
			response.setContent(userDto);
			response.setLastPage(page.isLast());
			response.setPageNumber(page.getNumber());
			response.setPageSize(page.getSize());
			response.setTotalElements(page.getTotalElements());
			response.setTotalPages(page.getTotalPages());
			
			
			return response;
		
	}

}

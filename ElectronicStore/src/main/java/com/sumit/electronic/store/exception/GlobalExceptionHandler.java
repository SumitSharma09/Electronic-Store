package com.sumit.electronic.store.exception;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sumit.electronic.store.dtos.ApiResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseMessage> resourceNotFoundException(ResourceNotFoundException ex){
		ApiResponseMessage response = ApiResponseMessage.builder().message("ex.getMessage")
				.success(true).status(HttpStatus.OK).build();
		
		return new ResponseEntity(response, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		
		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
		Map<String, Object> response= new HashMap<>();
		allErrors.stream().forEach(objectError -> {
			String message = objectError.getDefaultMessage();
			String field = ((FieldError) objectError).getField();
			response.put(field, message);
		});
		return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		
	}
	// handle bad api request exception
	@ExceptionHandler(BadApiRequest.class)
	public ResponseEntity<ApiResponseMessage> handleBadApiRequest(ResourceNotFoundException ex){
		ApiResponseMessage response = ApiResponseMessage.builder().message("ex.getMessage")
				.success(false).status(HttpStatus.BAD_REQUEST).build();
		
		return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		
	}

}

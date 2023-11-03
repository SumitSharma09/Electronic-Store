package com.sumit.electronic.store.exception;

public class BadApiRequest extends RuntimeException{

	public BadApiRequest(String message) {
		super(message);
	}
	
	public BadApiRequest() {
		super("Bad Request !!");
	}
}

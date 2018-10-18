package com.scheid.jsondiff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception used for handling invalid data provided for comparison 
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class JsonDiffException extends RuntimeException {
	
	private static final long serialVersionUID = 4817698030715952395L;

	public JsonDiffException(String message) {
		super(message);
	}
	
	public JsonDiffException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}

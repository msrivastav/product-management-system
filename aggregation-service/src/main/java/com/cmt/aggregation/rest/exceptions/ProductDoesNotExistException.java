package com.cmt.aggregation.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception class will be used to handle the case of Product not being found
 * @author Manoo.Srivastav
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductDoesNotExistException extends RuntimeException {
	private static final long serialVersionUID = 115346513645454L;

	public ProductDoesNotExistException(String s) {
        super(s);
    }
}

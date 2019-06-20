package com.cmt.aggregation.rest.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cmt.aggregation.rest.exceptions.ProductDoesNotExistException;

import lombok.extern.slf4j.Slf4j;

/**
 * This class provides configuration to handle known exception responses
 * @author Manoo.Srivastav
 *
 */
@ControllerAdvice
@Slf4j
public class ExceptionConfiguration extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
		try {
			List<String> messages = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
			log.error("Constraint violation occured : " + messages.toString());
			return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST); //HTTP status 400
		} 
		catch (Exception e) {
			log.error("Exception : " + e.getMessage());
			return new ResponseEntity<>(Arrays.asList(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR); // HTTP status 500
		}
	}

	@ExceptionHandler(ProductDoesNotExistException.class)
	protected ResponseEntity<?> handleProductNotFound(ProductDoesNotExistException ex, HttpServletRequest request) {
		try {
			log.error(ex.getMessage());
			return new ResponseEntity<>(Arrays.asList(ex.getMessage()), HttpStatus.NOT_FOUND); //HTTP status 404
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
			return new ResponseEntity<>(Arrays.asList(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR); //HTTP status 500
		}
	}
	
	//General runtime exception handler so that end used doen't see server traces
	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<?> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
			return new ResponseEntity<>(Arrays.asList(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

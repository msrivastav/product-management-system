package com.cmt.aggregation.rest.deligator;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmt.aggregation.rest.model.request.Product;
import com.cmt.aggregation.rest.model.response.DayWiseStatisticResponse;
import com.cmt.aggregation.rest.model.response.ProductResponseModel;
import com.cmt.aggregation.rest.processor.ProductProcessor;

import lombok.extern.slf4j.Slf4j;

/**
 * This is just an entry class to provide layer between endpoint and processor. If any process to combine multiple processors has to be done,
 * it will be done from here
 * @author Manoo.Srivastav
 *
 */
@Component("productProcessDeligator")
@Slf4j
public class ProductProcessDeligator {

	@Autowired
	@Qualifier("productProcessor")
	private ProductProcessor productProcessor;
	
	@Value("${async.call.timeout}")
	private long timeout;
	
	@Value("${async.read.all.timeout}")
	private long readAllTimeout;

	public ProductResponseModel create(Product product) {
		log.info("Calling product processor to create product : " + product);

		ProductResponseModel response = null;

		try {
			response = productProcessor.create(product).get(timeout, TimeUnit.MILLISECONDS);
		} 
		catch (Exception e) {
			log.error("Error while processing create request : {}", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}

		return response;
	}

	/**
	 * This method is used to update the product
	 * @author Manoo.Srivastav
	 */
	public ProductResponseModel update(Product product) {
		log.info("Calling product processor to update product : {}", product);

		ProductResponseModel response = null;

		try {
			response = productProcessor.update(product).get(timeout, TimeUnit.MILLISECONDS);
		} 
		catch (Exception e) {
			log.error("Error while processing update request : {}", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}

		return response;
	}

	public void delete(String id) {
		log.info("Calling product processor to delete the product with id : {}", id);

		try {
			productProcessor.delete(id);
		} 
		catch (Exception e) {
			log.error("Error while processing delete request : {}", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}

	}

	/**
	 * Method to read one record based on ID
	 */
	public ProductResponseModel read(String id) {
		log.info("Calling processor to read single record with id : {}", id);

		ProductResponseModel response = null;
		
		try {
			response = productProcessor.read(id).get(timeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			log.error("Error while processing read request : {}", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
		return response;
	}

	/**
	 * Method to read all the product records
	 */
	public List<ProductResponseModel> readAll() {
		log.info("Calling processor to fetch all records");
		
		List<ProductResponseModel> response = null;
		
		try {
			response = productProcessor.readAll().get(readAllTimeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			log.error("Error while processing read all request : {}", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
		return response;
	}

	/**
	 * Method to get count of total Products created and total products updated for today
	 */
	public DayWiseStatisticResponse getStatForDay() {
		log.info("Calling processor to get total records stats for today");
		
		DayWiseStatisticResponse response = null;
		
		try {
			response = productProcessor.getStatForDay(new Date()).get();
		} catch (Exception e) {
			log.error("Error while processing stat request : {}", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
		return response;
	}
}

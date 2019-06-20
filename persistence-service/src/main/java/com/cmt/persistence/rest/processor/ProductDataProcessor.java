package com.cmt.persistence.rest.processor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmt.persistence.data.entity.DeleteProduct;
import com.cmt.persistence.data.entity.Product;
import com.cmt.persistence.data.repository.ProductDeleteRepository;
import com.cmt.persistence.data.repository.ProductRepository;
import com.cmt.persistence.rest.exceptions.ProductDoesNotExistException;
import com.cmt.persistence.rest.model.response.DayWiseStatisticResponse;
import com.cmt.persistence.rest.model.response.ProductResponseModel;

import lombok.extern.slf4j.Slf4j;

/**
 * This service is used to process Product related CRUD operations before repository call
 * @author Manoo.Srivastav
 *
 */
@Service("ProductDataProcessor")
@Slf4j
public class ProductDataProcessor implements DataProcessor<ProductResponseModel, Product, String> {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductDeleteRepository productDeleteRepository;
	
	/**
	 * This method is used to create the product
	 * If product is already there in DB, the  service should return that product
	 * @author Manoo.Srivastav
	 */
	@Override
	@Transactional
	public ProductResponseModel create(Product product) {
		//Since 
		Product tempProduct = productRepository.findById(product.getUuid()).orElse(null);
		if(tempProduct!=null) {
			log.info("Product already exists for Id : " + product.getUuid());
			return new ProductResponseModel(tempProduct);
		}
		product = productRepository.saveAndFlush(product);
		log.info("New product created : " + product);
		return new ProductResponseModel(product);
	}

	/**
	 * This method is used to update the product
	 * This is transactional method as first it will read the data then save new state
	 * @author Manoo.Srivastav
	 */
	@Override
	@Transactional
	public ProductResponseModel update(Product newProduct) {
		Product product = productRepository.findById(newProduct.getUuid())
				.orElseThrow(()->new ProductDoesNotExistException("Product not found with id: " + newProduct.getUuid()));
		product.setName(newProduct.getName());
		product.setDescription(newProduct.getDescription());
		product.setIsAvailable(newProduct.getIsAvailable());
		product.setMeasurementUnit(newProduct.getMeasurementUnit());
		product.setProvider(newProduct.getProvider());
		//Setting the last update date field to track when this data was last updated
		product.setLastUpdateDate(new java.sql.Date(System.currentTimeMillis()));
		product = productRepository.saveAndFlush(product);
		log.info("Product updated : " + product);
		return new ProductResponseModel(product);
	}

	/**
	 * This method deletes data from Product table and stores same data in DELETED_PRODUCT (logical delete). Later on deleted data may be used for
	 * analysis and audit or any other action
	 * @author Manoo.Srivastav 
	 */
	@Override
	@Transactional
	public void delete(String id) {
		Product toDelete = productRepository.findById(id)
				.orElseThrow(()->new ProductDoesNotExistException("Product not found with id: " + id));
		DeleteProduct deleteProduct = new DeleteProduct();
		deleteProduct.fromProduct(toDelete);
		productDeleteRepository.saveAndFlush(deleteProduct);
		productRepository.delete(toDelete);
		log.info("Product deleted from Product Table");
	}

	/**
	 * Method to read one record based on ID
	 */
	@Override
	public ProductResponseModel read(String id) {
		Product product = productRepository.findById(id)
				.orElseThrow(()->new ProductDoesNotExistException("Product not found with id: " + id));
		log.info("Returning product : " + product);
		return new ProductResponseModel(product);
	}

	/**
	 * Method to read all the product records from database
	 * Using parallel stream to convert the data to list. Doing this at java layer will save DB time as here parallel stream can work much faster for massive data
	 * setting timeout for 10 mins
	 */
	@Override
	@Transactional(readOnly=true, timeout=600)
	public List<ProductResponseModel> readAll() {
		List<ProductResponseModel> allProducts = StreamSupport.stream(productRepository.findAll().spliterator(), true)
															  .map( t -> new ProductResponseModel(t))
															  .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		if(allProducts==null || allProducts.isEmpty()) throw new ProductDoesNotExistException("No product data exists");
		return allProducts;
	}
	
	/**
	 * Method to get count of total Products created and total products updated for given date
	 * Since hibernate pushes update timestamp in case of creation as well so all the records 
	 * created today will surely be considered updated today as well
	 * @param date
	 * @return DayWiseStatisticResponse
	 */
	@Transactional
	public DayWiseStatisticResponse getStatForDay(Date date) {
		//Setting the date to +1 day so that in query all records with less than that date can be fetched
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		long totalCreates = productRepository.findCountByCreateDate(date);
		log.info("total products created for the date {} -- {}", date, totalCreates);
		long totalUpdates = productRepository.findCountByUpdateDate(date);
		log.info("total products updated for the date {} -- {}", date, totalUpdates);
		DayWiseStatisticResponse resp =
				DayWiseStatisticResponse.builder()
				.date(date)
				.productCreatedCount(totalCreates)
				.productUpdatedCount(totalUpdates)
				.build();
		log.info("Stat for the day : " + resp);
		return resp;
	}
}

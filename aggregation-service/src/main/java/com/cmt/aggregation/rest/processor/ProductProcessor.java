package com.cmt.aggregation.rest.processor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cmt.aggregation.provider.cache.processor.BaseCacheProcessor;
import com.cmt.aggregation.provider.persistence.processor.ProductPersistenceProcessor;
import com.cmt.aggregation.rest.model.request.Product;
import com.cmt.aggregation.rest.model.response.DayWiseStatisticResponse;
import com.cmt.aggregation.rest.model.response.ProductResponseModel;

import lombok.extern.slf4j.Slf4j;

/**
 * This class does all needed processing before sending request to cache processor
 * @author Manoo.Srivastav
 *
 */
@Service("productProcessor")
@Slf4j
public class ProductProcessor {

	@Autowired
	@Qualifier("dataCacheProcessor")
	private BaseCacheProcessor<String, Product> dataCacheProcessor;
	
	@Autowired
	@Qualifier("productPersistenceProcessor")
	private ProductPersistenceProcessor persistenceProcessor;
	
	/**
	 * This method is used to create the product - it sends request to cache processor
	 * @author Manoo.Srivastav
	 */
	@Async
	public CompletableFuture<ProductResponseModel> create(Product product) {
		log.info("Processing new product creation request : " + product);
		
		//Trying to fetch this value from data cache
		Product productResponse = dataCacheProcessor.getFromCache(product.getUuid());
		
		//If the product is in data cache then returning the product
		if(productResponse!=null) {
			log.info("Product found in cache : {}", product);
			return CompletableFuture.completedFuture(getProductResponseModel(productResponse));
		}
		
		//If the value is not in data cache then DB service should be hit to fetch the data
		Product createdProduct = persistenceProcessor.create(product).getProduct();
		
		//Setting data in cache
		dataCacheProcessor.addToCache(createdProduct.getUuid(), createdProduct);
		
		return CompletableFuture.completedFuture(getProductResponseModel(createdProduct));
	}

	/**
	 * This method is used to update the product
	 * @author Manoo.Srivastav
	 */
	@Async
	public CompletableFuture<ProductResponseModel> update(Product product) {
		log.info("Processing product update request : " + product);
		
		//Trying to fetch this value from data cache
		Product productResponse = dataCacheProcessor.getFromCache(product.getUuid());
		
		/*
		 * If product with given uuid is in data cache and it is same as requested updated product, then 
		 * returning it
		 */
		if(productResponse!=null && productResponse.equals(product)) {
			log.info("Product found in cache : {}", productResponse);
			return CompletableFuture.completedFuture(getProductResponseModel(productResponse));
		}
		
		//If the value is not in data cache then DB service should be hit to fetch the data
		Product createdProduct = persistenceProcessor.create(product).getProduct();
		
		//Setting data in cache
		dataCacheProcessor.addToCache(createdProduct.getUuid(), createdProduct);
		
		return CompletableFuture.completedFuture(getProductResponseModel(createdProduct));
	}

	/**
	 * This method deletes data from Product table and stores same data in DELETED_PRODUCT (logical delete). Later on deleted data may be used for
	 * analysis and audit or any other action
	 * @author Manoo.Srivastav 
	 */
	@Async
	public void delete(String id) {
		log.info("Processing the request to delete the product");
		//Deleting data from database
		persistenceProcessor.delete(id);
		
		//Deleting data from cache
		dataCacheProcessor.removeFromCache(id);
		
	}

	/**
	 * Method to read one record based on ID
	 */
	@Async
	public CompletableFuture<ProductResponseModel> read(String id) {
		log.info("Processing request for product Id : {}", id);
		//Fetching request from cache
		Product product = dataCacheProcessor.getFromCache(id);
		
		//If the product is in data cache then returning the product
		if(product!=null) {
			log.info("Product found in cache : {}", product);
			return CompletableFuture.completedFuture(getProductResponseModel(product));
		}
		
		//Fetching the product from DB
		ProductResponseModel responseModel = persistenceProcessor.read(id);
		
		//Adding product to cache
		dataCacheProcessor.addToCache(responseModel.getUuid(), responseModel.getProduct());
		
		return CompletableFuture.completedFuture(responseModel);
	}

	/**
	 * This method reads all entities so far into DB. Then it updates all entities into cache
	 */
	@Async
	public CompletableFuture<List<ProductResponseModel>> readAll() {
		log.info("Processing request for all products");
		List<ProductResponseModel> responseList = persistenceProcessor.readAll();
		/*
		 * Bellow stream operation converts product response objects to product objects and adds them into cache
		 * then it adds HATEOS links to response object
		 */
		responseList.parallelStream()											//Creating parallel stream
					.map(t->{t.addLink(); return t;}) 							//Adding the HATEOS link
					.map(t->t.getProduct())		   								//Converting to product
					.forEach(t->dataCacheProcessor.addToCache(t.getUuid(), t));	// adding product to cache
		
		return CompletableFuture.completedFuture(responseList);
	}
	
	/**
	 * Method to get count of total Products created and total products updated for given given date
	 */
	@Async
	public CompletableFuture<DayWiseStatisticResponse> getStatForDay(Date date) {
		log.info("Processing request to get stats for date : {}", date);
		return CompletableFuture.completedFuture(persistenceProcessor.stat(date));
	}
	
	//MEthod to convert Product to ProductResponseModel
	private ProductResponseModel getProductResponseModel(Product product) {
		ProductResponseModel pr = new ProductResponseModel();
		pr.setUuid(product.getUuid());
		pr.setName(product.getName());
		pr.setDescription(product.getDescription());
		pr.setProvider(product.getProvider());
		pr.setIsAvailable(product.getIsAvailable());
		pr.setMeasurementUnit(product.getMeasurementUnit());
		pr.setCreateDate(product.getCreateDate());
		pr.setLastUpdateDate(product.getLastUpdateDate());
		pr.setSeqNo(product.getSeqNo());
		return pr;
	}
}

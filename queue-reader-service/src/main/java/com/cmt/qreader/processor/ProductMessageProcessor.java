package com.cmt.qreader.processor;

import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.cmt.qreader.provicer.PersistenceServiceProvider;
import com.cmt.qreader.provicer.model.response.ProductResponseModel;

import lombok.extern.slf4j.Slf4j;
import pojo.Product;

/**
 * This class is a layer between mq message receiving and sending the data to db.
 * It does following things
 * 1) Does the analysis on product data if needed
 * 2) sends the data to persistence service
 * 3) processes the db response
 * @author Manoo.Srivastav
 */
@Slf4j
@Component("productMessageProcessor")
public class ProductMessageProcessor implements BaseProcessor {
	
	@Autowired
	@Qualifier("persistenceServiceProvider")
	private PersistenceServiceProvider dataServiceProvider;
	
	@Autowired
	@Qualifier("productRequestCache")
	Cache<String, String> productRequestCache;
	
	@Override
	public void process(Object object) {
		log.info("Starting product object processing");
		Product product = (Product) object;
		
		//Before sending request to DB service, same request will be checked in cache
		if(productRequestCache.containsKey(product.toString())) {
			//If this request is already in cache then, it will not be sent further
			log.info("Request already processed product. Not calling DB service : " + product);
			return;
		}
		
		log.info("Request not in cache. Hitting create product service of DB");
		ProductResponseModel responseEntity = dataServiceProvider.create(product);
		log.info("Response from DB service for product creation : {}", responseEntity);
		//If responseEntity is null, then something went wrong with calling db service
		if(responseEntity==null) return;
		
		//If fields of product are not same with response from db service, then it is the case of update
		if(!compareProductWithServiceResponse(product, responseEntity)) {
			log.info("Response from create service and original product requested are not same. This is case of update.");
			//Sending update request
			ProductResponseModel updateResponseEntity = dataServiceProvider.update(product);
			log.info("Response from DB service for update entity : {}",updateResponseEntity);
			if(updateResponseEntity==null) return;
			//In case of update, old entity should be evicted from the cach
			//In case of update, old product should be removed from cache
			Product oldProduct = Product.builder()
										.uuid(responseEntity.getUuid())
										.name(responseEntity.getName())
										.description(responseEntity.getDescription())
										.provider(responseEntity.getProvider())
										.isAvailable(responseEntity.getIsAvailable())
										.measurementUnit(responseEntity.getMeasurementUnit()).build();
			productRequestCache.remove(oldProduct.toString());
		}
		
		//If all goes well, adding the object to cache
		productRequestCache.put(product.toString(),"");
	}
	
	
	/*
	 * This method returns true if all the fields of original product and product response are same
	 * @param product
	 * @param productResponseModel
	 * @return
	 */
	private boolean compareProductWithServiceResponse(Product p, ProductResponseModel prm) {
		return   p.getUuid().equalsIgnoreCase(prm.getUuid())
			  && p.getName().equalsIgnoreCase(prm.getName())
			  && p.getDescription().equalsIgnoreCase(prm.getDescription())
			  && p.getProvider().equalsIgnoreCase(prm.getProvider())
			  && p.getIsAvailable().equals(prm.getIsAvailable())
			  && p.getMeasurementUnit().equalsIgnoreCase(prm.getMeasurementUnit());
	}

}

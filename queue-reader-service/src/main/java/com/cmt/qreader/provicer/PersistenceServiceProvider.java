package com.cmt.qreader.provicer;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cmt.qreader.provicer.model.request.ProductRequestModel;
import com.cmt.qreader.provicer.model.response.ProductResponseModel;

import lombok.extern.slf4j.Slf4j;
import pojo.Product;

/**
 * This class is responsible for calling DB service and writing the data
 * @author Manoo.Srivastav
 */
@Component("persistenceServiceProvider")
@Slf4j
public class PersistenceServiceProvider {
	
    @Autowired
    RestTemplate restTemplate;

    @Value("${persistence.service.product.create.url}")
    private String createUrl;
    
    @Value("${persistence.service.product.update.url}")
    private String updateUrl;
    
	/**
	 * This method calls the update endpoint of db service to create data
	 * @param product
	 * @return response model
	 */
	public ProductResponseModel create(Product product) {
		log.info("Sending create request for product : " + product);
		ProductRequestModel request = ProductRequestModel.builder()
														 .uuid(product.getUuid())
														 .name(product.getName())
														 .description(product.getDescription())
														 .provider(product.getProvider())
														 .isAvailable(product.getIsAvailable())
														 .measurementUnit(product.getMeasurementUnit()).build();
		try {
			ResponseEntity<ProductResponseModel> responseEntity = restTemplate.postForEntity(createUrl, request, ProductResponseModel.class);
			//If response code is neither OK(200) nor CREATED(201), then above flow will throw exception
			return responseEntity.getBody();
		}
		catch(Exception e) {
			log.error("Product creation failed for product {}, due to reason : {}", product, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method calls the create endpoint of db service to create data
	 * @param product
	 * @return response model
	 */
	public ProductResponseModel update(Product product) {
		log.info("Sending update request for product : " + product);
		ProductRequestModel productRequestModel = ProductRequestModel.builder()
														 .uuid(product.getUuid())
														 .name(product.getName())
														 .description(product.getDescription())
														 .provider(product.getProvider())
														 .isAvailable(product.getIsAvailable())
														 .measurementUnit(product.getMeasurementUnit()).build();
		try {
			RequestEntity<ProductRequestModel> request = RequestEntity.put(URI.create(updateUrl)).body(productRequestModel);
			ResponseEntity<ProductResponseModel> responseEntity = restTemplate.exchange(request, ProductResponseModel.class);
			//If response code is neither OK(200) nor CREATED(201), then above flow will throw exception
			return responseEntity.getBody();
		}
		catch(Exception e) {
			log.error("Product updation failed for product {}, due to reason : {}", product, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}

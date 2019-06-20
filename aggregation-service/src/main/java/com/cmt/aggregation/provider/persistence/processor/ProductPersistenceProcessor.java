package com.cmt.aggregation.provider.persistence.processor;

import java.net.URI;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cmt.aggregation.rest.model.request.Product;
import com.cmt.aggregation.rest.model.request.RequestModel;
import com.cmt.aggregation.rest.model.response.DayWiseStatisticResponse;
import com.cmt.aggregation.rest.model.response.ProductResponseModel;

import lombok.extern.slf4j.Slf4j;

/**
 * This class will provide calls to persistence service
 * @author Manoo.Srivastav
 */
@Component("productPersistenceProcessor")
@Slf4j
public class ProductPersistenceProcessor {

	@Autowired
	@Qualifier("simpleRestTemplate")
	private RestTemplate restTemplate;

	@Value("${persistence.service.product.create.url}")
	private String createUrl;

	@Value("${persistence.service.product.update.url}")
	private String updateUrl;

	@Value("${persistence.service.product.delete.url}")
	private String deleteUrl;

	@Value("${persistence.service.product.read.url}")
	private String readUrl;

	@Value("${persistence.service.product.readall.url}")
	private String readAllUrl;

	@Value("${persistence.service.product.stat.url}")
	private String statUrl;


	public ProductResponseModel read(String id) {
		log.info("Sending read request to persistence service for id : {}", id);

		try {
			ResponseEntity<ProductResponseModel> responseEntity = restTemplate.getForEntity(readUrl, ProductResponseModel.class, id);
			//If response code is not OK(200), then above flow will throw exception
			ProductResponseModel resp = responseEntity.getBody();
			log.info("Read response from persistence layer : {}", resp);
			return resp;
		}
		catch(Exception e) {
			log.error("Call to persistence service for read product failed : {}", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	public List<ProductResponseModel> readAll(){
		log.info("Sending read all request to persistence service");
		try {
			ResponseEntity<List<ProductResponseModel>> responseEntity = restTemplate.exchange(readAllUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductResponseModel>>() {});
			return responseEntity.getBody();
		}
		catch(Exception e) {
			log.error("Call to persistence service for read all products failed : {}", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	public void delete(String id) {
		log.info("Sending delete request to persistence service for id : {}", id);

		try {
				ResponseEntity<String> resp = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, String.class, id);
				//If http status is not OK then there is problem with delete request
				if(!resp.getStatusCode().equals(HttpStatus.OK)) {
					log.info("Problem in deleting :{} - {}", resp.getStatusCode(), resp.getBody());
					throw new RuntimeException("HTTP code -" + resp.getStatusCode() + " : " + resp.getBody());
				}
		}
		catch(Exception e) {
			log.error("Call to persistence service for delete product failed : {}", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * This method calls the update endpoint of db service to create data
	 * @param product
	 * @return response model
	 */
	public ProductResponseModel create(Product product) {
		log.info("Sending create request to persistence service for product : {}", product);
		RequestModel request = createProductRequest(product);
		try {
			ResponseEntity<ProductResponseModel> responseEntity = restTemplate.postForEntity(createUrl, request, ProductResponseModel.class);
			//If response code is neither OK(200) nor CREATED(201), then above flow will throw exception
			ProductResponseModel resp = responseEntity.getBody();
			log.info("Create response from persistence layer : {}", resp);
			return resp;
		}
		catch(Exception e) {
			log.error("Call to persistence service for create product {} failed due to reason : {}", product, e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * This method calls the create endpoint of db service to create data
	 * @param product
	 * @return response model
	 */
	public ProductResponseModel update(Product product) {
		log.info("Sending update request to persistence service for product : {}", product);
		RequestModel productRequestModel = createProductRequest(product);
		try {
			RequestEntity<RequestModel> request = RequestEntity.put(URI.create(updateUrl)).body(productRequestModel);
			ResponseEntity<ProductResponseModel> responseEntity = restTemplate.exchange(request, ProductResponseModel.class);
			//If response code is not OK(200), then above flow will throw exception
			ProductResponseModel resp = responseEntity.getBody();
			log.info("Update response from persistence layer : {}", resp);
			return resp;
		}
		catch(Exception e) {
			log.error("Call to persistence service for update product {} failed due to reason : {}", product, e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * This method calls the method of DB service that generate daily stats
	 * @return
	 */
	public DayWiseStatisticResponse stat(Date date){
		log.info("Sending request to persistence service to get stat for today");
		try {
			java.sql.Date d = new java.sql.Date(date.getTime());
			ResponseEntity<DayWiseStatisticResponse> responseEntity = restTemplate.getForEntity(statUrl, DayWiseStatisticResponse.class, d.toString());
			//If response code is not OK(200), then above flow will throw exception
			DayWiseStatisticResponse resp = responseEntity.getBody();
			log.info("Stat response from persistence layer : {}", resp);
			return resp;
		}
		catch(Exception e) {
			log.error("Call to persistence service for getting stats failed : {}", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	//Method to create product request model from product
	private RequestModel createProductRequest(Product p) {
		RequestModel pr = new RequestModel();
		pr.setUuid(p.getUuid());
		pr.setName(p.getName());
		pr.setDescription(p.getDescription());
		pr.setProvider(p.getProvider());
		pr.setIsAvailable(p.getIsAvailable());
		pr.setMeasurementUnit(p.getMeasurementUnit());
		return pr;
	}
}

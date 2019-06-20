package com.cmt.persistence.rest.endpoints;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmt.persistence.rest.model.request.ProductRequestModel;
import com.cmt.persistence.rest.model.response.DayWiseStatisticResponse;
import com.cmt.persistence.rest.model.response.ProductResponseModel;
import com.cmt.persistence.rest.processor.ProductDataProcessor;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product")
@Validated
@Slf4j
public class ProductController {
	
	@Autowired
	@Qualifier("ProductDataProcessor")
	private ProductDataProcessor productDataProcessor;
	
	@GetMapping(path="/read/{id}", produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ProductResponseModel> read(@PathVariable("id") @NotBlank String id) {
		log.info("Read request for id : " + id);
		return ResponseEntity.ok(productDataProcessor.read(id.toLowerCase()));
	}
	
	@GetMapping(path="/read/all", produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<ProductResponseModel>> readAll(){
		log.info("Read all request");
		return ResponseEntity.ok(productDataProcessor.readAll());
	}
	
	@DeleteMapping(path="/delete/{id}")
	public void delete(@PathVariable("id") @NotBlank String id) {
		log.info("Delete request for id : " + id);
		productDataProcessor.delete(id.toLowerCase());
	}
	
	@PutMapping(path="/update", produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ProductResponseModel> update(@Valid @RequestBody ProductRequestModel request){
		log.info("Update request for product : " + request);
		return ResponseEntity.ok(productDataProcessor.update(request.getProduct()));
	}
	
	@PostMapping(path="/create", produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ProductResponseModel> create(@Valid @RequestBody ProductRequestModel request) {
		log.info("Create request for product : " + request);
		return ResponseEntity.status(HttpStatus.CREATED).body(productDataProcessor.create(request.getProduct())); //HTTP status for user created is 201 
	}
	
	/**
	 * This method returns day wise total product creation and updates
	 */
	// This method is exposed directly at DB layer because, pulling massive date wise data just for doing 
	// one aggregate analysis (count of product update and creation for the day), would have effected service performance significantly for massive data
	@GetMapping(path="/stat/day/{date}", produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<DayWiseStatisticResponse> stat(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") @Past(message="Date can not be in future") Date date){
		log.info("Fetching create and update stat for the date : " + date);
		return ResponseEntity.ok(productDataProcessor.getStatForDay(date));
	}
}

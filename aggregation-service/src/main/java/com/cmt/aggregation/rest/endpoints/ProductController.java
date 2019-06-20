package com.cmt.aggregation.rest.endpoints;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.Resources;
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

import com.cmt.aggregation.rest.deligator.ProductProcessDeligator;
import com.cmt.aggregation.rest.model.request.RequestModel;
import com.cmt.aggregation.rest.model.response.DayWiseStatisticResponse;
import com.cmt.aggregation.rest.model.response.ProductResponseModel;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product")
@Validated
@Slf4j
public class ProductController {
	
	@Autowired
	@Qualifier("productProcessDeligator")
	private ProductProcessDeligator productProcessDeligator;
	
	@GetMapping(path="/read/{id}", produces= {MediaType.APPLICATION_JSON_VALUE,org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE})
	public ResponseEntity<ProductResponseModel> read(@PathVariable("id") @NotBlank String id) {
		log.info("Read request for id : " + id);
		return ResponseEntity.ok(productProcessDeligator.read(id.toLowerCase()));
	}
	
	@GetMapping(path="/read/all", produces= {MediaType.APPLICATION_JSON_VALUE})
	public Resources<ProductResponseModel> readAll(){
		log.info("Read all request");
		List<ProductResponseModel> list = productProcessDeligator.readAll();
		Resources<ProductResponseModel> resources = new Resources<>(list);
		return resources;
	}
	
	@DeleteMapping(path="/delete/{id}")
	public void delete(@PathVariable("id") @NotBlank String id) {
		log.info("Delete request for id : " + id);
		productProcessDeligator.delete(id.toLowerCase());
	}
	
	@PutMapping(path="/update", produces= {MediaType.APPLICATION_JSON_VALUE,org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE})
	public ResponseEntity<ProductResponseModel> update(@Valid @RequestBody RequestModel request){
		log.info("Update request for product : " + request);
		return ResponseEntity.ok(productProcessDeligator.update(request.getProduct()));
	}
	
	@PostMapping(path="/create", produces= {MediaType.APPLICATION_JSON_VALUE,org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE})
	public ResponseEntity<ProductResponseModel> create(@Valid @RequestBody RequestModel request) {
		log.info("Create request for product : " + request);
		return ResponseEntity.status(HttpStatus.CREATED).body(productProcessDeligator.create(request.getProduct())); //HTTP status for user created is 201 
	}
	
	/**
	 * This method returns day wise total product creation and updates
	 */
	@GetMapping(path="/current/stat", produces= {MediaType.APPLICATION_JSON_VALUE,org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE})
	public ResponseEntity<DayWiseStatisticResponse> stat(){
		log.info("Fetching create and update stat for today");
		return ResponseEntity.ok(productProcessDeligator.getStatForDay());
	}
}

package com.cmt.qreader.provicer.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

/**
 * This request object is to used to send request to persistence layer
 * @author Manoo.Srivastav
 *
 */
@Builder
@Value
public class ProductRequestModel{
	
	@JsonProperty("id")
	private String uuid;
	
	private String name;
	
	private String description;
	
	private String provider;
	
	private Boolean isAvailable;
	
	private String measurementUnit;
	
}

package com.cmt.qreader.provicer.model.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductResponseModel {
	
	@JsonProperty("id")
	private String uuid;
	
	private String name;
	
	private String description;
	
	private String provider;
	
	private Boolean isAvailable;
	
	private String measurementUnit;
	
	private Date createDate;
	
	private Date lastUpdateDate;
	
	private Long seqNo;
}

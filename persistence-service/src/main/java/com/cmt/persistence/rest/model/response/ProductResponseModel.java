package com.cmt.persistence.rest.model.response;

import java.sql.Date;

import org.springframework.hateoas.ResourceSupport;

import com.cmt.persistence.data.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductResponseModel extends ResourceSupport {
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
		
	public ProductResponseModel(Product product) {
		this.uuid = product.getUuid();
		this.name = product.getName();
		this.description = product.getDescription();
		this.provider = product.getProvider();
		this.isAvailable = product.getIsAvailable();
		this.measurementUnit = product.getMeasurementUnit();
		this.createDate = product.getCreateDate();
		this.lastUpdateDate = product.getLastUpdateDate();
		this.seqNo = product.getSeqNo();
	}
}

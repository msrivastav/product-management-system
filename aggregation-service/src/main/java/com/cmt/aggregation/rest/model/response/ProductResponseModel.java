package com.cmt.aggregation.rest.model.response;

import java.util.Date;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import com.cmt.aggregation.rest.endpoints.ProductController;
import com.cmt.aggregation.rest.model.request.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
	
	public ProductResponseModel() {
		addLink();
	}
	
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
		
		addLink();
	}
	
	public void addLink() {
	    Link selfLink = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ProductController.class, String.class).read(uuid)).withSelfRel();
		add(selfLink);
	}
	
	@JsonIgnore
	public Product getProduct() {
			Product p = new Product();
			p.setUuid(uuid.toLowerCase());
			p.setName(name);
			p.setDescription(description);
			p.setProvider(provider);
			p.setIsAvailable(isAvailable);
			p.setMeasurementUnit(measurementUnit);
			p.setCreateDate(createDate);
			p.setLastUpdateDate(lastUpdateDate);
			p.setSeqNo(seqNo);
			return p;
					  
	}
}

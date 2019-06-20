package com.cmt.persistence.rest.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.ResourceSupport;

import com.cmt.persistence.data.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This request object is to provide a layer between persistence and response.
 * This gives convenient way add or remove fields in response object
 * @author Manoo.Srivastav
 *
 */
@Getter
@Setter
@ToString
public class ProductRequestModel extends ResourceSupport {
	
	@JsonProperty("id")
	@NotBlank(message="Id can not be blank")
	private String uuid;
	
	@NotBlank(message="Name can not be blank")
	private String name;
	
	@NotBlank(message="Description can not be blank")
	private String description;
	
	@NotBlank(message="Provider can not be blank")
	private String provider;
	
	@NotNull(message="Available flag can not be blank")
	private Boolean isAvailable;
	
	@NotBlank(message="Measurement Unit can not be blank")
	private String measurementUnit;
	
	/**
	 * This method creates a product object with same data as its field
	 * @return Product
	 * @author Manoo.Srivastav
	 */
	public Product getProduct() {
		Product product = new Product();
		product.setUuid(uuid.toLowerCase()); //uuid is case insensitive, I am keeping it lower case
		product.setDescription(description);
		product.setIsAvailable(isAvailable);
		product.setMeasurementUnit(measurementUnit);
		product.setName(name);
		product.setProvider(provider);
		return product;
	}
}

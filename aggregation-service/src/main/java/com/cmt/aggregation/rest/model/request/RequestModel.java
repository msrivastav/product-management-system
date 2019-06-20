
package com.cmt.aggregation.rest.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.ResourceSupport;

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
public class RequestModel extends ResourceSupport {
	
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
		Product p = new Product();
		p.setUuid(uuid.toLowerCase());
		p.setName(name);
		p.setDescription(description);
		p.setProvider(provider);
		p.setIsAvailable(isAvailable);
		p.setMeasurementUnit(measurementUnit);
		return p;
	}
}
package pojo;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Product implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String uuid;
	
	private String name;
	
	private String description;
	
	private String provider;
	
	private Boolean isAvailable;
	
	private String measurementUnit;
		
}

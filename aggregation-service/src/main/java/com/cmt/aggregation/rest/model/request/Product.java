package com.cmt.aggregation.rest.model.request;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public String uuid;
	
	public String name;
	
	public String description;
	
	public String provider;
	
	public Boolean isAvailable;
	
	public String measurementUnit;
	
	@EqualsAndHashCode.Exclude public Date createDate;
	
	@EqualsAndHashCode.Exclude public Date lastUpdateDate;
	
	@EqualsAndHashCode.Exclude public Long seqNo;
}

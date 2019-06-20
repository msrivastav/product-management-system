package com.cmt.persistence.data.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Table(name="DELETE_PRODUCT")
public class DeleteProduct {
	/*
	 * This field must have numeric id as product with same uuid can be created and deleted repeatedly
	 */
	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="SEQ_NO")
	private Long seqNo;
	
	@Getter
	@Setter
	@Column(name="UUID", nullable=false, unique=true)
	private String uuid;
	
	@Getter
	@Setter
	@Column(name="NAME")
	private String name;
	
	@Getter
	@Setter
	@Column(name="DESCRIPTION")
	private String description;
	
	@Getter
	@Setter
	@Column(name="PROVIDER")
	private String provider;
	
	@Getter
	@Setter
	@Column(name="IS_AVAILABLE")
	private Boolean isAvailable;
	
	@Getter
	@Setter
	@Column(name="MEASUREMENT_UNIT")
	private String measurementUnit;
	
	@Getter
	@Column(name="DELETE_TIMESTAMP")
	@CreationTimestamp
	private LocalDateTime deleteTimestamp;
	
	public void fromProduct(Product product) {
		this.uuid = product.getUuid();
		this.name = product.getName();
		this.description = product.getName();
		this.provider = product.getProvider();
		this.isAvailable = product.getIsAvailable();
		this.measurementUnit = product.getMeasurementUnit();
	}
}

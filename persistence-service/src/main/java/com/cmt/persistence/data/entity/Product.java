package com.cmt.persistence.data.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Table(name="PRODUCT")
public class Product {
	
	@Getter
	@Setter
	@Id
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
	@Setter
	@Column(name="CREATION_DATE")
	@CreationTimestamp
	private Date createDate;
	
	@Getter
	@Setter
	@Column(name="LAST_UPDATE_DATE", nullable=true)
	private Date lastUpdateDate;
	
	/*
	 * This field is just to maintain order in the table. This will help in operations such as pagination
	 */
	@Getter
	@Setter
	@Generated(value=GenerationTime.INSERT)
	@Column(name="SEQ_NO")
	private Long seqNo;
	
}

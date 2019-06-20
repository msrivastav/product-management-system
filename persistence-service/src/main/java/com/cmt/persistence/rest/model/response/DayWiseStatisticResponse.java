package com.cmt.persistence.rest.model.response;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This is response object for fetching the count of total products created and updated for the day
 * @author Manoo.Srivastav
 */
@Getter
@Setter
@ToString
@Builder
public class DayWiseStatisticResponse {
	
	private Date date;
	
	private Long productCreatedCount;
	
	private Long productUpdatedCount;
}

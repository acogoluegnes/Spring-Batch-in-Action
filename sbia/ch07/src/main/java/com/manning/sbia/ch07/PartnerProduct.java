/**
 * 
 */
package com.manning.sbia.ch07;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author acogoluegnes
 *
 */
public class PartnerProduct {

	private String id;
	
	private String title;
	
	private String details;
	
	private BigDecimal price;
	
	private Date releaseDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
}

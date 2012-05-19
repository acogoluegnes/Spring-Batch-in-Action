/**
 *
 */
package com.manning.sbia.ch13.domain;

import java.io.Serializable;

/**
 * @author templth
 *
 */
public class ProductForColumnRange implements Serializable {
	private int id;
	private String name;
	private String description;
	private float price;

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public float getPrice() { return price; }
	public void setPrice(float price) { this. price = price; }
}


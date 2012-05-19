/**
 *
 */
package com.manning.sbia.ch13.domain;

/**
 * @author templth
 *
 */
public class MobilePhoneProduct extends Product {
	private String manufacturer;

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}
}


/**
 *
 */
package com.manning.sbia.ch13.domain;

/**
 * @author templth
 *
 */
public class BookProduct extends Product {
	private String publisher;

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublisher() {
		return this.publisher;
	}
}


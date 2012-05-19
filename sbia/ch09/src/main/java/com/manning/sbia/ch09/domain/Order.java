/**
 * 
 */
package com.manning.sbia.ch09.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author acogoluegnes
 *
 */
public class Order implements Serializable {

	private String orderId;
	
	private List<OrderItem> items;

	public Order(String orderId, List<OrderItem> items) {
		super();
		this.orderId = orderId;
		this.items = Collections.unmodifiableList(items);
	}
	
	public String getOrderId() {
		return orderId;
	}
	
	public List<OrderItem> getItems() {
		return items;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		return true;
	}
	
	
	
}

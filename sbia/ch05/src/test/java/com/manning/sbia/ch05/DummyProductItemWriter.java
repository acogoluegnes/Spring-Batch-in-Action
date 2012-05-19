/**
 * 
 */
package com.manning.sbia.ch05;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.manning.sbia.ch05.Product;

/**
 * @author acogoluegnes
 *
 */
public class DummyProductItemWriter implements ItemWriter<Product> {
	
	public List<Product> products = new ArrayList<Product>();

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(List<? extends Product> items) throws Exception {
		//System.out.println("items = "+items.size());
		products.addAll(items);
	}

	public List<Product> getProducts() {
		return products;
	}
}

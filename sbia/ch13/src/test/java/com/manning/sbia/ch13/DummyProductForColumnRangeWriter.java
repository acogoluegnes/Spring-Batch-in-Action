/**
 * 
 */
package com.manning.sbia.ch13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.manning.sbia.ch13.ThreadUtils;
import com.manning.sbia.ch13.domain.Product;
import com.manning.sbia.ch13.domain.ProductForColumnRange;

/**
 * @author acogoluegnes
 *
 */
public class DummyProductForColumnRangeWriter implements ItemWriter<ProductForColumnRange> {
	
	private List<ProductForColumnRange> products = new ArrayList<ProductForColumnRange>();

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(List<? extends ProductForColumnRange> items) throws Exception {
		ThreadUtils.writeThreadExecutionMessage("write", items);
		for(ProductForColumnRange product : items) {
			processProduct(product);
		}
	}

	private void processProduct(ProductForColumnRange product) throws InterruptedException {
		Thread.sleep(5);
		synchronized(products) {
			products.add(product);
		}
	}
	
	public List<ProductForColumnRange> getProducts() {
		return Collections.unmodifiableList(products);
	}
	
	public void clear() {
		products.clear();
	}

}

/**
 * 
 */
package com.manning.sbia.ch07;


import org.apache.commons.lang.math.NumberUtils;
import org.springframework.batch.item.ItemProcessor;

import com.manning.sbia.ch01.domain.Product;

/**
 * @author acogoluegnes
 *
 */
public class FilteringProductItemProcessor implements
		ItemProcessor<Product, Product> {

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public Product process(Product item) throws Exception {
		return needsToBeFiltered(item) ? null : item;
	}
	
	private boolean needsToBeFiltered(Product item) {
		String id = item.getId();
		String lastDigit = id.substring(id.length()-1, id.length());
		if(NumberUtils.isDigits(lastDigit)) {
			return NumberUtils.toInt(lastDigit) % 2 == 1;
		} else {
			return false;
		}		 
	}

}

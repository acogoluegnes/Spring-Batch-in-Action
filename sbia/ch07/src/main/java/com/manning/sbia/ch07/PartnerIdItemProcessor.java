/**
 * 
 */
package com.manning.sbia.ch07;

import org.springframework.batch.item.ItemProcessor;

import com.manning.sbia.ch01.domain.Product;

/**
 * @author acogoluegnes
 *
 */
public class PartnerIdItemProcessor implements
		ItemProcessor<Product, Product> {

	private PartnerIdMapper mapper;
	
	@Override
	public Product process(Product item) throws Exception {		
		return mapper.map(item);
	}
	
	public void setMapper(PartnerIdMapper mapper) {
		this.mapper = mapper;
	}
	
}

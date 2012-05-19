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
public class PartnerProductItemProcessor implements
		ItemProcessor<PartnerProduct, Product> {
	
	private PartnerProductMapper mapper;

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public Product process(PartnerProduct item) throws Exception {
		return mapper.map(item);
	}
	
	public void setMapper(PartnerProductMapper mapper) {
		this.mapper = mapper;
	}

}

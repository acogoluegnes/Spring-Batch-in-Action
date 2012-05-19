/**
 * 
 */
package com.manning.sbia.ch07;

import com.manning.sbia.ch01.domain.Product;

/**
 * @author acogoluegnes
 *
 */
public class SimplePartnerProductMapper implements PartnerProductMapper {

	/* (non-Javadoc)
	 * @see com.manning.sbia.ch08.PartnerProductMapper#map(com.manning.sbia.ch08.PartnerProduct)
	 */
	@Override
	public Product map(PartnerProduct partnerProduct) {
		Product product = new Product();
		product.setId(partnerProduct.getId());
		product.setName(partnerProduct.getTitle());
		product.setDescription(partnerProduct.getDetails());
		product.setPrice(partnerProduct.getPrice());
		return product;
	}

}

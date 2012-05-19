/**
 * 
 */
package com.manning.sbia.ch07;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;



/**
 * @author acogoluegnes
 *
 */
public class PartnerProductFieldSetMapper implements FieldSetMapper<PartnerProduct> {
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapFieldSet(org.springframework.batch.item.file.transform.FieldSet)
	 */
	public PartnerProduct mapFieldSet(FieldSet fieldSet) throws BindException {
		PartnerProduct product = new PartnerProduct();
		product.setId(fieldSet.readString("PRODUCT_ID"));
		product.setTitle(fieldSet.readString("TITLE"));
		product.setDetails(fieldSet.readString("DETAILS"));
		product.setPrice(fieldSet.readBigDecimal("PRICE"));
		product.setReleaseDate(fieldSet.readDate("RELEASE_DATE"));
		return product;
	}
	
}

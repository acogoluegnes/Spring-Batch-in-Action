/**
 * 
 */
package com.manning.sbia.ch13;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.manning.sbia.ch13.domain.BookProduct;
import com.manning.sbia.ch13.domain.MobilePhoneProduct;
import com.manning.sbia.ch13.domain.Product;

/**
 * @author templth
 *
 */
public class ProductFieldSetMapper implements FieldSetMapper<Product> {
	public Product mapFieldSet(FieldSet fieldSet) {
		String id = fieldSet.readString("id");
		Product product = null;
		if (id.startsWith("PRB")) {
			product = new BookProduct();
		} else if (id.startsWith("PRM")) {
			product = new MobilePhoneProduct();
		} else {
			product = new Product();
		}
		product.setId(id);
		product.setName(fieldSet.readString("name"));
		product.setDescription(fieldSet.readString("description"));
		product.setPrice(fieldSet.readFloat("price"));
		return product;
	}
}


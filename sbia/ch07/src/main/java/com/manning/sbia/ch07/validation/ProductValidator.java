/**
 * 
 */
package com.manning.sbia.ch07.validation;

import java.math.BigDecimal;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import com.manning.sbia.ch01.domain.Product;

/**
 * @author acogoluegnes
 *
 */
public class ProductValidator implements Validator<Product> {

	@Override
	public void validate(Product product) throws ValidationException {
		if(BigDecimal.ZERO.compareTo(product.getPrice()) >= 0) {
			throw new ValidationException("Product price cannot be negative!");
		}		
	}

}

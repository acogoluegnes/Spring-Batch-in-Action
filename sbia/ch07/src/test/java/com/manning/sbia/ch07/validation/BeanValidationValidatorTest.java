/**
 * 
 */
package com.manning.sbia.ch07.validation;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.item.validator.ValidationException;

import com.manning.sbia.ch01.domain.Product;
import com.manning.sbia.ch07.validation.BeanValidationValidator;

/**
 * @author acogoluegnes
 *
 */
public class BeanValidationValidatorTest {
	
	private BeanValidationValidator<Object> validator = new BeanValidationValidator<Object>();
	
	private Product product = new Product();
	
	private Order order = new Order();

	@Test public void validateAnnotatedOk() {
		product.setPrice(BigDecimal.TEN);
		validator.validate(product);
	}
	
	@Test public void validateAnnotatedNOk() {
		product.setPrice(BigDecimal.TEN.negate());
		try {
			validator.validate(product);
			Assert.fail("price is 0, validation should have failed");
		} catch (ValidationException e) {
			// OK
		}
	}
	
	@Test public void validateNotAnnotated() {
		validator.validate(order);
	}
	
}

/**
 *
 */
package com.manning.sbia.ch14.batch.unit.validation;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.validator.ValidationException;

import com.manning.sbia.ch14.batch.validation.PositivePriceValidator;
import com.manning.sbia.ch14.domain.Product;

/**
 * Unit test.
 *
 * @author bazoud
 *
 */
public class PositivePriceValidatorTest {
  private PositivePriceValidator validator;
  private Product product;

  @Before
  public void setUp() {
    validator = new PositivePriceValidator();
    product = new Product();
  }

  @Test
  public void testPositivePrice() {
    product.setPrice(new BigDecimal("100.0"));
    validator.validate(product);
  }

  @Test(expected = ValidationException.class)
  public void testZeroPrice() {
    product.setPrice(new BigDecimal("0.0"));
    validator.validate(product);
  }

  @Test(expected = ValidationException.class)
  public void testNegativePrice() {
    product.setPrice(new BigDecimal("-800.0"));
    validator.validate(product);
  }
}

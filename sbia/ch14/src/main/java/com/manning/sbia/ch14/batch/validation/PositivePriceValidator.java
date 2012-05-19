/**
 *
 */
package com.manning.sbia.ch14.batch.validation;

import java.math.BigDecimal;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import com.manning.sbia.ch14.domain.Product;

/**
 * @author bazoud
 *
 */
public class PositivePriceValidator implements Validator<Product> {
  @Override
  public void validate(Product product) throws ValidationException {
    if (BigDecimal.ZERO.compareTo(product.getPrice()) >= 0) {
      throw new ValidationException("Product price cannot be 0 or negative!");
    }
  }
}

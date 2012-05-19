/**
 *
 */
package com.manning.sbia.ch14.batch.validation;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import com.manning.sbia.ch14.domain.Product;

/**
 * @author bazoud
 *
 */
public class PriceMandatoryValidator implements Validator<Product> {
  @Override
  public void validate(Product product) throws ValidationException {
    if (product.getPrice() == null) {
      throw new ValidationException("Product price is mandatory !");
    }
  }
}

/**
 *
 */
package com.manning.sbia.ch14.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.manning.sbia.ch14.domain.Product;

/**
 * @author bazoud
 *
 */
public class ProductFieldSetMapper implements FieldSetMapper<Product> {
  public static final String FIELD_ID = "ID";
  public static final String FIELD_NAME = "NAME";
  public static final String FIELD_DESCRIPTION = "DESCRIPTION";
  public static final String FIELD_PRICE = "PRICE";

  @Override
  public Product mapFieldSet(FieldSet fieldSet) throws BindException {
    Product product = new Product();
    product.setId(fieldSet.readString(FIELD_ID));
    product.setName(fieldSet.readString(FIELD_NAME));
    product.setDescription(fieldSet.readString(FIELD_DESCRIPTION));
    product.setPrice(fieldSet.readBigDecimal(FIELD_PRICE));
    return product;
  }
}

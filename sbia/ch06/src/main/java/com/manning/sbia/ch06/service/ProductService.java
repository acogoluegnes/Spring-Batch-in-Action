package com.manning.sbia.ch06.service;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.manning.sbia.ch06.Product;

/**
 * @author bazoud
 */
public class ProductService {
  protected static final Log LOGGER = LogFactory.getLog(ProductService.class);

  public void write(Product product) {
    LOGGER.warn(String.format("Use existing service method, writing product id %s", product.getId()));
  }

  public void write(String id, String name, String description, BigDecimal price) {
    LOGGER.warn(String.format("Use existing service method, writing product id %s, name %s", id, name));
  }
}

/**
 *
 */
package com.manning.sbia.ch14.batch;

import java.util.Arrays;

import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Required;

import com.manning.sbia.ch14.domain.Product;

/**
 * @author bazoud
 *
 */
public class ProductItemListener extends ItemListenerSupport<Product,Product> {
  private FlatFileItemWriter<Product> excludeWriter;

  @Override
  public void afterProcess(Product item, Product result) {
    if (result == null) {
      try {
        excludeWriter.write(Arrays.asList(item));
      } catch (Exception e) {
      }
    }
  }

  @Required
  public void setExcludeWriter(FlatFileItemWriter<Product> excludeWriter) {
    this.excludeWriter = excludeWriter;
  }
}

/**
 *
 */
package com.manning.sbia.ch14.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.manning.sbia.ch14.domain.Product;

/**
 *
 * @author bazoud
 *
 */
public class ProductItemWriter implements ItemWriter<Product> {
  public static final String INSERT_SQL = "INSERT INTO PRODUCT (ID, NAME, DESCRIPTION, PRICE) VALUES (:id, :name, :description, :price)";
  public static final String UPDATE_SQL = "UPDATE PRODUCT SET NAME=:name, DESCRIPTION=:description, PRICE=:price WHERE ID=:id";
  private ItemSqlParameterSourceProvider<Product> itemSqlParameterSourceProvider;
  private SimpleJdbcTemplate simpleJdbcTemplate;

  @Required
  public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
    this.simpleJdbcTemplate = simpleJdbcTemplate;
  }

  @Required
  public void setItemSqlParameterSourceProvider(
      ItemSqlParameterSourceProvider<Product> itemSqlParameterSourceProvider) {
    this.itemSqlParameterSourceProvider = itemSqlParameterSourceProvider;
  }

  @Override
  public void write(List<? extends Product> items) throws Exception {
    for (Product item : items) {
      SqlParameterSource args = itemSqlParameterSourceProvider
          .createSqlParameterSource(item);
      int updated = simpleJdbcTemplate.update(UPDATE_SQL, args);
      if (updated == 0) {
        simpleJdbcTemplate.update(INSERT_SQL, args);
      }
    }
  }

}

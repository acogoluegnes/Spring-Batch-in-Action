/**
 * 
 */
package com.manning.sbia.ch07;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

import com.manning.sbia.ch01.domain.Product;

/**
 * @author acogoluegnes
 *
 */
public class ExistingProductFilterItemProcessor implements
		ItemProcessor<Product, Product> {
	
	private static final String SQL_COUNT_PRODUCT = "select count(1) from product where id = ?";
	
	private JdbcTemplate jdbcTemplate;

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public Product process(Product item) throws Exception {
		return needsToBeFiltered(item) ? null : item;
	}

	private boolean needsToBeFiltered(Product item) {
		return jdbcTemplate.queryForInt(SQL_COUNT_PRODUCT, item.getId()) != 0;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
}

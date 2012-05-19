/**
 * 
 */
package com.manning.sbia.ch07;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.manning.sbia.ch01.domain.Product;

/**
 * @author acogoluegnes
 *
 */
public class PartnerIdMapper {
	
	private static final String SQL_SELECT_STORE_PRODUCT_ID = "select store_product_id from partner_mapping where partner_id = ? and partner_product_id= ?";

	private String partnerId;
	
	private JdbcTemplate jdbcTemplate;

	public Product map(Product partnerProduct) {
		String storeProductId = jdbcTemplate.queryForObject(
			SQL_SELECT_STORE_PRODUCT_ID,
			String.class,
			partnerId,partnerProduct.getId()	
		);
		partnerProduct.setId(storeProductId);
		return partnerProduct;
	}
	
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
}

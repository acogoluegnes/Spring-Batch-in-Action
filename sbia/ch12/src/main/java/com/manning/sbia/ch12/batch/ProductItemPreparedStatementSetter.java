package com.manning.sbia.ch12.batch;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import com.manning.sbia.ch12.domain.Product;

public class ProductItemPreparedStatementSetter implements ItemPreparedStatementSetter<Product> {

	public void setValues(Product product, PreparedStatement ps) throws SQLException {
		ps.setString(1, product.getId());
		ps.setString(2, product.getName());
		ps.setString(3, product.getDescription());
		ps.setFloat(4, product.getPrice());
	}

}

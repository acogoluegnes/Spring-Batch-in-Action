package com.manning.sbia.ch13.partition;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.manning.sbia.ch13.domain.ProductForColumnRange;

public class ProductForColumnRangeRowMapper implements RowMapper<ProductForColumnRange> {
	public ProductForColumnRange mapRow(ResultSet rs, int rowNum) throws SQLException {
	    ProductForColumnRange product = new ProductForColumnRange();
	    product.setId(rs.getInt("id"));
	    product.setName(rs.getString("name"));
	    product.setDescription(rs.getString("description"));
	    product.setPrice(rs.getFloat("price"));
	    return product;
	}
}
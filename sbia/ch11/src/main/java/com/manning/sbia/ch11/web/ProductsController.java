/**
 * 
 */
package com.manning.sbia.ch11.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manning.sbia.ch01.domain.Product;

/**
 * @author acogoluegnes
 *
 */
@Controller
public class ProductsController {
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public ProductsController(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@RequestMapping(value="/products",method=RequestMethod.GET)
	@ResponseBody
	public List<Product> get() {
		return jdbcTemplate.query(
				"select id,name,description,price from product order by id",
				new ProductRowMapper()
		);
	}
	
	private static class ProductRowMapper implements RowMapper<Product> {

		/*
		 * (non-Javadoc)
		 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
		 */
		@Override
		public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
			Product product = new Product();
			product.setId(rs.getString("id"));
			product.setName(rs.getString("name"));
			product.setDescription(rs.getString("description"));
			product.setPrice(rs.getBigDecimal("price"));
			return product;
		}
		
	}
	
}

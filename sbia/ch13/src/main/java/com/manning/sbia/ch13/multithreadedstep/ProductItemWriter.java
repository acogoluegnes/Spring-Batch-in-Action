package com.manning.sbia.ch13.multithreadedstep;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.manning.sbia.ch13.ThreadUtils;
import com.manning.sbia.ch13.domain.Product;

public class ProductItemWriter extends JdbcDaoSupport implements
		ItemWriter<Product> {

	public void write(List<? extends Product> items) throws Exception {
		ThreadUtils.writeThreadExecutionMessage("write", items);
		for (Product product : items) {
			getJdbcTemplate().update("update product set processed=? where id=?",
					true, product.getId());
			// Writing then the product content
		}
	}

}
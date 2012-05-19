/**
 * 
 */
package com.manning.sbia.ch05.file;

import java.util.Map;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.JsonLineMapper;

import com.manning.sbia.ch05.Product;

/**
 * @author templth
 *
 */
public class WrappedJsonLineMapper implements LineMapper<Product> {
	private JsonLineMapper delegate;

	public Product mapLine(String line, int lineNumber) throws Exception {
  		  Map<String,Object> productAsMap
  		                 = delegate.mapLine(line, lineNumber);

		Product product = new Product();
		product.setId((String)productAsMap.get("id"));
		product.setName((String)productAsMap.get("name"));
		product.setDescription((String)productAsMap.get("description"));
		product.setPrice(new Float((Double)productAsMap.get("price")));

		return product;
	}

	public void setDelegate(JsonLineMapper delegate) {
		this.delegate = delegate;
	}
}


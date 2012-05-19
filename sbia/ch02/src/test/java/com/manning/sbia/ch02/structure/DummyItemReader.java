/**
 * 
 */
package com.manning.sbia.ch02.structure;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.manning.sbia.ch01.domain.Product;

/**
 * @author acogoluegnes
 *
 */
public class DummyItemReader implements ItemReader<Product> {
	
	Queue<Product> _products = new LinkedBlockingDeque<Product>() {{
		add(new Product("1"));
		add(new Product("2"));
		add(new Product("3"));
		add(new Product("4"));
	}};

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	@Override
	public Product read() throws Exception, UnexpectedInputException,
			ParseException {
		return _products.poll();
	}

}

/**
 * 
 */
package com.manning.sbia.ch13;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.manning.sbia.ch13.ThreadUtils;
import com.manning.sbia.ch13.domain.Product;

/**
 * @author acogoluegnes
 *
 */
public class DummyProductReader implements ItemReader<Product> {
	
	private AtomicInteger count = new AtomicInteger(0);
	
	private Integer max = 100;
	
	

	public DummyProductReader(Integer max) {
		super();
		this.max = max;
	}



	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	@Override
	public Product read() throws Exception, UnexpectedInputException,
			ParseException {
		synchronized(count) {
			if(count.incrementAndGet() <= max) {
				Product product = new Product(String.valueOf(count.get()));
				ThreadUtils.writeThreadExecutionMessage("read", product);
				return product;
			} else {
				return null;
			}
		}
		
	}

}

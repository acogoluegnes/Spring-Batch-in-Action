package com.manning.sbia.ch13.multithreadedstep;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;

import com.manning.sbia.ch13.ThreadUtils;
import com.manning.sbia.ch13.domain.Product;

public class SynchronizingItemReader implements ItemReader<Product>, ItemStream {
	private ItemReader<Product> delegate;

	public synchronized Product read() throws Exception {
		Product product = delegate.read();
		ThreadUtils.writeThreadExecutionMessage("read", product);
		return product;
	}

	public ItemReader<Product> getDelegate() {
		return delegate;
	}

	public void setDelegate(ItemReader<Product> delegate) {
		this.delegate = delegate;
	}

	// Stream
	
	public void close() throws ItemStreamException {
		if (this.delegate instanceof ItemStream) {
			((ItemStream)this.delegate).close();
		}
	}

	public void open(ExecutionContext context) throws ItemStreamException {
		if (this.delegate instanceof ItemStream) {
			((ItemStream)this.delegate).open(context);
		}
	}

	public void update(ExecutionContext context) throws ItemStreamException {
		if (this.delegate instanceof ItemStream) {
			((ItemStream)this.delegate).update(context);
		}
	}

}

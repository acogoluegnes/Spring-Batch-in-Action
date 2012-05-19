/**
 * 
 */
package com.manning.sbia.ch04.stop;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

/**
 * @author acogoluegnes
 *
 */
public class EmptyWriter implements ItemWriter<String> {

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(List<? extends String> in) throws Exception {

	}

}

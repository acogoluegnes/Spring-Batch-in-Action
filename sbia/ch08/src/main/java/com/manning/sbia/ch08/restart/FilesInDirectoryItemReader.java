/**
 * 
 */
package com.manning.sbia.ch08.restart;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * @author acogoluegnes
 *
 */
public class FilesInDirectoryItemReader implements ItemReader<File>, ItemStream {
	
	private File [] files;
	
	private int currentCount;
	
	private String key = "file.in.directory.count";

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemStream#open(org.springframework.batch.item.ExecutionContext)
	 */
	@Override
	public void open(ExecutionContext executionContext)
			throws ItemStreamException {
		currentCount = executionContext.getInt(key, 0);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemStream#update(org.springframework.batch.item.ExecutionContext)
	 */
	@Override
	public void update(ExecutionContext executionContext)
			throws ItemStreamException {
		executionContext.putInt(key, currentCount);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemStream#close()
	 */
	@Override
	public void close() throws ItemStreamException { }

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	@Override
	public File read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		int index = ++currentCount - 1;
		if(index == files.length) {
			return null;
		}
		return files[index];
	}
	
	public void setDirectory(String directory) {
		this.files = new File(directory).listFiles(
			(FileFilter) FileFilterUtils.fileFileFilter()
		);
		Arrays.sort(files, new NameFileComparator());
	}

}

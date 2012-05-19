/**
 * 
 */
package com.manning.sbia.ch06.custom;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import com.manning.sbia.ch06.Product;

/**
 * @author bazoud
 * 
 */
public class ProductMD5ItemWriter implements ItemWriter<Product>, ItemStream, FlatFileFooterCallback, InitializingBean {
    private ResourceAwareItemWriterItemStream<Product> delegate;
    private List<String> chunkMD5 = new ArrayList<String>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(delegate, "A delegate ItemWriter must be provided.");
    }
    
    public void setDelegate(ResourceAwareItemWriterItemStream<Product> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void write(List<? extends Product> items) throws Exception {
        delegate.write(items);

        StringBuilder builder = new StringBuilder();
        for (Product product : items) {
            builder.append(product.getId());
        }
        chunkMD5.add(DigestUtils.md5DigestAsHex(builder.toString().getBytes("UTF-8")));
        
    }

    @Override
    public void writeFooter(Writer writer) throws IOException {
        for (int i = 0; i < chunkMD5.size(); i++) {
            writer.append(String.format("Chunk %03d> %s", i, chunkMD5.get(i)));
            writer.append(System.getProperty("line.separator"));
        }
    }
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }
    
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }
    
    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }
}

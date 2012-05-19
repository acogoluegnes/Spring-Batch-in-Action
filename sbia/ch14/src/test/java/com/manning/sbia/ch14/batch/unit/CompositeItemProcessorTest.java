/**
 * 
 */
package com.manning.sbia.ch14.batch.unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;

import com.manning.sbia.ch14.batch.validation.PositivePriceValidator;
import com.manning.sbia.ch14.batch.validation.PriceMandatoryValidator;
import com.manning.sbia.ch14.domain.Product;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author bazoud
 * 
 */
public class CompositeItemProcessorTest {
    private CompositeItemProcessor<Product, Product> processor;
    
    @Before
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setUp() {
        List delegates = new ArrayList();
        ValidatingItemProcessor processor1 = new ValidatingItemProcessor();
        processor1.setFilter(true);
        processor1.setValidator(new PriceMandatoryValidator());
        delegates.add(processor1);
        
        ValidatingItemProcessor processor2 = new ValidatingItemProcessor();
        processor2.setFilter(true);
        processor2.setValidator(new PositivePriceValidator());
        delegates.add(processor2);
        
        processor = new CompositeItemProcessor<Product, Product>();
        processor.setDelegates(delegates);    
    }

    @Test
    public void processor() throws Exception {
      Product p1 = new Product();
      p1.setPrice(new BigDecimal(100.0f));
      Product p2 = processor.process(p1);
      assertNotNull(p2);
    }

    @Test
    public void processorFail() throws Exception {
      Product p1 = new Product();
      p1.setPrice(new BigDecimal(-800.0f));
      Product p2 = processor.process(p1);
      assertNull(p2);
    }

    @Test
    public void processorFailOrder() throws Exception {
      Product p1 = new Product();
      Product p2 = processor.process(p1);
      assertNull(p2);
    }

}

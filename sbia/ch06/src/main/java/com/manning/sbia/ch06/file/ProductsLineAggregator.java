/**
 * 
 */
package com.manning.sbia.ch06.file;

import java.util.Map;

import org.springframework.batch.item.file.transform.LineAggregator;

import com.manning.sbia.ch06.Product;

/**
 * @author bazoud
 * 
 */
public class ProductsLineAggregator implements LineAggregator<Product> {
    private Map<Class<LineAggregator<Product>>, LineAggregator<Object>> aggregators;

    @Override
    public String aggregate(Product product) {
        return aggregators.get(product.getClass()).aggregate(product);
    }

    public void setAggregators(Map<Class<LineAggregator<Product>>, LineAggregator<Object>> aggregators) {
        this.aggregators = aggregators;
    }
}

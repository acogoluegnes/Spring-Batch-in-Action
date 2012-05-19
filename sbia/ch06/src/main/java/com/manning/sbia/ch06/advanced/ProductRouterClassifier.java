/**
 * 
 */
package com.manning.sbia.ch06.advanced;


import org.springframework.batch.support.annotation.Classifier;

import com.manning.sbia.ch06.Product;

/**
 * @author bazoud
 * 
 */
public class ProductRouterClassifier {
    @Classifier
    public String classify(Product classifiable) {
        return classifiable.getOperation();
    }
}

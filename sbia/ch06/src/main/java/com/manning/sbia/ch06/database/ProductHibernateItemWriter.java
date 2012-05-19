/**
 * 
 */
package com.manning.sbia.ch06.database;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.manning.sbia.ch06.Product;

/**
 * @author bazoud
 * 
 */
public class ProductHibernateItemWriter implements ItemWriter<Product> {
    @Autowired
    private HibernateProductDao productDao;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public void write(List<? extends Product> items) throws Exception {
        for (Product product : items) {
            productDao.persistProduct(product);
        }
        try {
            hibernateTemplate.flush();
        } finally {
            hibernateTemplate.clear();
        }
    }
}

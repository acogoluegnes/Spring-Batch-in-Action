/**
 * 
 */
package com.manning.sbia.ch06.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.manning.sbia.ch06.Product;

/**
 * @author bazoud
 *
 */
@Repository
public class HibernateProductDao {
    @Autowired
    private HibernateTemplate hibernateTemplate;
    
    public void persistProduct(Product product) {
        if (product.getId() != null) {
            hibernateTemplate.save(product);
        } else {
            hibernateTemplate.update(product);
        }
        
    }

}

/**
 * 
 */
package com.manning.sbia.ch05;

import java.util.List;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.manning.sbia.ch05.Product;

/**
 * @author templth
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractJobStructureTest {

	@Autowired
	protected Job job;
	
	@Autowired
	protected JobLauncher jobLauncher;

	@Autowired
	protected DummyProductItemWriter writer;
	
	protected Product createProduct(String id, String name,
							String description, float price) {
		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		return product;
	}
	
	protected void hasProduct(List<Product> products, String productId) {
		for (Product product : products) {
			if (product.getId().equals(productId)) {
				return;
			}
		}

		Assert.fail("Product with id "+productId+" is expected.");
	}

	protected void checkProducts(List<Product> products, String[] productIds) {
		Assert.assertEquals(8, products.size());
		for (String productId : productIds) {
			hasProduct(products, productId);
		}
	}

}

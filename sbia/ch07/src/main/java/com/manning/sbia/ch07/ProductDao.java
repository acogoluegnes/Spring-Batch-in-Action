/**
 * 
 */
package com.manning.sbia.ch07;

import com.manning.sbia.ch01.domain.Product;

/**
 * @author acogoluegnes
 *
 */
public interface ProductDao {

	Product load(String productId);
	
}

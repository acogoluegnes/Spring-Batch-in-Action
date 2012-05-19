package com.manning.sbia.ch05.service;

import java.util.List;
import org.springframework.beans.factory.InitializingBean;

import com.manning.sbia.ch05.Product;

public class ProductServiceAdapter implements InitializingBean {
	private ProductService productService;
	private List<Product> products;

	public void afterPropertiesSet() throws Exception {
		this.products = productService.getProducts();
	}

	public Product getProduct() {
		if (products.size()>0) {
			return products.remove(0);
		} else {
			return null;
		}
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}

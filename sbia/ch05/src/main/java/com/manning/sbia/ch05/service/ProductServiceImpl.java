package com.manning.sbia.ch05.service;

import java.util.List;
import java.util.ArrayList;

import com.manning.sbia.ch05.Product;

public class ProductServiceImpl implements ProductService {

	private Product createProduct(String id, String name, String description, float price) {
		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		return product;
	}

	public List<Product> getProducts() {
		List<Product> products = new ArrayList<Product>();
		products.add(createProduct("PR....210", "BlackBerry 8100 Pearl", "", 124.60f));
		products.add(createProduct("PR....211", "Sony Ericsson W810i", "", 139.45f));
		products.add(createProduct("PR....212", "Samsung MM-A900M Ace", "", 97.80f));
		products.add(createProduct("PR....213", "Toshiba M285-E 14", "", 166.20f));
		products.add(createProduct("PR....214", "Nokia 2610 Phone", "", 145.50f));
		products.add(createProduct("PR....215", "CN Clogs Beach/Garden Clog", "", 190.70f));
		products.add(createProduct("PR....216", "AT&T 8525 PDA", "", 289.20f));
		products.add(createProduct("PR....217", "Canon Digital Rebel XT 8MP Digital SLR Camera", "", 13.70f));
		return products;
	}
}

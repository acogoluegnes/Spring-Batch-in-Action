package com.manning.sbia.ch13;

import java.util.List;

import com.manning.sbia.ch13.domain.Product;
import com.manning.sbia.ch13.domain.ProductForColumnRange;

public abstract class ThreadUtils {

	public static void writeThreadExecutionMessage(String readWrite, Product product) {
		if (product!=null) {
			writeThreadExecutionMessage(readWrite, " #"+product.getId());
		}
	}

	public static void writeThreadExecutionMessage(String readWrite, ProductForColumnRange product) {
		if (product!=null) {
			writeThreadExecutionMessage(readWrite, " #"+product.getId());
		}
	}

	public static void writeThreadExecutionMessage(String readWrite, List products) {
		if (products!=null && products.size()>0) {
			StringBuilder productIds = new StringBuilder();
			for (Object product : products) {
				productIds.append(" #");
				if (product instanceof Product) {
					productIds.append(((Product)product).getId());
				} else if (product instanceof ProductForColumnRange) {
					productIds.append(((ProductForColumnRange)product).getId());
				} else {
					productIds.append("?");
				}
			}
			writeThreadExecutionMessage(readWrite, productIds.toString());
		}
	}

	public static void writeThreadExecutionMessage(String readWrite, String productIdMessage) {
		String threadName = Thread.currentThread().getName();
		StringBuilder message = new StringBuilder();
		message.append("thread ");
		message.append(threadName);
		message.append(" - ");
		message.append(readWrite);
		message.append(" product(s) with id(s)");
		message.append(productIdMessage);
	}
}

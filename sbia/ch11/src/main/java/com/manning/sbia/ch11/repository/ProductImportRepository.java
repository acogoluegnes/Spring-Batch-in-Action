/**
 * 
 */
package com.manning.sbia.ch11.repository;

import org.springframework.dao.DuplicateKeyException;

import com.manning.sbia.ch11.integration.ProductImport;

/**
 * @author acogoluegnes
 *
 */
public interface ProductImportRepository {

	void createProductImport(String importId) throws DuplicateKeyException;
	
	void mapImportToJobInstance(String importId,Long jobInstanceId);
	
	ProductImport get(String importId);
	
}

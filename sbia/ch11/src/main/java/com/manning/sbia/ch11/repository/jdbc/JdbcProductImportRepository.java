/**
 * 
 */
package com.manning.sbia.ch11.repository.jdbc;

import java.util.Date;

import javax.sql.DataSource;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.manning.sbia.ch11.integration.ProductImport;
import com.manning.sbia.ch11.repository.ProductImportRepository;

/**
 * @author acogoluegnes
 *
 */
@Repository
@Transactional
public class JdbcProductImportRepository implements ProductImportRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	private JobExplorer jobExplorer;
	
	public JdbcProductImportRepository(DataSource dataSource,JobExplorer jobExplorer) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.jobExplorer = jobExplorer;
	}

	/* (non-Javadoc)
	 * @see com.manning.sbia.ch11.repository.ProductImportRepository#createProductImport(java.lang.String)
	 */
	@Override
	public void createProductImport(String importId)
			throws DuplicateKeyException {
		int count = jdbcTemplate.queryForInt("select count(1) from product_import where import_id = ?",importId);
		if(count > 0) {
			throw new DuplicateKeyException("Import already exists: "+importId);
		}
		jdbcTemplate.update("insert into product_import (import_id,creation_date) values (?,?)",importId,new Date());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.manning.sbia.ch11.repository.ProductImportRepository#mapImportToJobInstance(java.lang.String, java.lang.Long)
	 */
	@Override
	public void mapImportToJobInstance(String importId, Long jobInstanceId) {
		jdbcTemplate.update("update product_import set job_instance_id = ? where import_id = ?",
			jobInstanceId,importId);		
	}
	
	@Override
	public ProductImport get(String importId) {
		int count = jdbcTemplate.queryForInt("select count(1) from product_import where import_id = ?",importId);
		if(count == 0) {
			throw new EmptyResultDataAccessException("No import with this ID: "+importId,1);
		}
		String status = "PENDING";
		Long instanceId = jdbcTemplate.queryForLong("select job_instance_id from product_import where import_id = ?",importId);
		JobInstance jobInstance = jobExplorer.getJobInstance(instanceId);		
		if(jobInstance != null) {
			JobExecution lastJobExecution = jobExplorer.getJobExecutions(jobInstance).get(0);
			status = lastJobExecution.getStatus().toString();
		}		 
		return new ProductImport(importId, status);			
	}

}

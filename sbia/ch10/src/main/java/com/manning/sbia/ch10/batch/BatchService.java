/**
 * 
 */
package com.manning.sbia.ch10.batch;

import org.springframework.batch.core.JobExecution;

import com.manning.sbia.ch10.exception.IntegrityViolationException;

/**
 * @author acogoluegnes
 *
 */
public interface BatchService {

	/**
	 * Download input file for processing.
	 * @param targetFile the location where to copy the downloaded file.
	 */
	void download(String targetFile);
	
	/**
	 * Decompress an input archive into a directory.
	 * @param inputFile
	 * @param outputDirectory
	 */
	void decompress(String inputFile,String outputDirectory);
	
	/**
	 * Verify the integrity of extracted files.
	 * @param outputDirectory the directory where to find extracted files
	 * @throws IntegrityViolationException if integrity checks failed
	 */
	void verify(String outputDirectory) throws IntegrityViolationException;
	
	/**
	 * Cleaning the output directory from extracted files.
	 * @param outputDirectory
	 */
	void clean(String outputDirectory);
	
	/**
	 * Track the import in the database.
	 * @param importId
	 */
	void track(String importId);
	
	/**
	 * Extract metadata from a properties file in an output directory.
	 * @param outputDirectory
	 * @return
	 */
	ImportMetadata extractMetadata(String outputDirectory);
	
	/**
	 * Checks if a file exists or not.
	 * @param file
	 * @return
	 */
	boolean exists(String file);

	/**
	 * Create a report on the job execution.
	 * Used to report skipped for example.
	 * @param jobExecution
	 */
	void generateReport(JobExecution jobExecution);
	
}

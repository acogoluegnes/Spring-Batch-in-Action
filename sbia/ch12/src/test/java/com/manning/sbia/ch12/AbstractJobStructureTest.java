/**
 * 
 */
package com.manning.sbia.ch12;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.Date;

import org.h2.tools.Server;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author templth
 *
 */
public abstract class AbstractJobStructureTest {

	@Autowired
	@Qualifier("importProductsJobSuccess")
	protected Job jobSuccess;
	
	@Autowired
	@Qualifier("importProductsJobFailure")
	protected Job jobFailure;
	
	@Autowired
	protected JobLauncher jobLauncher;

	protected Server startH2Server() throws SQLException {
		// removed all db files
		
		File[] files = (new File(".")).listFiles(new FilenameFilter() {
			public boolean accept(File file, String name) {
				return (name.startsWith("ch13."));
			}
		});
		for (File file : files) {
			file.delete();
		}
		
		// start the TCP Server
		Server server = Server.createTcpServer(new String[0]).start();
		
		return server;
	}
	
	protected void stopH2Server(Server server) {
		// stop the TCP Server
		server.stop();
	}

	protected void launchSuccessJob() {
		try {
			JobParametersBuilder parametersBuilder = new JobParametersBuilder();
			parametersBuilder.addDate("date", new Date());
			jobLauncher.run(jobSuccess, parametersBuilder.toJobParameters());
			Thread.sleep(2000);
		} catch (Exception ex) {}
	}
	
	protected void launchFailureJob() {
		try {
			JobParametersBuilder parametersBuilder = new JobParametersBuilder();
			parametersBuilder.addDate("date", new Date());
			jobLauncher.run(jobFailure, parametersBuilder.toJobParameters());
			Thread.sleep(2000);
		} catch (Throwable ex) {}
	}
	
}



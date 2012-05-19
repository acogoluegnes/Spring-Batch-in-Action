/**
 * 
 */
package com.manning.sbia.ch02;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Launches the import invoices job.
 * @author acogoluegnes
 *
 */
public class LaunchImportProductsJob {

	/**
     * @param args
     */
    public static void main(String[] args) throws Exception {
            ApplicationContext ctx = new ClassPathXmlApplicationContext(
                    "/import-products-job-context.xml",
                    "/com/manning/sbia/ch02/batch-infrastructure-context.xml",
        			"/com/manning/sbia/ch02/connect-database-context.xml"
            );
            
            JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
            Job job = ctx.getBean(Job.class);       
            
            jobLauncher.run(job, new JobParametersBuilder()
                    .addString("inputResource", "file:./products.zip")
                    .addString("targetDirectory", "./importproductsbatch/")
                    .addString("targetFile","products.txt")
                    .addString("date", "2010-06-27")
                    .toJobParameters()
            );
    }
	
}

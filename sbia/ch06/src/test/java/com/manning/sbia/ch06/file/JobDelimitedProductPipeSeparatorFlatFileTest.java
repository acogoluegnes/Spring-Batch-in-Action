package com.manning.sbia.ch06.file;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author bazoud
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JobDelimitedProductPipeSeparatorFlatFileTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void testDelimitedProductPipeSeparatorFieldExtractor() throws Exception {
        JobExecution exec = jobLauncherTestUtils.launchJob();
        Assert.assertEquals(BatchStatus.COMPLETED, exec.getStatus());

        Resource ouput= new FileSystemResource("./target/outputs/delimited-pipeseparator.txt");
        AssertLine.assertLineFileEquals(ouput, 1, "PR....210|124.60|BlackBerry 8100 Pearl");
        AssertLine.assertLineFileEquals(ouput, 7, "PR....216|289.20|AT&T 8525 PDA");
        AssertLine.assertLineFileEquals(ouput, 8, "PR....217|13.70|Canon Digital Rebel XT 8MP");
    }

}

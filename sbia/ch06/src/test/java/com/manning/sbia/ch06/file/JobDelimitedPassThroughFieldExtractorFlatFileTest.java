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
public class JobDelimitedPassThroughFieldExtractorFlatFileTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void testDelimitedPassThroughFieldExtractor() throws Exception {
        JobExecution exec = jobLauncherTestUtils.launchJob();
        Assert.assertEquals(BatchStatus.COMPLETED, exec.getStatus());

        Resource ouput = new FileSystemResource("./target/outputs/delimited-passthroughextractor.txt");
        AssertLine.assertLineFileEquals(ouput, 1, "Product [id=PR....210, name=BlackBerry 8100 Pearl]");
        AssertLine.assertLineFileEquals(ouput, 7, "Product [id=PR....216, name=AT&T 8525 PDA]");
        AssertLine.assertLineFileEquals(ouput, 8, "Product [id=PR....217, name=Canon Digital Rebel XT 8MP]");
    }

}

package com.manning.sbia.ch06.file;

import java.text.DecimalFormatSymbols;

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
public class JobFixedWidthLineAggregatorFlatFileTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    char decimalSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();

    @Test
    public void testFixedWidthLineAggregator() throws Exception {
        JobExecution exec = jobLauncherTestUtils.launchJob();
        Assert.assertEquals(BatchStatus.COMPLETED, exec.getStatus());

        Resource ouput= new FileSystemResource("./target/outputs/fixedwidth-lineaggregator.txt");
        AssertLine.assertLineFileEquals(ouput, 1, "PRM....210124" + decimalSeparator + "60BlackBerry 8100 Pearl         BlackBerry  ");
        AssertLine.assertLineFileEquals(ouput, 3, "PRB....734 34" + decimalSeparator + "95Spring Batch in action        Manning     ");
        AssertLine.assertLineFileEquals(ouput, 8, "PRM....214145" + decimalSeparator + "50Nokia 2610 Phone              Nokia       ");
    }

}

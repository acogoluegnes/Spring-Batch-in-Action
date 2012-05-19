package com.manning.sbia.ch06.file;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

import org.apache.commons.io.IOUtils;
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
public class JobXmlMultiFileTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void testXmlMulti() throws Exception {
        JobExecution exec = jobLauncherTestUtils.launchJob();
        Assert.assertEquals(BatchStatus.COMPLETED, exec.getStatus());

        Resource ouput= new FileSystemResource("./target/outputs/products-multi.xml.1");
        String content = IOUtils.toString(ouput.getInputStream());
        assertXpathEvaluatesTo("1000", "count(//product)", content);

        ouput= new FileSystemResource("./target/outputs/products-multi.xml.2");
        content = IOUtils.toString(ouput.getInputStream());
        assertXpathEvaluatesTo("1000", "count(//product)", content);

        ouput= new FileSystemResource("./target/outputs/products-multi.xml.3");
        content = IOUtils.toString(ouput.getInputStream());
        assertXpathEvaluatesTo("1000", "count(//product)", content);

        ouput= new FileSystemResource("./target/outputs/products-multi.xml.4");
        content = IOUtils.toString(ouput.getInputStream());
        assertXpathEvaluatesTo("1000", "count(//product)", content);

        ouput= new FileSystemResource("./target/outputs/products-multi.xml.5");
        content = IOUtils.toString(ouput.getInputStream());
        assertXpathEvaluatesTo("517", "count(//product)", content);

    }

}

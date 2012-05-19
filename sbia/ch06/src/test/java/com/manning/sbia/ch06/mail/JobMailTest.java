package com.manning.sbia.ch06.mail;

import javax.mail.internet.MimeMessage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icegreen.greenmail.util.GreenMail;

import static org.junit.Assert.assertEquals;

/**
 * @author bazoud
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JobMailTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private GreenMail greenMail;

    @Test
    @DirtiesContext
    public void testMail() throws Exception {
        JobExecution exec = jobLauncherTestUtils.launchJob();
        Assert.assertEquals(BatchStatus.COMPLETED, exec.getStatus());

        greenMail.waitForIncomingEmail(5);
        MimeMessage[] received = greenMail.getReceivedMessages();
        assertEquals(5, received.length);
        assertEquals("Hello Mr. White\r\n", received[0].getContent());
    }
}

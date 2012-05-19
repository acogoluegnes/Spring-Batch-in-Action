/**
 * 
 */
package com.manning.sbia.ch06.file;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.file.FlatFileFooterCallback;

/**
 * @author bazoud
 * 
 */
public class ProductFooterCallback extends StepExecutionListenerSupport implements FlatFileFooterCallback {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private StepExecution stepExecution;

    @Override
    public void writeFooter(Writer writer) throws IOException {
        writer.write("# Write count: " + stepExecution.getWriteCount());
        writer.write(LINE_SEPARATOR);
        long delta = stepExecution.getEndTime().getTime() - stepExecution.getStartTime().getTime();
        writer.write("# Done in: " + delta + " ms");
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}

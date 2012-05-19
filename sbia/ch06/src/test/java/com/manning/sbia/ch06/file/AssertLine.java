/**
 * 
 */
package com.manning.sbia.ch06.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Assert;
import org.springframework.core.io.Resource;

/**
 * @author bazoud
 *
 */
public class AssertLine {
    public static void assertLineFileEquals(Resource resource, int lineNumber,  String expectedLine) throws Exception {
        assertLineFileEquals(resource.getFile(), lineNumber,  expectedLine);
    }
    public static void assertLineFileEquals(File file, int lineNumber,  String expectedLine) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            int lineNum = 1;
            String line = reader.readLine();
            while (line != null && lineNum < lineNumber) {
                lineNum++;
                line = reader.readLine();
            }
           Assert.assertEquals("Line number " + lineNum + " does not match.", lineNumber, lineNum);
           Assert.assertEquals("Content line at number " + lineNum + " does not match.", expectedLine, line);
        }
        finally {
            reader.close();
        }
    }

}

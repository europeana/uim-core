/* ExecutionLogFileWriter.java - created on Oct 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.bean.ExecutionBean;

/**
 * Simple test of the log file writer
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Oct 19, 2011
 */
public class ExecutionLogFileWriterTest {

    /**
     * 
     */
    @BeforeClass
    public static void setup() {
        File file = new File("./target/logtest");
        FileUtils.deleteQuietly(file);
    }
    /**
     * @throws IOException
     */
    @Test
    public void testLogToFile() throws IOException {
        ExecutionLogFileWriter<Long> executionLogFileWriter = new ExecutionLogFileWriter<Long>("./target/logtest");
        assertNotNull(executionLogFileWriter);
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Execution<Long> execution=new ExecutionBean(1123);
        executionLogFileWriter.log(execution, Level.SEVERE, "test1");
        assertTrue(executionLogFileWriter.getLogFile(execution).exists());
        executionLogFileWriter.log(execution, Level.INFO, "test2");    
    }
    
    
    
    
}

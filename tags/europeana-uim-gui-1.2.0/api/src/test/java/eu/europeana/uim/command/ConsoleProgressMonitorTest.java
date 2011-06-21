/* ConsoleProgressMonitorTest.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.command;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import eu.europeana.uim.common.RevisableProgressMonitor;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class ConsoleProgressMonitorTest {

    
    /**
     * 
     */
    @Test
    public void testConsoleProgress(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);
        
        ConsoleProgressMonitor monitor = new ConsoleProgressMonitor(out);
        monitor.setStart(1000);

        monitor.beginTask("Junit test", 5);
        assertEquals("Junit test", monitor.getTask());
        assertEquals(5, monitor.getWork());
        assertEquals(0, monitor.getWorked());
        
        
        monitor.worked(1);
        assertEquals(5, monitor.getWork());
        assertEquals(1, monitor.getWorked());

        monitor.worked(2);
        assertEquals(5, monitor.getWork());
        assertEquals(3, monitor.getWorked());
     
        monitor.done();
        
        assertEquals("Starting:Junit test, 5 units of work. [..]\n", new String(baos.toByteArray()));
    }

    /**
     * 
     */
    @Test
    public void testListeningProgress(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);
        
        RevisableProgressMonitor monitor = new RevisableProgressMonitor();
        
        monitor.beginTask("Junit test", 500);
        assertEquals("Junit test", monitor.getTask());
        assertEquals(500, monitor.getWork());
        assertEquals(0, monitor.getWorked());
        
        ConsoleProgressMonitor console = new ConsoleProgressMonitor(out);
        monitor.addListener(console);
        
        monitor.worked(1);
        assertEquals(500, monitor.getWork());
        assertEquals(1, monitor.getWorked());

        monitor.worked(2);
        assertEquals(500, monitor.getWork());
        assertEquals(3, monitor.getWorked());
     
        monitor.done();
        String msg = new String(baos.toByteArray());
        assertEquals("Attached to monitor current status: <Junit test> total 500 units of work. Worked so far:0 [..]\n", msg);
    }

}

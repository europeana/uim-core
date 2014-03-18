/* UIMWorkflowTest.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.felix.service.command.CommandSession;
import org.junit.Test;

import eu.europeana.uim.LegalIngestionWorkflow;
import eu.europeana.uim.UIMRegistry;
import eu.europeana.uim.api.Orchestrator;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class UIMExecutionTest {

    
    @Test
    public void testListRegistry() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);

        UIMRegistry registry = new UIMRegistry();
        
        Orchestrator orchestrator = mock(Orchestrator.class);
        registry.setOrchestrator(orchestrator);
        
        LegalIngestionWorkflow workflow = new LegalIngestionWorkflow();
        registry.addWorkflow(workflow);
        assertFalse(registry.getWorkflows().isEmpty());

        
        CommandSession session = mock(CommandSession.class);
        when(session.getConsole()).thenReturn(out);
        
        UIMExecution command = new UIMExecution(registry);
        command.operation = UIMExecution.Operation.list;
        command.execute(session);
        
        String msg = new String(baos.toByteArray());
        System.out.println(msg);
        assertEquals(41, msg.length());
    }
}

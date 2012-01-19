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

/**
 * UIM info tests.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class UIMInfoTest {
    /**
     * Tests listing of operations of infos.
     * 
     * @throws Exception
     */
    @Test
    public void testListRegistry() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);

        UIMRegistry registry = new UIMRegistry();

        LegalIngestionWorkflow workflow = new LegalIngestionWorkflow();
        registry.addWorkflow(workflow);
        assertFalse(registry.getWorkflows().isEmpty());

        CommandSession session = mock(CommandSession.class);
        when(session.getConsole()).thenReturn(out);

        UIMInfo command = new UIMInfo(registry);
        command.execute(session);

        String msg = new String(baos.toByteArray());
        assertEquals(488, msg.length());
    }
}

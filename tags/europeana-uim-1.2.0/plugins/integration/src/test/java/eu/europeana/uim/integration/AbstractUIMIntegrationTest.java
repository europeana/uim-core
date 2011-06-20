package eu.europeana.uim.integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.testing.AbstractIntegrationTest;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class AbstractUIMIntegrationTest extends AbstractIntegrationTest {

    protected String getCommandResult(String command) {
        String res = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        CommandProcessor cp = getOsgiService(CommandProcessor.class);
        CommandSession cs = cp.createSession(System.in, ps, System.err);
        try {
            cs.execute(command);
            res = baos.toString("UTF-8").trim();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null)
                cs.close();
        }
        return res;

    }
}

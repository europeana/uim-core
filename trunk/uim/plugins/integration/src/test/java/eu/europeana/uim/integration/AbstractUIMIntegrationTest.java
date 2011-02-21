package eu.europeana.uim.integration;

import org.apache.karaf.testing.AbstractIntegrationTest;
import org.osgi.service.command.CommandProcessor;
import org.osgi.service.command.CommandSession;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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

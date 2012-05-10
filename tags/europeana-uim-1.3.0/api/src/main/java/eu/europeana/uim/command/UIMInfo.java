package eu.europeana.uim.command;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.service.command.CommandSession;
import org.apache.felix.service.command.Function;

import eu.europeana.uim.api.Registry;

/**
 * Infos during execution.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
@Command(name = "uim", scope = "info")
public class UIMInfo implements Function, Action {
    private static final Logger log = Logger.getLogger(UIMInfo.class.getName());

    private Registry            registry;

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     */
    public UIMInfo(Registry registry) {
        this.registry = registry;
    }

    @Override
    public Object execute(CommandSession session) throws Exception {
        try {
            return execute(session, Collections.emptyList());
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Failed to start info command:", t);
        }
        return null;
    }

    @Override
    public Object execute(CommandSession commandSession, List<Object> objects) throws Exception {
        commandSession.getConsole().println("UIM Registry: " + registry.toString());
        return null;
    }
}

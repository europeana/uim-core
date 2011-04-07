package eu.europeana.uim.command;

import java.util.Collections;
import java.util.List;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.service.command.CommandSession;
import org.apache.felix.service.command.Function;

import eu.europeana.uim.api.Registry;

/**
 * Inofos during execution.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
@Command(name = "uim", scope = "info")
public class UIMInfo implements Function, Action {
    private Registry registry;

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
        return execute(session, Collections.emptyList());
    }

    @Override
    public Object execute(CommandSession commandSession, List<Object> objects) throws Exception {
        System.out.println("UIM Registry: " + registry.toString());
        return null;
    }
}

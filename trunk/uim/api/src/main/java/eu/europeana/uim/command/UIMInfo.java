package eu.europeana.uim.command;

import eu.europeana.uim.api.Registry;
import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Command;
import org.osgi.service.command.CommandSession;
import org.osgi.service.command.Function;

import java.util.Collections;
import java.util.List;


@Command(name = "uim", scope = "info")
public class UIMInfo implements Function, Action {

    private Registry registry;

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

package eu.europeana.uim.gui.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * RemoteServiceServlet that is integrated with the UIM OSGI platform.
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class AbstractOSGIRemoteServiceServlet extends RemoteServiceServlet {

    private static final String DEVMODE = "devmode";

    private final UIMEngine engine;

    public AbstractOSGIRemoteServiceServlet() {
        this.engine = UIMDependenciesActivator.getUIMEngine();
    }

    @Override
    protected void doUnexpectedFailure(Throwable e) {
        e.printStackTrace();
    }

    @Override
    protected void checkPermutationStrongName() throws SecurityException {
        // this is defined in the gwt:run mojo of the POM
        // without it, GWT won't let us run against the deployed version on the OSGI platform
        if(System.getenv(DEVMODE) != null && System.getenv(DEVMODE).equals("true")) {
            return;
        }
        super.checkPermutationStrongName();
    }

    protected UIMEngine getEngine() {
        if(engine == null) {
            throw new RuntimeException("No engine found. Make sure the OSGi platform is running and all UIM modules are started");
        }
        return engine;
    }
}

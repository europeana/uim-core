package eu.europeana.uim.gui.webconsole;

import org.apache.felix.webconsole.AbstractWebConsolePlugin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class UIMAdminPlugin extends AbstractWebConsolePlugin {

    private final static String LABEL = "UIM";

    @Override
    protected void renderContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Life is good");
    }

    @Override
    public String getLabel() {
        return LABEL;
    }

    @Override
    public String getTitle() {
        return "Unified Ingestion Manager";
    }
}

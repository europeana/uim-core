package eu.europeana.uim.api;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.TKey;

/**
 * Abstract ingestion plugin providing defaults and helper methods for plugin implementation.
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Manuel Bernhardt (bernhardt.manuel@gmail.com)
 */
public abstract class AbstractIngestionPlugin implements IngestionPlugin {

	private static final Logger log = Logger.getLogger(AbstractIngestionPlugin.class.getName());

	private Level level = Level.FINEST;

	public AbstractIngestionPlugin() {
	}

	public AbstractIngestionPlugin(Level level) {
		this.level = level;
	}

	@Override
	public int getPreferredThreadCount() {
		return 1;
	}

	@Override
	public int getMaximumThreadCount() {
		return 1;
	}


	@Override
	@SuppressWarnings("unchecked")
	public TKey<MDRFieldRegistry, ?>[] getInputFields() {
		return new TKey[0];
	}

	@Override
	@SuppressWarnings("unchecked")
	public TKey<MDRFieldRegistry, ?>[] getOutputFields() {
		return new TKey[0];
	}


    @Override
    public void initialize(ExecutionContext context) {
    }


    @Override
    public void completed(ExecutionContext context) {
    }

    
    @Override
    public String getDescription() {
        return "Undescribed plugin.";
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getParameters() {
        return Collections.EMPTY_LIST;
    }
}

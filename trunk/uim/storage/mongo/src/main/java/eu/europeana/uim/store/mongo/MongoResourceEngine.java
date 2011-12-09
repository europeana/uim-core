/**
 * 
 */
package eu.europeana.uim.store.mongo;

import java.io.File;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.Mongo;

import eu.europeana.uim.api.EngineStatus;
import eu.europeana.uim.api.ResourceEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.memory.MemoryResourceEngine;
import eu.europeana.uim.store.mongo.decorators.MongoCollectionDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoExecutionDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoProviderDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoRequestDecorator;
import eu.europeana.uim.store.mongo.resourceentities.CollectionResource;
import eu.europeana.uim.store.mongo.resourceentities.GlobalResource;
import eu.europeana.uim.store.mongo.resourceentities.ProviderResource;
import eu.europeana.uim.store.mongo.resourceentities.WorkflowResource;
import eu.europeana.uim.workflow.Workflow;

/**
 * 
 * @author Georgios Markakis
 */
public class MongoResourceEngine implements ResourceEngine {

    private static final String DEFAULT_UIM_DB_NAME = "UIM";
    private String dbName;
    private EngineStatus status = EngineStatus.STOPPED;
    Mongo mongo = null;
    private DB db = null;
    private Datastore ds = null;
    
	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#initialize()
	 */
	@Override
	public void initialize() {
        try {
            if (dbName == null) {
                dbName = DEFAULT_UIM_DB_NAME;
            }
            status = EngineStatus.BOOTING;
            mongo = new Mongo();
            db = mongo.getDB(dbName);
            Morphia morphia = new Morphia();

          
            morphia.
            //map(MongoAbstractEntity.class).
            //map(MongoAbstractNamedEntity.class).
            map(CollectionResource.class).
            map(GlobalResource.class).
            map(ProviderResource.class).
            map(WorkflowResource.class).
            map(MongoProviderDecorator.class).
            map(MongoExecutionDecorator.class).
            map(MongoCollectionDecorator.class).
            map(MongoRequestDecorator.class);
            
            ds = morphia.createDatastore(mongo, dbName);
            status = EngineStatus.RUNNING;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        

	}
	
	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		return  MongoResourceEngine.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#setConfiguration(java.util.Map)
	 */
	@Override
	public void setConfiguration(Map<String, String> config) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#getConfiguration()
	 */
	@Override
	public Map<String, String> getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#shutdown()
	 */
	@Override
	public void shutdown() {
        status = EngineStatus.STOPPED;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#checkpoint()
	 */
	@Override
	public void checkpoint() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#getStatus()
	 */
	@Override
	public EngineStatus getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#setGlobalResources(java.util.LinkedHashMap)
	 */
	@Override
	public void setGlobalResources(LinkedHashMap<String, List<String>> resources) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#getGlobalResources(java.util.List)
	 */
	@Override
	public LinkedHashMap<String, List<String>> getGlobalResources(
			List<String> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#setWorkflowResources(eu.europeana.uim.workflow.Workflow, java.util.LinkedHashMap)
	 */
	@Override
	public void setWorkflowResources(Workflow workflow,
			LinkedHashMap<String, List<String>> resources) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#getWorkflowResources(eu.europeana.uim.workflow.Workflow, java.util.List)
	 */
	@Override
	public LinkedHashMap<String, List<String>> getWorkflowResources(
			Workflow workflow, List<String> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#setProviderResources(eu.europeana.uim.store.Provider, java.util.LinkedHashMap)
	 */
	@Override
	public void setProviderResources(Provider<?> provider,
			LinkedHashMap<String, List<String>> resources) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#getProviderResources(eu.europeana.uim.store.Provider, java.util.List)
	 */
	@Override
	public LinkedHashMap<String, List<String>> getProviderResources(
			Provider<?> provider, List<String> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#setCollectionResources(eu.europeana.uim.store.Collection, java.util.LinkedHashMap)
	 */
	@Override
	public void setCollectionResources(Collection<?> collection,
			LinkedHashMap<String, List<String>> resources) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#getCollectionResources(eu.europeana.uim.store.Collection, java.util.List)
	 */
	@Override
	public LinkedHashMap<String, List<String>> getCollectionResources(
			Collection<?> collection, List<String> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#getResourceDirectory()
	 */
	@Override
	public File getResourceDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#getWorkingDirectory()
	 */
	@Override
	public File getWorkingDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.ResourceEngine#getTemporaryDirectory()
	 */
	@Override
	public File getTemporaryDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

}

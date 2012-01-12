/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europeana.uim.store.mongo;

import java.io.File;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bson.types.ObjectId;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.Mongo;
import eu.europeana.uim.api.EngineStatus;
import eu.europeana.uim.api.ResourceEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.UimEntity;
import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.bean.ProviderBean;
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
 * Basic implementation of a ResourceEngine based on MongoDB with Morphia.
 * 
 * @author Georgios Markakis <gwarkx@hotmail.com>
 */
public class MongoResourceEngine implements ResourceEngine {

	private static final String DEFAULT_UIM_DB_NAME = "UIM";
	private String dbName;
	private EngineStatus status = EngineStatus.STOPPED;
	Mongo mongo = null;
	private DB db = null;
	private Datastore ds = null;

	public MongoResourceEngine() {
	}

	public MongoResourceEngine(String dbName) {
		this.dbName = dbName;
	}

	/*
	 * (non-Javadoc)
	 * 
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

			morphia.map(CollectionResource.class).map(GlobalResource.class)
					.map(ProviderResource.class).map(WorkflowResource.class)
					.map(MongoProviderDecorator.class)
					.map(MongoExecutionDecorator.class)
					.map(MongoCollectionDecorator.class)
					.map(MongoRequestDecorator.class);

			ds = morphia.createDatastore(mongo, dbName);
			status = EngineStatus.RUNNING;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.ResourceEngine#getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		return MongoResourceEngine.class.getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.ResourceEngine#setConfiguration(java.util.Map)
	 */
	@Override
	public void setConfiguration(Map<String, String> config) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.ResourceEngine#getConfiguration()
	 */
	@Override
	public Map<String, String> getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.ResourceEngine#shutdown()
	 */
	@Override
	public void shutdown() {
		status = EngineStatus.STOPPED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.ResourceEngine#checkpoint()
	 */
	@Override
	public void checkpoint() {
		// Does nothing in this implementation
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.ResourceEngine#getStatus()
	 */
	@Override
	public EngineStatus getStatus() {
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.ResourceEngine#setGlobalResources(java.util.
	 * LinkedHashMap)
	 */
	@Override
	public void setGlobalResources(LinkedHashMap<String, List<String>> resources) {

		GlobalResource global = ds.find(GlobalResource.class).get();

		if (global == null) {
			global = new GlobalResource();
			ds.save(global);
		}

		if (resources == null) {
			global.getResources().clear();
			ds.save(global);
			return;
		}
		for (String key : resources.keySet()) {
			if (resources.get(key) == null) {
				// clean up. if the value is null, explicitely remove the key
				// from the stored set.
				global.getResources().remove(key);
			} else {
				global.getResources().put(key, resources.get(key));
			}
		}

		ds.save(global);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.ResourceEngine#getGlobalResources(java.util.List)
	 */
	@Override
	public LinkedHashMap<String, List<String>> getGlobalResources(
			List<String> keys) {
		LinkedHashMap<String, List<String>> results = new LinkedHashMap<String, List<String>>();
		GlobalResource global = ds.find(GlobalResource.class).get();
		if (global == null) {
			global = new GlobalResource();
			ds.save(global);
		}
		for (String key : keys) {
			List<String> values = global.getResources().get(key);
			results.put(key, values);
		}
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.ResourceEngine#setWorkflowResources(eu.europeana
	 * .uim.workflow.Workflow, java.util.LinkedHashMap)
	 */
	@Override
	public void setWorkflowResources(Workflow workflow,
			LinkedHashMap<String, List<String>> resources) {
		WorkflowResource wfresource = ds.find(WorkflowResource.class)
				.filter("workflowid", workflow.getIdentifier()).get();
		if (wfresource == null) {
			wfresource = new WorkflowResource(workflow.getIdentifier());
			ds.save(wfresource);
		}
		if (resources == null) {
			// clean up and remove id entry from resources
			wfresource.getResources().clear();
			ds.save(wfresource);
			return;
		}
		LinkedHashMap<String, List<String>> workResources = wfresource
				.getResources();
		for (Entry<String, List<String>> entry : resources.entrySet()) {
			if (entry.getValue() == null) {
				// clean up. if the value is null, explicitely remove the key
				// from the stored set.
				workResources.remove(entry.getKey());
			} else {
				workResources.put(entry.getKey(), entry.getValue());
			}
		}
		ds.save(wfresource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.ResourceEngine#getWorkflowResources(eu.europeana
	 * .uim.workflow.Workflow, java.util.List)
	 */
	@Override
	public LinkedHashMap<String, List<String>> getWorkflowResources(
			Workflow workflow, List<String> keys) {
		LinkedHashMap<String, List<String>> results = new LinkedHashMap<String, List<String>>();
		WorkflowResource wfresource = ds.find(WorkflowResource.class)
				.filter("workflowid", workflow.getIdentifier()).get();
		if (wfresource == null) {
			for (String key : keys) {
				results.put(key, null);
			}
			return results;
		}
		LinkedHashMap<String, List<String>> workflowMap = wfresource
				.getResources();
		for (String key : keys) {
			List<String> values = null;
			if (workflowMap != null) {
				values = workflowMap.get(key);
			}
			results.put(key, values);
		}
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.ResourceEngine#setProviderResources(eu.europeana
	 * .uim.store.Provider, java.util.LinkedHashMap)
	 */
	@Override
	public void setProviderResources(Provider<?> provider,
			LinkedHashMap<String, List<String>> resources) {
		provider = (Provider<?>) ensureConsistency(provider);
		ProviderResource prresource = ds.find(ProviderResource.class)
				.filter("provider", provider).get();
		if (prresource == null) {
			prresource = new ProviderResource(provider);
			ds.save(prresource);
		}
		if (resources == null) {
			// clean up and remove id entry from resources
			prresource.getResources().clear();
			ds.save(prresource);
			return;
		}
		LinkedHashMap<String, List<String>> provResources = prresource
				.getResources();
		for (String key : resources.keySet()) {
			if (resources.get(key) == null) {
				// clean up. if the value is null, explicitly remove the key
				// from the stored set.
				provResources.remove(key);
			} else {
				provResources.put(key, resources.get(key));
			}
		}
		ds.save(prresource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.ResourceEngine#getProviderResources(eu.europeana
	 * .uim.store.Provider, java.util.List)
	 */
	@Override
	public LinkedHashMap<String, List<String>> getProviderResources(
			Provider<?> provider, List<String> keys) {
		LinkedHashMap<String, List<String>> results = new LinkedHashMap<String, List<String>>();
		provider = (Provider<?>) ensureConsistency(provider);
		ProviderResource prresource = ds.find(ProviderResource.class)
				.filter("provider", provider).get();
		if (prresource == null) {
			for (String key : keys) {
				results.put(key, null);
			}
			return results;
		}
		LinkedHashMap<String, List<String>> providerMap = prresource
				.getResources();
		for (String key : keys) {
			List<String> values = null;
			if (providerMap != null) {
				values = providerMap.get(key);
			}
			results.put(key, values);
		}
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.ResourceEngine#setCollectionResources(eu.europeana
	 * .uim.store.Collection, java.util.LinkedHashMap)
	 */
	@Override
	public void setCollectionResources(Collection<?> collection,
			LinkedHashMap<String, List<String>> resources) {
		collection = (Collection<?>) ensureConsistency(collection);
		CollectionResource colresource = ds.find(CollectionResource.class)
				.filter("collection", collection).get();
		if (colresource == null) {
			colresource = new CollectionResource(collection);
			ds.save(colresource);
		}
		if (resources == null) {
			// clean up and remove id entry from resources
			colresource.getResources().clear();
			ds.save(colresource);
			return;
		}
		LinkedHashMap<String, List<String>> collResources = colresource
				.getResources();
		for (String key : resources.keySet()) {
			if (resources.get(key) == null) {
				// clean up. if the value is null, explicitely remove the key
				// from the stored set.
				collResources.remove(key);
			} else {
				collResources.put(key, resources.get(key));
			}
		}
		ds.save(colresource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.ResourceEngine#getCollectionResources(eu.europeana
	 * .uim.store.Collection, java.util.List)
	 */
	@Override
	public LinkedHashMap<String, List<String>> getCollectionResources(
			Collection<?> collection, List<String> keys) {

		collection = (Collection<?>) ensureConsistency(collection);
		LinkedHashMap<String, List<String>> results = new LinkedHashMap<String, List<String>>();
		CollectionResource colresource = ds.find(CollectionResource.class)
				.filter("collection", collection).get();

		if (colresource == null) {
			for (String key : keys) {
				results.put(key, null);
			}
			return results;
		}
		LinkedHashMap<String, List<String>> collectionMap = colresource
				.getResources();

		for (String key : keys) {
			List<String> values = null;
			if (collectionMap != null) {
				values = collectionMap.get(key);
			}
			results.put(key, values);
		}
		return results;
	}

	/**
	 * Private method that guranteed that the UIM Entity which is used for
	 * search purposes will always be converted to MongoDB entity type which
	 * contains a valid ObjectID reference.
	 * 
	 * @param uimType
	 * @return an appropriate MongoDB entity type
	 */
	private UimEntity<?> ensureConsistency(UimEntity<?> uimType) {
		if (uimType instanceof CollectionBean) {
			MongoCollectionDecorator<String> tmp = new MongoCollectionDecorator<String>();
			ObjectId id = ObjectId.massageToObjectId(uimType.getId());
			tmp.setMongoId(id);
			return tmp;
		}
		if (uimType instanceof ProviderBean) {
			MongoProviderDecorator<String> tmp = new MongoProviderDecorator<String>();
			ObjectId id = ObjectId.massageToObjectId(uimType.getId());
			tmp.setMongoId(id);
			return tmp;
		}
		return uimType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.ResourceEngine#getResourceDirectory()
	 */
	@Override
	public File getResourceDirectory() {
		// Not used here
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.ResourceEngine#getWorkingDirectory()
	 */
	@Override
	public File getWorkingDirectory() {
		// Not used here
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.ResourceEngine#getTemporaryDirectory()
	 */
	@Override
	public File getTemporaryDirectory() {
		// Not used here
		return null;
	}

}

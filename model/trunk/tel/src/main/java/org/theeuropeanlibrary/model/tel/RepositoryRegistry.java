package org.theeuropeanlibrary.model.tel;

import java.util.List;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.bean.ExecutionBean;
import eu.europeana.uim.store.bean.ProviderBean;
import eu.europeana.uim.store.bean.RequestBean;
import gnu.trove.set.hash.TLongHashSet;

/**
 * Registry holding all known keys for the repository like collections, providers, etc.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Nov 15, 2012
 */
@SuppressWarnings("rawtypes")
public final class RepositoryRegistry {
    // storage keys
    /**
     * provider typed key
     */
    public static final TKey<RepositoryRegistry, ProviderBean>   PROVIDER                 = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "provider",
                                                                                                  ProviderBean.class);
    /**
     * request typed key
     */
    public static final TKey<RepositoryRegistry, RequestBean>    REQUEST                  = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "request",
                                                                                                  RequestBean.class);
    /**
     * collection typed key
     */
    public static final TKey<RepositoryRegistry, CollectionBean> COLLECTION               = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "collection",
                                                                                                  CollectionBean.class);
    /**
     * execution typed key
     */
    public static final TKey<RepositoryRegistry, ExecutionBean>  EXECUTION                = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "execution",
                                                                                                  ExecutionBean.class);
    /**
     * typed key for set of mdrs for a collection
     */
    public static final TKey<RepositoryRegistry, TLongHashSet>   MDR_COLLECTION           = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "mdr.collection",
                                                                                                  TLongHashSet.class);
    /**
     * typed key for set of mdrs for a request
     */
    public static final TKey<RepositoryRegistry, TLongHashSet>   MDR_REQUEST              = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "mdr.request",
                                                                                                  TLongHashSet.class);
    /**
     * typed key for type of entity
     */
    public static final TKey<RepositoryRegistry, String>         ENTITY_TYPE              = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "entity.type",
                                                                                                  String.class);
    /**
     * typed key for relation between providers
     */
    public static final TKey<RepositoryRegistry, String>         REL_PROVIDER_PROVIDER    = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.provider.provider",
                                                                                                  String.class);
    /**
     * typed key for relation between provider and collection
     */
    public static final TKey<RepositoryRegistry, String>         REL_PROVIDER_COLLECTION  = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.provider.collection",
                                                                                                  String.class);
    /**
     * typed key for relation between collection and request
     */
    public static final TKey<RepositoryRegistry, String>         REL_COLLECTION_REQUEST   = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.collection.request",
                                                                                                  String.class);
    /**
     * typed key for relation between collection and record
     */
    public static final TKey<RepositoryRegistry, String>         REL_COLLECTION_RECORD    = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.collection.record",
                                                                                                  String.class);
    /**
     * typed key for relation between collection and execution
     */
    public static final TKey<RepositoryRegistry, String>         REL_COLLECTION_EXECUTION = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.collection.execution",
                                                                                                  String.class);
    /**
     * typed key for relation between request and execution
     */
    public static final TKey<RepositoryRegistry, String>         REL_REQUEST_EXECUTION    = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.request.execution",
                                                                                                  String.class);
    /**
     * typed key for relation between record and execution
     */
    public static final TKey<RepositoryRegistry, String>         REL_RECORD_EXECUTION     = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.record.execution",
                                                                                                  String.class);

    // resource keys
    /**
     * key specifying a resource entry
     */
    public static final TKey<RepositoryRegistry, String>         RESOURCE_KEY             = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "resource.key",
                                                                                                  String.class);

    /**
     * key specifying value of a resouce
     */
    public static final TKey<RepositoryRegistry, List>           RESOURCE_VALUES          = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "resource.values",
                                                                                                  List.class);

    /**
     * scope of resource like workflow, collection, provider
     */
    public static final TKey<RepositoryRegistry, String>         RESOURCE_SCOPE           = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "resource.scope",
                                                                                                  String.class);

    // constants
    /**
     * global resource entity constant
     */
    public static final String                                   ENTITY_GLOBAL            = "global";
    /**
     * workflow entity constant
     */
    public static final String                                   ENTITY_WORKFLOW          = "workflow";
    /**
     * provider entity constant
     */
    public static final String                                   ENTITY_PROVIDER          = "provider";
    /**
     * collection entity constant
     */
    public static final String                                   ENTITY_COLLECTION        = "collection";
    /**
     * request entity constant
     */
    public static final String                                   ENTITY_REQUEST           = "request";
    /**
     * execution entity constant
     */
    public static final String                                   ENTITY_EXECUTION         = "execution";

}

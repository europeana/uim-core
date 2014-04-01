package org.theeuropeanlibrary.model.tel;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.theeuropeanlibrary.model.common.FieldId;

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
    @FieldId(14)
    public static final TKey<RepositoryRegistry, ProviderBean>   PROVIDER                 = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "provider",
                                                                                                  ProviderBean.class);
    /**
     * request typed key
     */
    @FieldId(20)
    public static final TKey<RepositoryRegistry, RequestBean>    REQUEST                  = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "request",
                                                                                                  RequestBean.class);
    /**
     * collection typed key
     */
    @FieldId(13)
    public static final TKey<RepositoryRegistry, CollectionBean> COLLECTION               = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "collection",
                                                                                                  CollectionBean.class);
    /**
     * execution typed key
     */
    @FieldId(15)
    public static final TKey<RepositoryRegistry, ExecutionBean>  EXECUTION                = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "execution",
                                                                                                  ExecutionBean.class);
    /**
     * typed key for set of mdrs for a collection
     */
    @FieldId(18)
    public static final TKey<RepositoryRegistry, TLongHashSet>   MDR_COLLECTION           = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "mdr.collection",
                                                                                                  TLongHashSet.class);

    /**
     * typed key for set of mdrs for a request
     */
    @FieldId(19)
    public static final TKey<RepositoryRegistry, TLongHashSet>   MDR_REQUEST              = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "mdr.request",
                                                                                                  TLongHashSet.class);
    /**
     * typed key for type of entity
     */
    @FieldId(10)
    public static final TKey<RepositoryRegistry, String>         ENTITY_TYPE              = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "entity.type",
                                                                                                  String.class);
    /**
     * typed key for relation between providers
     */
    @FieldId(11)
    public static final TKey<RepositoryRegistry, String>         REL_PROVIDER_PROVIDER    = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.provider.provider",
                                                                                                  String.class);
    /**
     * typed key for relation between provider and collection
     */
    @FieldId(12)
    public static final TKey<RepositoryRegistry, String>         REL_PROVIDER_COLLECTION  = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.provider.collection",
                                                                                                  String.class);
    /**
     * typed key for relation between collection and request
     */
    @FieldId(17)
    public static final TKey<RepositoryRegistry, String>         REL_COLLECTION_REQUEST   = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.collection.request",
                                                                                                  String.class);
    /**
     * typed key for relation between collection and record
     */
    @FieldId(21)
    public static final TKey<RepositoryRegistry, String>         REL_COLLECTION_RECORD    = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.collection.record",
                                                                                                  String.class);
    /**
     * typed key for relation between collection and execution
     */
    @FieldId(16)
    public static final TKey<RepositoryRegistry, String>         REL_COLLECTION_EXECUTION = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.collection.execution",
                                                                                                  String.class);
    /**
     * typed key for relation between request and execution
     */
    @FieldId(22)
    public static final TKey<RepositoryRegistry, String>         REL_REQUEST_EXECUTION    = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.request.execution",
                                                                                                  String.class);
    /**
     * typed key for relation between record and execution
     */
    @FieldId(23)
    public static final TKey<RepositoryRegistry, String>         REL_RECORD_EXECUTION     = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "relation.record.execution",
                                                                                                  String.class);

    // resource keys
    /**
     * key specifying a resource entry
     */
    @FieldId(11)
    public static final TKey<RepositoryRegistry, String>         RESOURCE_KEY             = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "resource.key",
                                                                                                  String.class);

    /**
     * key specifying value of a resouce
     */
    @FieldId(12)
    public static final TKey<RepositoryRegistry, List>           RESOURCE_VALUES          = TKey.register(
                                                                                                  RepositoryRegistry.class,
                                                                                                  "resource.values",
                                                                                                  List.class);

    /**
     * scope of resource like workflow, collection, provider
     */
    @FieldId(10)
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

    /**
     * field keys lookup to an integer value for authority information
     */
    public static Map<TKey<?, ?>, Integer>                       authorityTkeyFieldId;
    /**
     * integer value to field keys lookup for authority information
     */
    public static Map<Integer, TKey<?, ?>>                       authorityFieldIdTkey;

    /**
     * field keys lookup to an integer value for resource information
     */
    public static Map<TKey<?, ?>, Integer>                       resourceTkeyFieldId;
    /**
     * integer value to field keys lookup for resource information
     */
    public static Map<Integer, TKey<?, ?>>                       resourceFieldIdTkey;

    static {
        authorityTkeyFieldId = new HashMap<TKey<?, ?>, Integer>();
        authorityFieldIdTkey = new HashMap<Integer, TKey<?, ?>>();

        resourceTkeyFieldId = new HashMap<TKey<?, ?>, Integer>();
        resourceFieldIdTkey = new HashMap<Integer, TKey<?, ?>>();

        for (Field f : RepositoryRegistry.class.getDeclaredFields()) {
            FieldId ann = f.getAnnotation(FieldId.class);
            if (ann != null) {
                if (f.getName().contains("RESOURCE")) {
                    addResourceField(f);
                } else {
                    addAuthorityField(f);
                }
            }
        }
    }

    private static void addResourceField(Field f) {
        FieldId ann = f.getAnnotation(FieldId.class);
        if (resourceFieldIdTkey.containsKey(ann.value())) { throw new RuntimeException(
                "Duplicate field id '" + ann.value() + "' is not allowed!"); }

        try {
            resourceTkeyFieldId.put((TKey<?, ?>)f.get(TKey.class), ann.value());
            resourceFieldIdTkey.put(ann.value(), (TKey<?, ?>)f.get(TKey.class));
        } catch (Exception e) {
            throw new RuntimeException("Field '" + f + "' cannot be accessed!", e);
        }
    }

    private static void addAuthorityField(Field f) {
        FieldId ann = f.getAnnotation(FieldId.class);
        if (authorityFieldIdTkey.containsKey(ann.value())) { throw new RuntimeException(
                "Duplicate field id '" + ann.value() + "' is not allowed!"); }

        try {
            authorityTkeyFieldId.put((TKey<?, ?>)f.get(TKey.class), ann.value());
            authorityFieldIdTkey.put(ann.value(), (TKey<?, ?>)f.get(TKey.class));
        } catch (Exception e) {
            throw new RuntimeException("Field '" + f + "' cannot be accessed!", e);
        }
    }
}

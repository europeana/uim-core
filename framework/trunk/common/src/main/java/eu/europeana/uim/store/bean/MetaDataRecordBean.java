package eu.europeana.uim.store.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * In-memory implemenation of {@link MetaDataRecord} that uses Long as ID. It is supposed to be the
 * core class for usage if there is no need for a special implemenation due to special requirements
 * of the storage backend.
 * 
 * @param <I>
 *            unique ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
@SuppressWarnings("unchecked")
public class MetaDataRecordBean<I> extends AbstractEntityBean<I> implements MetaDataRecord<I>,
        Serializable {
    private static final long                            serialVersionUID = 1L;

    /**
     * the collection that is responsible for this record
     */
    private Collection<I>                                collection;

    /**
     * holds for each key a list of known qualified values
     */
    private HashMap<TKey<?, ?>, List<QualifiedValue<?>>> fields           = new HashMap<TKey<?, ?>, List<QualifiedValue<?>>>();

    /**
     * Maintain index in order to retain ordering. null: not calculated yet
     */
    private transient Integer                            nextOrderIndex   = null;

    /**
     * Creates a new instance of this class.
     */
    public MetaDataRecordBean() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     *            unique ID
     * @param collection
     *            the collection that is responsible for this record
     */
    public MetaDataRecordBean(I id, Collection<I> collection) {
        super(id);
        this.collection = collection;
    }

    /**
     * @return the collection this mdr belongs to
     */
    @Override
    public Collection<I> getCollection() {
        return collection;
    }

    /**
     * @param collection
     */
    public void setCollection(Collection<I> collection) {
        this.collection = collection;
    }

    @Override
    public <N, T> T getFirstValue(TKey<N, T> key, Enum<?>... qualifiers) {
        T result = null;
        List<QualifiedValue<?>> values = fields.get(key);
        if (values != null && values.size() > 0) {
            for (QualifiedValue<?> value : values) {
                if (checkQualifier(value, qualifiers)) {
                    result = (T)value.getValue();
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public <N, T> QualifiedValue<T> getFirstQualifiedValue(TKey<N, T> key, Enum<?>... qualifiers) {
        QualifiedValue<T> result = null;
        List<QualifiedValue<?>> values = fields.get(key);
        if (values != null && values.size() > 0) {
            for (QualifiedValue<?> value : values) {
                if (checkQualifier(value, qualifiers)) {
                    result = (QualifiedValue<T>)value;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public <N, T> List<QualifiedValue<T>> getQualifiedValues(TKey<N, T> key, Enum<?>... qualifiers) {
        List<QualifiedValue<T>> result = new ArrayList<QualifiedValue<T>>();
        List<QualifiedValue<?>> values = fields.get(key);
        if (values != null && values.size() > 0) {
            for (QualifiedValue<?> value : values) {
                if (checkQualifier(value, qualifiers)) {
                    result.add((QualifiedValue<T>)value);
                }
            }
        }
        return result;
    }

    @Override
    public <N, T> List<T> getValues(TKey<N, T> key, Enum<?>... qualifiers) {
        List<T> result = new ArrayList<T>();
        List<QualifiedValue<?>> values = fields.get(key);
        if (values != null && values.size() > 0) {
            for (QualifiedValue<?> value : values) {
                if (checkQualifier(value, qualifiers)) {
                    result.add((T)value.getValue());
                }
            }
        }
        return result;
    }

    @Override
    public <N, T> void addValue(TKey<N, T> key, T value, Enum<?>... qualifiers) {
        if (value == null) { throw new IllegalArgumentException(
                "Argument 'value' should not be null!"); }

        Set<Enum<?>> quals = new HashSet<Enum<?>>();
        for (Enum<?> qualifier : qualifiers) {
            if (qualifier != null) {
                quals.add(qualifier);
            }
        }

        List<QualifiedValue<?>> values = fields.get(key);
        if (values == null) {
            values = new ArrayList<MetaDataRecord.QualifiedValue<?>>();
            fields.put(key, values);
        }
        if (nextOrderIndex == null) {
            nextOrderIndex = calculateNextOrderIndex();
        }
        values.add(new QualifiedValue<T>(value, quals, nextOrderIndex++));
    }

    private int calculateNextOrderIndex() {
        int nextOrderIndex = 0;
        for (List<QualifiedValue<?>> vals : fields.values()) {
            for (QualifiedValue<?> val : vals) {
                if (val.getOrderIndex() >= nextOrderIndex) {
                    nextOrderIndex = val.getOrderIndex() + 1;
                }
            }
        }
        return nextOrderIndex;
    }

    /**
     * This should only be used during conversion, as in general addValue is the way to fill a
     * {@link MetaDataRecord}.
     * 
     * @param <N>
     *            the namespace (type) in which the field is defined
     * @param <T>
     *            the runtime type of the values for this field
     * @param key
     *            typed key which holds namespace, name and type information
     * @param values
     *            list of qualified values set under the given key (overrides exisiting entries)
     */
    public <N, T> void setValue(TKey<N, T> key, List<QualifiedValue<T>> values) {
        List<QualifiedValue<?>> oldValues = fields.get(key);
        if (oldValues != null) { throw new IllegalArgumentException(
                "setValue should only be called be called once per tkey"); }
        if (nextOrderIndex != null) { throw new IllegalArgumentException(
                "setValue should not be called after addValue"); }
        List<QualifiedValue<?>> sortedValues = new ArrayList<QualifiedValue<?>>(values);
        Collections.sort(sortedValues);
        fields.put(key, sortedValues);
    }

    @Override
    public <N, T> List<QualifiedValue<T>> deleteValues(TKey<N, T> key, Enum<?>... qualifiers) {
        List<QualifiedValue<T>> result = new ArrayList<QualifiedValue<T>>();

        List<QualifiedValue<?>> values = fields.remove(key);
        if (values != null && values.size() > 0) {
            List<QualifiedValue<?>> leftValues = new ArrayList<MetaDataRecord.QualifiedValue<?>>();

            for (QualifiedValue<?> value : values) {
                if (checkQualifier(value, qualifiers)) {
                    result.add((QualifiedValue<T>)value);
                } else {
                    leftValues.add(value);
                }
            }

            if (leftValues.size() > 0) {
                fields.put(key, leftValues);
            }
        }

        for (QualifiedValue<T> res : result) {
            deleteRelations(res);
        }

        return result;
    }

    @Override
    public <N, T> boolean deleteValue(TKey<N, T> key, QualifiedValue<T> remove) {
        List<QualifiedValue<?>> values = fields.get(key);
        boolean removed = values.remove(remove);
        if (removed) {
            deleteRelations(remove);
        }
        return removed;
    }

    private boolean checkQualifier(QualifiedValue<?> value, Enum<?>... qualifiers) {
        boolean contained = true;
        for (Enum<?> qualifier : qualifiers) {
            if (!value.getQualifiers().contains(qualifier)) {
                contained = false;
                break;
            }
        }
        return contained;
    }

    // modeling structure information
    /**
     * holds relations starting from source nodes, giving back target nodes with connected
     * qualifications
     */
    private HashMap<QualifiedValue<?>, HashMap<QualifiedValue<?>, Set<Enum<?>>>> sourcesLookup = new HashMap<QualifiedValue<?>, HashMap<QualifiedValue<?>, Set<Enum<?>>>>();

    /**
     * holds relations ending in target nodes, giving back source nodes with connected
     * qualifications
     */
    private HashMap<QualifiedValue<?>, HashMap<QualifiedValue<?>, Set<Enum<?>>>> targetsLookup = new HashMap<QualifiedValue<?>, HashMap<QualifiedValue<?>, Set<Enum<?>>>>();

    @Override
    public <S, T> void addRelation(QualifiedValue<S> source, QualifiedValue<T> target,
            Enum<?>... qualifiers) {
        Set<Enum<?>> qualifierSet = new HashSet<Enum<?>>();
        for (Enum<?> qualifier : qualifiers) {
            qualifierSet.add(qualifier);
        }

        HashMap<QualifiedValue<?>, Set<Enum<?>>> targetsMap = sourcesLookup.get(source);
        if (targetsMap == null) {
            targetsMap = new HashMap<QualifiedValue<?>, Set<Enum<?>>>();
            sourcesLookup.put(source, targetsMap);
        }
        targetsMap.put(target, qualifierSet);

        HashMap<QualifiedValue<?>, Set<Enum<?>>> sourcesMap = targetsLookup.get(source);
        if (sourcesMap == null) {
            sourcesMap = new HashMap<QualifiedValue<?>, Set<Enum<?>>>();
            targetsLookup.put(target, sourcesMap);
        }
        sourcesMap.put(source, qualifierSet);
    }

    @Override
    public <T> void deleteRelations(QualifiedValue<T> value, Enum<?>... qualifiers) {
        clearMap(sourcesLookup, targetsLookup, value, qualifiers);
        clearMap(targetsLookup, sourcesLookup, value, qualifiers);
    }

    private <T> void clearMap(
            HashMap<QualifiedValue<?>, HashMap<QualifiedValue<?>, Set<Enum<?>>>> startLookup,
            HashMap<QualifiedValue<?>, HashMap<QualifiedValue<?>, Set<Enum<?>>>> syncLookup,
            QualifiedValue<T> value, Enum<?>... qualifiers) {
        Set<QualifiedValue<?>> rems = new HashSet<QualifiedValue<?>>();
        if (qualifiers.length > 0) {
            rems.addAll(startLookup.remove(value).keySet());
        } else {
            HashMap<QualifiedValue<?>, Set<Enum<?>>> targetsMap = startLookup.get(value);
            if (targetsMap != null) {
                for (Entry<QualifiedValue<?>, Set<Enum<?>>> entryTargets : targetsMap.entrySet()) {
                    boolean contained = true;
                    for (Enum<?> qualifier : qualifiers) {
                        if (!entryTargets.getValue().contains(qualifier)) {
                            contained = false;
                            break;
                        }
                    }
                    if (contained) {
                        rems.add(entryTargets.getKey());
                        targetsMap.remove(entryTargets.getKey());
                    }
                }
                if (targetsMap.isEmpty()) {
                    startLookup.remove(value);
                }
            }
        }

        for (QualifiedValue<?> rem : rems) {
            HashMap<QualifiedValue<?>, Set<Enum<?>>> sourcesMap = syncLookup.get(rem);
            sourcesMap.remove(value);
            if (sourcesMap.isEmpty()) {
                syncLookup.remove(rem);
            }
        }
    }

    @Override
    public <N, S, T> Set<QualifiedValue<T>> getTargetQualifiedValues(QualifiedValue<S> source,
            TKey<N, T> targetKey, Enum<?>... qualifiers) {
        Set<QualifiedValue<T>> results = new HashSet<QualifiedValue<T>>();

        HashMap<QualifiedValue<?>, Set<Enum<?>>> targetsMap = sourcesLookup.get(source);
        if (targetsMap != null) {
            for (Entry<QualifiedValue<?>, Set<Enum<?>>> entryTargets : targetsMap.entrySet()) {
                boolean contained = true;

                List<QualifiedValue<?>> validTargets = fields.get(targetKey);
                if (!validTargets.contains(entryTargets.getKey())) {
                    contained = false;
                } else {
                    for (Enum<?> qualifier : qualifiers) {
                        if (!entryTargets.getValue().contains(qualifier)) {
                            contained = false;
                            break;
                        }
                    }
                }

                if (contained) {
                    results.add((QualifiedValue<T>)entryTargets.getKey());
                }
            }
        }

        return results;

    }

    
    
    
    @Override
    public <N, S, T> Set<QualifiedValue<S>> getSourceQualifiedValues(QualifiedValue<T> target,
            TKey<N, S> sourceKey, Enum<?>... qualifiers) {
        Set<QualifiedValue<S>> results = new HashSet<QualifiedValue<S>>();

        HashMap<QualifiedValue<?>, Set<Enum<?>>> sourcesMap = targetsLookup.get(target);
        if (sourcesMap != null) {
            for (Entry<QualifiedValue<?>, Set<Enum<?>>> entrySources : sourcesMap.entrySet()) {
                boolean contained = true;

                List<QualifiedValue<?>> validSources = fields.get(sourceKey);
                if (!validSources.contains(entrySources.getKey())) {
                    contained = false;
                } else {
                    for (Enum<?> qualifier : qualifiers) {
                        if (!entrySources.getValue().contains(qualifier)) {
                            contained = false;
                            break;
                        }
                    }
                }

                if (contained) {
                    results.add((QualifiedValue<S>)entrySources.getKey());
                }
            }
        }

        return results;
    }

    
    @Override
    public <N, S, T> Set<QualifiedRelation<S, T>> getSourceQualifiedRelations(QualifiedValue<T> target,
            TKey<N, S> sourceKey, Enum<?>... qualifiers) {
        Set<QualifiedRelation<S, T>> results = new HashSet<MetaDataRecord.QualifiedRelation<S,T>>();

        HashMap<QualifiedValue<?>, Set<Enum<?>>> sourcesMap = targetsLookup.get(target);
        if (sourcesMap != null) {
            for (Entry<QualifiedValue<?>, Set<Enum<?>>> entrySources : sourcesMap.entrySet()) {
                boolean contained = true;

                List<QualifiedValue<?>> validSources = fields.get(sourceKey);
                if (!validSources.contains(entrySources.getKey())) {
                    contained = false;
                } else {
                    for (Enum<?> qualifier : qualifiers) {
                        if (!entrySources.getValue().contains(qualifier)) {
                            contained = false;
                            break;
                        }
                    }
                }

                if (contained) {
                    @SuppressWarnings("rawtypes")
                    QualifiedRelation<S, T> relation = new QualifiedRelation(entrySources.getKey(), target,  
                            entrySources.getValue());
                    results.add(relation);
                }
            }
        }

        return results;
    }    
    
    



    @Override
    public <N, S, T> Set<QualifiedRelation<S, T>> getTargetQualifiedRelations(QualifiedValue<S> source,
            TKey<N, T> targetKey, Enum<?>... qualifiers) {
        Set<QualifiedRelation<S, T>> results = new HashSet<QualifiedRelation<S,T>>();

        HashMap<QualifiedValue<?>, Set<Enum<?>>> targetsMap = sourcesLookup.get(source);
        if (targetsMap != null) {
            for (Entry<QualifiedValue<?>, Set<Enum<?>>> entryTargets : targetsMap.entrySet()) {
                boolean contained = true;

                List<QualifiedValue<?>> validTargets = fields.get(targetKey);
                if (!validTargets.contains(entryTargets.getKey())) {
                    contained = false;
                } else {
                    for (Enum<?> qualifier : qualifiers) {
                        if (!entryTargets.getValue().contains(qualifier)) {
                            contained = false;
                            break;
                        }
                    }
                }

                if (contained) {
                    @SuppressWarnings("rawtypes")
                    QualifiedRelation<S, T> relation = new QualifiedRelation(source, entryTargets.getKey(),  
                            entryTargets.getValue());
                    results.add(relation);
                }
            }
        }

        return results;

    }
    
    
    /**
     * @return available keys
     */
    public Set<TKey<?, ?>> getAvailableKeys() {
        return Collections.unmodifiableSet(new HashSet<TKey<?, ?>>(fields.keySet()));
    }

    /**
     * @return available relations
     */
    @SuppressWarnings("rawtypes")
    public Set<QualifiedRelation<?, ?>> getAvailableRelations() {
        Set<QualifiedRelation<?, ?>> relations = new HashSet<MetaDataRecord.QualifiedRelation<?, ?>>();
        for (Entry<QualifiedValue<?>, HashMap<QualifiedValue<?>, Set<Enum<?>>>> sourceEntry : sourcesLookup.entrySet()) {
            for (Entry<QualifiedValue<?>, Set<Enum<?>>> targetEntry : sourceEntry.getValue().entrySet()) {
                QualifiedRelation<?, ?> relation = new QualifiedRelation(sourceEntry.getKey(),
                        targetEntry.getKey(), targetEntry.getValue());
                relations.add(relation);

            }
        }
        return relations;
    }
    
    @Override
    public String toString() {
        try {
            StringBuilder sb=new StringBuilder();
            sb.append(String.format("{MetadataRecordBean id:%d col:%s",
                    getId(),
                    collection==null ? "" : collection.getMnemonic()
                    ));
            for(Entry<TKey<?, ?>, List<QualifiedValue<?>>> fld:fields.entrySet()) {
                sb.append(String.format("\n  [%s ",fld.getKey().getName()));
                if(fld.getValue().size()==1)
                    sb.append(String.format("%s]",fld.getValue().get(0).toString()));
                else {
                    for(QualifiedValue<?> v: fld.getValue()) 
                        sb.append(String.format("(%s)",v.toString()));
                    sb.append("]");
                }
            }
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            //safegard not to break anything
            return super.toString();
        }
    }
    
}

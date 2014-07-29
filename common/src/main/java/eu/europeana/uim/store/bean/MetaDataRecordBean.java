package eu.europeana.uim.store.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import java.util.Arrays;

/**
 * In-memory implemenation of {@link MetaDataRecord} that uses Long as ID. It is
 * supposed to be the core class for usage if there is no need for a special
 * implemenation due to special requirements of the storage backend.
 *
 * @param <I> unique ID
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
@SuppressWarnings("unchecked")
public class MetaDataRecordBean<I> extends AbstractEntityBean<I> implements MetaDataRecord<I>,
        Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * the collection that is responsible for this record
     */
    private Collection<I> collection;

    /**
     * unique external identifier
     */
    private String uniqueId;

    /**
     * holds for each key a list of known qualified values
     */
    private final Map<TKey<?, ?>, List<QualifiedValue<?>>> fields = new HashMap<>();

    /**
     * Maintain index in order to retain ordering. null: not calculated yet
     */
    private transient Integer nextOrderIndex = null;

    /**
     * Creates a new instance of this class.
     */
    public MetaDataRecordBean() {
        super();
    }

    /**
     * Creates a new instance of this class.
     *
     * @param id unique ID
     * @param collection the collection that is responsible for this record
     */
    public MetaDataRecordBean(I id, Collection<I> collection) {
        super(id);
        this.collection = collection;
    }

    /**
     * @return unique external identifier
     */
    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public Collection<I> getCollection() {
        return collection;
    }

    /**
     * @param collection the collection this mdr belongs to
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
                    result = (T) value.getValue();
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
                    result = (QualifiedValue<T>) value;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public <N, T> List<QualifiedValue<T>> getQualifiedValues(TKey<N, T> key, Enum<?>... qualifiers) {
        List<QualifiedValue<T>> result = new ArrayList<>();
        List<QualifiedValue<?>> values = fields.get(key);
        if (values != null && values.size() > 0) {
            for (QualifiedValue<?> value : values) {
                if (checkQualifier(value, qualifiers)) {
                    result.add((QualifiedValue<T>) value);
                }
            }
        }
        return result;
    }

    @Override
    public <N, T> List<T> getValues(TKey<N, T> key, Enum<?>... qualifiers) {
        List<T> result = new ArrayList<>();
        List<QualifiedValue<?>> values = fields.get(key);
        if (values != null && values.size() > 0) {
            for (QualifiedValue<?> value : values) {
                if (checkQualifier(value, qualifiers)) {
                    result.add((T) value.getValue());
                }
            }
        }
        return result;
    }

    @Override
    public <N, T> QualifiedValue<T> addValue(TKey<N, T> key, T value, Enum<?>... qualifiers) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Argument 'value' should not be null!");
        }

        Set<Enum<?>> quals = new HashSet<>();
        for (Enum<?> qualifier : qualifiers) {
            if (qualifier != null) {
                quals.add(qualifier);
            }
        }

        List<QualifiedValue<?>> values = fields.get(key);
        if (values == null) {
            values = new ArrayList<>();
            fields.put(key, values);
        }
        if (nextOrderIndex == null) {
            nextOrderIndex = calculateNextOrderIndex();
        }
        eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T> qualifiedValue = new QualifiedValue<>(
                value, quals, nextOrderIndex++);
        values.add(qualifiedValue);
        return qualifiedValue;
    }

    private int calculateNextOrderIndex() {
        int nextIndex = 0;
        for (List<QualifiedValue<?>> vals : fields.values()) {
            for (QualifiedValue<?> val : vals) {
                if (val.getOrderIndex() >= nextIndex) {
                    nextIndex = val.getOrderIndex() + 1;
                }
            }
        }
        return nextIndex;
    }

    /**
     * This should only be used during conversion, as in general addValue is the
     * way to fill a {@link MetaDataRecord}.
     *
     * @param <N> the namespace (type) in which the field is defined
     * @param <T> the runtime type of the values for this field
     * @param key typed key which holds namespace, name and type information
     * @param values list of qualified values set under the given key (overrides
     * exisiting entries)
     */
    public <N, T> void setValue(TKey<N, T> key, List<QualifiedValue<T>> values) {
        List<QualifiedValue<?>> oldValues = fields.get(key);
        if (oldValues != null) {
            throw new IllegalArgumentException(
                    "setValue should only be called be called once per tkey");
        }
        if (nextOrderIndex != null) {
            throw new IllegalArgumentException(
                    "setValue should not be called after addValue");
        }
        List<QualifiedValue<?>> sortedValues = new ArrayList<QualifiedValue<?>>(values);
        Collections.sort(sortedValues);
        fields.put(key, sortedValues);
    }

    @Override
    public <N, T> List<QualifiedValue<T>> deleteValues(TKey<N, T> key, Enum<?>... qualifiers) {
        List<QualifiedValue<T>> result = new ArrayList<>();

        List<QualifiedValue<?>> values = fields.remove(key);
        if (values != null && values.size() > 0) {
            List<QualifiedValue<?>> leftValues = new ArrayList<>();

            for (QualifiedValue<?> value : values) {
                if (checkQualifier(value, qualifiers)) {
                    result.add((QualifiedValue<T>) value);
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
     * holds relations starting from source nodes, giving back target nodes with
     * connected qualifications
     */
    private final Map<QualifiedValue<?>, Map<QualifiedValue<?>, Set<Enum<?>>>> sourcesLookup = new HashMap<>();

    /**
     * holds relations ending in target nodes, giving back source nodes with
     * connected qualifications
     */
    private final Map<QualifiedValue<?>, Map<QualifiedValue<?>, Set<Enum<?>>>> targetsLookup = new HashMap<>();

    @Override
    public <S, T> void addRelation(QualifiedValue<S> source, QualifiedValue<T> target,
            Enum<?>... qualifiers) {
        Set<Enum<?>> qualifierSet = new HashSet<>();
        qualifierSet.addAll(Arrays.asList(qualifiers));

        Map<QualifiedValue<?>, Set<Enum<?>>> targetsMap = sourcesLookup.get(source);
        if (targetsMap == null) {
            targetsMap = new HashMap<>();
            sourcesLookup.put(source, targetsMap);
        }
        targetsMap.put(target, qualifierSet);

        Map<QualifiedValue<?>, Set<Enum<?>>> sourcesMap = targetsLookup.get(source);
        if (sourcesMap == null) {
            sourcesMap = new HashMap<>();
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
            Map<QualifiedValue<?>, Map<QualifiedValue<?>, Set<Enum<?>>>> startLookup,
            Map<QualifiedValue<?>, Map<QualifiedValue<?>, Set<Enum<?>>>> syncLookup,
            QualifiedValue<T> value, Enum<?>... qualifiers) {
        Set<QualifiedValue<?>> rems = new HashSet<>();
        if (qualifiers.length == 0) {
            Map<QualifiedValue<?>, Set<Enum<?>>> remove = startLookup.remove(value);
            if (remove != null) {
                rems.addAll(remove.keySet());
            }
        } else {
            Map<QualifiedValue<?>, Set<Enum<?>>> targetsMap = startLookup.get(value);
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
                        break;
                    }
                }
                for (QualifiedValue<?> rem : rems) {
                    targetsMap.remove(rem);
                }
                if (targetsMap.isEmpty()) {
                    startLookup.remove(value);
                }
            }
        }

        for (QualifiedValue<?> rem : rems) {
            Map<QualifiedValue<?>, Set<Enum<?>>> sourcesMap = syncLookup.get(rem);
            if (sourcesMap != null) {
                sourcesMap.remove(value);
                if (sourcesMap.isEmpty()) {
                    syncLookup.remove(rem);
                }
            }
        }
    }

    @Override
    public <N, S, T> Set<QualifiedValue<T>> getTargetQualifiedValues(QualifiedValue<S> source,
            TKey<N, T> targetKey, Enum<?>... qualifiers) {
        Set<QualifiedValue<T>> results = new HashSet<>();

        Map<QualifiedValue<?>, Set<Enum<?>>> targetsMap = sourcesLookup.get(source);
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
                    results.add((QualifiedValue<T>) entryTargets.getKey());
                }
            }
        }

        return results;

    }

    @Override
    public <N, S, T> Set<QualifiedValue<S>> getSourceQualifiedValues(QualifiedValue<T> target,
            TKey<N, S> sourceKey, Enum<?>... qualifiers) {
        Set<QualifiedValue<S>> results = new HashSet<>();

        Map<QualifiedValue<?>, Set<Enum<?>>> sourcesMap = targetsLookup.get(target);
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
                    results.add((QualifiedValue<S>) entrySources.getKey());
                }
            }
        }

        return results;
    }

    @Override
    public <N, S, T> Set<QualifiedRelation<S, T>> getSourceQualifiedRelations(
            QualifiedValue<T> target, TKey<N, S> sourceKey, Enum<?>... qualifiers) {
        Set<QualifiedRelation<S, T>> results = new HashSet<>();

        Map<QualifiedValue<?>, Set<Enum<?>>> sourcesMap = targetsLookup.get(target);
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
                    QualifiedRelation<S, T> relation = new QualifiedRelation(entrySources.getKey(),
                            target, entrySources.getValue());
                    results.add(relation);
                }
            }
        }

        return results;
    }

    @Override
    public <N, S, T> Set<QualifiedRelation<S, T>> getTargetQualifiedRelations(
            QualifiedValue<S> source, TKey<N, T> targetKey, Enum<?>... qualifiers) {
        Set<QualifiedRelation<S, T>> results = new HashSet<>();

        Map<QualifiedValue<?>, Set<Enum<?>>> targetsMap = sourcesLookup.get(source);
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
                    QualifiedRelation<S, T> relation = new QualifiedRelation(source,
                            entryTargets.getKey(), entryTargets.getValue());
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
        return Collections.unmodifiableSet(new HashSet<>(fields.keySet()));
    }

    /**
     * @return available relations
     */
    @SuppressWarnings("rawtypes")
    public Set<QualifiedRelation<?, ?>> getAvailableRelations() {
        Set<QualifiedRelation<?, ?>> relations = new HashSet<>();
        for (Entry<QualifiedValue<?>, Map<QualifiedValue<?>, Set<Enum<?>>>> sourceEntry : sourcesLookup.entrySet()) {
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
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("{MetadataRecordBean id:%s col:%s", getId(), collection == null
                    ? "" : collection.getMnemonic()));
            for (Entry<TKey<?, ?>, List<QualifiedValue<?>>> fld : fields.entrySet()) {
                sb.append(String.format("\n  [%s ", fld.getKey().getName()));
                if (fld.getValue().size() == 1) {
                    sb.append(String.format("%s]", fld.getValue().get(0).toString()));
                } else {
                    for (QualifiedValue<?> v : fld.getValue()) {
                        sb.append(String.format("(%s)", v.toString()));
                    }
                    sb.append("]");
                }
            }
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            // safegard not to break anything
            return super.toString();
        }
    }
}

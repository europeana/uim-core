package eu.europeana.uim.store.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
public class MetaDataRecordBean<I> extends AbstractEntityBean<I> implements MetaDataRecord<I> {
    /**
     * the collection that is responsible for this record
     */
    private Collection<I>                                collection;

    /**
     * holds for each key a list of known qualified values
     */
    private HashMap<TKey<?, ?>, List<QualifiedValue<?>>> fields         = new HashMap<TKey<?, ?>, List<QualifiedValue<?>>>();

    /**
     * Maintain index in order to retain ordering. null: not calculated yet
     */
    private transient Integer                            nextOrderIndex = null;

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
                if (value.getQualifiers().containsAll(Arrays.asList(qualifiers))) {
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
                if (value.getQualifiers().containsAll(Arrays.asList(qualifiers))) {
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
                if (value.getQualifiers().containsAll(Arrays.asList(qualifiers))) {
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
                if (value.getQualifiers().containsAll(Arrays.asList(qualifiers))) {
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
            if (qualifier == null) { throw new IllegalArgumentException(
                    "Argument 'qualifiers' should not have null entries!"); }
            quals.add(qualifier);
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
     * This should only be used during conversion, as in general addvalue is the way to fill a
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
                if (qualifiers != null && qualifiers.length > 0) {
                    boolean removed = true;
                    
                    for (Enum<?> qualifier : qualifiers)  {
                        if(!value.getQualifiers().contains(qualifier)) {
                            removed = false;
                        }
                    }
                    
                    if (removed) {
                        result.add((QualifiedValue<T>)value);
                    } else {
                        leftValues.add(value);
                    }
                } else {
                    result.add((QualifiedValue<T>)value);
                }
            }
            
            if (leftValues.size() > 0) {
                fields.put(key, leftValues);
            }
        }
        
        return result;
    }

    /**
     * @return available keys
     */
    public Set<TKey<?, ?>> getAvailableKeys() {
        return Collections.unmodifiableSet(new HashSet<TKey<?, ?>>(fields.keySet()));
    }
}

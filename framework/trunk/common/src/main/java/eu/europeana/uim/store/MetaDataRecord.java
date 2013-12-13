package eu.europeana.uim.store;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import eu.europeana.uim.common.TKey;

/**
 * This interface defines a highly dynamic model of records consisting of metadata. A meta data
 * record represents the fines unit of a record in the Europeana and The European Library sense.
 * 
 * @param <I>
 *            generic ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface MetaDataRecord<I> extends UimDataSet<I> {
    /**
     * @return the collection the record belongs to
     */
    Collection<I> getCollection();

    /**
     * Retrieves the first value for this key and qualifiers.
     * 
     * @param <N>
     *            the namespace (type) in which the field is defined
     * @param <T>
     *            the runtime type of the values for this field
     * @param key
     *            typed key which holds namespace, name and type information
     * @param qualifiers
     *            information typed by enumerations to provide further filtered data
     * @return first field qualified with the given qualifiers or null (no field or no one matching
     *         all provided qualifiers)
     */
    <N, T> T getFirstValue(TKey<N, T> key, Enum<?>... qualifiers);

    /**
     * Retrieves the first qualified value for this key and qualifiers.
     * 
     * @param <N>
     *            the namespace (type) in which the field is defined
     * @param <T>
     *            the runtime type of the values for this field
     * @param key
     *            typed key which holds namespace, name and type information
     * @param qualifiers
     *            information typed by enumerations to provide further filtered data
     * @return first field qualified with the given qualifiers or null (no field or no one matching
     *         all provided qualifiers)
     */
    <N, T> QualifiedValue<T> getFirstQualifiedValue(TKey<N, T> key, Enum<?>... qualifiers);

    /**
     * Retrieves all field values of this key (qualified and unqualified fields).
     * 
     * @param <N>
     *            the namespace (type) in which the field is defined
     * @param <T>
     *            the runtime type of the values for this field
     * @param key
     *            typed key which holds namespace, name and type information
     * @param qualifiers
     *            information typed by enumerations to provide additional data
     * @return values as list of qualified values (value + known qualifiers)
     */
    <N, T> List<QualifiedValue<T>> getQualifiedValues(TKey<N, T> key, Enum<?>... qualifiers);

    /**
     * Retrieves as list the field values matching the optional qualifiers and key.
     * 
     * @param <N>
     *            the namespace (type) in which the field is defined
     * @param <T>
     *            the runtime type of the values for this field
     * @param key
     *            typed key which holds namespace, name and type information
     * @param qualifiers
     *            information typed by enumerations to provide additional data
     * @return the list of values qualified with the given qualifier
     */
    <N, T> List<T> getValues(TKey<N, T> key, Enum<?>... qualifiers);

    /**
     * Adds value to the list of values under the specified key and optional qualifiers.
     * 
     * @param <N>
     *            the namespace (type) in which the field is defined
     * @param <T>
     *            the runtime type of the values for this field
     * @param key
     *            typed key which holds namespace, name and type information
     * @param value
     *            object typed using the type specified in the key
     * @param qualifiers
     *            information typed by enumerations to provide additional data
     */
    <N, T> void addValue(TKey<N, T> key, T value, Enum<?>... qualifiers);

    /**
     * Deletes all values known under the given typed key and returns this list of values.
     * 
     * @param <N>
     *            the namespace (type) in which the field is defined
     * @param <T>
     *            the runtime type of the values for this field
     * @param key
     *            typed key which holds namespace, name and type information
     * @param qualifiers
     *            optional qualifiers, if true only matching values will be removed
     * @return values that have just been removed as list of qualified values
     */
    <N, T> List<QualifiedValue<T>> deleteValues(TKey<N, T> key, Enum<?>... qualifiers);
    
    /**
     * Delete a specific qualified value.
     * 
     * @param <N>
     *            the namespace (type) in which the field is defined
     * @param <T>
     *            the runtime type of the values for this field
     * @param key
     *            typed key which holds namespace, name and type information
     * @param remove
     *            value that should be removed
     * @return Successfull?
     */
    <N, T> boolean deleteValue(TKey<N, T> key, QualifiedValue<T> remove);

    /**
     * Small class holding information of values with qualification (might be null, if there are
     * none).
     * 
     * @param <T>
     *            generic type of value
     * 
     * @author Markus Muhr (markus.muhr@kb.nl)
     * @since Mar 21, 2011
     */
    public class QualifiedValue<T> implements Comparable<QualifiedValue<?>>, Serializable {
        private static final long  serialVersionUID = 1L;

        /**
         * generic value
         */
        private final T            value;
        /**
         * how the given value has been qualified
         */
        private final Set<Enum<?>> qualifiers;
        /**
         * index of the value within a single record
         */
        private final int          orderIndex;

        /**
         * Creates a new instance of this class.
         * 
         * @param value
         *            generic value
         * @param qualifiers
         *            how the given value has been qualified
         * @param orderIndex
         *            order of the value within the record
         */
        public QualifiedValue(T value, Set<Enum<?>> qualifiers, int orderIndex) {
            this.value = value;
            this.qualifiers = qualifiers;
            this.orderIndex = orderIndex;
        }

        /**
         * @return generic value
         */
        public T getValue() {
            return value;
        }

        /**
         * @return how the given value has been qualified
         */
        public Set<Enum<?>> getQualifiers() {
            return qualifiers;
        }

        /**
         * @param <A>
         *            The qualifier class to get
         * @param qualifierType
         *            The qualifier class to get
         * @return the qualifier value, null if the qualifier is not present
         */
        @SuppressWarnings("unchecked")
        public <A extends Enum<?>> A getQualifier(Class<A> qualifierType) {
            if (qualifiers == null) return null;
            for (Enum<?> qualifier : qualifiers) {
                if (qualifier.getClass().equals(qualifierType)) return (A)qualifier;
            }
            return null;
        }

        /**
         * @return order index
         */
        public int getOrderIndex() {
            return orderIndex;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public int compareTo(QualifiedValue<?> other) {
            if (orderIndex < other.orderIndex) return -1;
            if (orderIndex > other.orderIndex) return 1;
            if (value instanceof Comparable<?>) { return ((Comparable)value).compareTo(other); }
            return 0;
        }
        

        @Override
        public String toString() {
            try {
                StringBuilder sb=new StringBuilder();
                for (Enum<?> qualifier : qualifiers) 
                    sb.append(qualifier.name()).append(" ");
                sb.append(value.toString());
                return sb.toString();
            } catch (Exception e) {
                //safegard not to break anything
                return super.toString();
            }
        }
        
    }

    // modeling structural information between qualified values
    /**
     * Adds a relation between the source and the target values to model structural information.
     * 
     * @param <S>
     *            the runtime type of the values for the source value
     * @param <T>
     *            the runtime type of the values for the target value
     * @param source
     *            a qualified value determining the start point of the relation
     * @param target
     *            a qualified value determining the end point of the relation
     * @param qualifiers
     *            information typed by enumerations to provide semantic context (e.g. time instant
     *            is connected to place as publication for example)
     */
    <S, T> void addRelation(QualifiedValue<S> source, QualifiedValue<T> target,
            Enum<?>... qualifiers);

    /**
     * Deletes all relations known starting or ending in the provided qualified value.
     * 
     * @param <T>
     *            the runtime type of the values for this field
     * @param value
     *            a qualified value determining start or end point of all the relations to be
     *            removed
     * @param qualifiers
     *            optional qualifiers, if true only matching relations will be removed
     */
    <T> void deleteRelations(QualifiedValue<T> value, Enum<?>... qualifiers);

    /**
     * Retrieves as list the qualified field values which are end points of a relation starting at
     * the given source value. Furthermore, the targets are filtered using the given (optional)
     * qualifiers.
     * 
     * @param <N>
     *            the namespace (type) in which the field is defined
     * @param <S>
     *            the runtime type of the values for the source value
     * @param <T>
     *            the runtime type of the values for the target value
     * @param source
     *            a qualified value determining the start point of the relation
     * @param targetKey
     *            typed key which holds namespace, name and type information that the target values
     *            should be
     * @param qualifiers
     *            information typed by enumerations to provide semantic context (e.g. time instant
     *            is connected to place as publication for example)
     * @return the list of qualified values
     */
    <N, S, T> Set<QualifiedValue<T>> getTargetQualifiedValues(QualifiedValue<S> source,
            TKey<N, T> targetKey, Enum<?>... qualifiers);

    /**
     * Retrieves as list the qualified field values which are start points of a relation ending in
     * the given target value. Furthermore, the targets are filtered using the given (optional)
     * qualifiers.
     * 
     * @param <N>
     *            the namespace (type) in which the field is defined
     * @param <S>
     *            the runtime type of the values for the source value
     * @param <T>
     *            the runtime type of the values for the target value
     * @param target
     *            a qualified value determining the end point of the relation
     * @param sourceKey
     *            typed key which holds namespace, name and type information that the source values
     *            should be
     * @param qualifiers
     *            information typed by enumerations to provide semantic context (e.g. time instant
     *            is connected to place as publication for example)
     * @return the list of qualified values
     */
    <N, S, T> Set<QualifiedValue<S>> getSourceQualifiedValues(QualifiedValue<T> target,
            TKey<N, S> sourceKey, Enum<?>... qualifiers);

    /**
     * Small class holding information of relations including qualification.
     * 
     * @param <S>
     *            generic type of source value
     * @param <T>
     *            generic type of target value
     * 
     * @author Markus Muhr (markus.muhr@kb.nl)
     * @since Aug 13, 2012
     */
    public class QualifiedRelation<S, T> implements Serializable {
        private static final long       serialVersionUID = 1L;

        /**
         * a qualified value determining the start point of the relation
         */
        private final QualifiedValue<S> source;
        /**
         * a qualified value determining the end point of the relation
         */
        private final QualifiedValue<T> target;
        /**
         * how the given value has been qualified
         */
        private final Set<Enum<?>>      qualifiers;

        /**
         * Creates a new instance of this class.
         * 
         * @param source
         *            a qualified value determining the start point of the relation
         * @param target
         *            a qualified value determining the end point of the relation
         * @param qualifiers
         *            how the relation has been qualified
         */
        public QualifiedRelation(QualifiedValue<S> source, QualifiedValue<T> target,
                                 Set<Enum<?>> qualifiers) {
            this.source = source;
            this.target = target;
            this.qualifiers = qualifiers;
        }

        /**
         * @return a qualified value determining the start point of the relation
         */
        public QualifiedValue<S> getSource() {
            return source;
        }

        /**
         * @return a qualified value determining the end point of the relation
         */
        public QualifiedValue<T> getTarget() {
            return target;
        }

        /**
         * @return how the given value has been qualified
         */
        public Set<Enum<?>> getQualifiers() {
            return qualifiers;
        }

        /**
         * @param <A>
         *            The qualifier class to get
         * @param qualifierType
         *            The qualifier class to get
         * @return the qualifier value, null if the qualifier is not present
         */
        @SuppressWarnings("unchecked")
        public <A extends Enum<?>> A getQualifier(Class<A> qualifierType) {
            if (qualifiers == null) return null;
            for (Enum<?> qualifier : qualifiers) {
                if (qualifier.getClass().equals(qualifierType)) return (A)qualifier;
            }
            return null;
        }
    }
    
}

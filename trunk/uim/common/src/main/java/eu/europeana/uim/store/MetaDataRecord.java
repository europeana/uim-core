package eu.europeana.uim.store;

import java.util.List;
import java.util.Set;

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
     * @return values that have just been removed as list of qualified values
     */
    <N, T> List<QualifiedValue<T>> deleteValues(TKey<N, T> key);

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
    public class QualifiedValue<T> implements Comparable<QualifiedValue<?>> {
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
    }
}

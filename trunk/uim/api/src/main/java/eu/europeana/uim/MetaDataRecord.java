package eu.europeana.uim;

import java.util.List;
import java.util.Set;

import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Request;

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
public interface MetaDataRecord<I> extends DataSet<I> {
    /**
     * @return the request in which this record is valid
     */
    Request<I> getRequest();

    /**
     * Retrieves the first value of the list of values represented under that key qualified or
     * unqualified. In other words the first unqualified field is always the same value no matter if
     * it used with a qualifier or not.
     * 
     * @param <N>
     *            the namespace (type) in which the field is defined
     * @param <T>
     *            the runtime type of the values for this field
     * @param key
     *            typed key which holds namespace, name and type information
     * @return the first value or null
     */
    <N, T> T getFirstField(TKey<N, T> key);

    /**
     * Retrieves the first field value for this key and qualifiers.
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
    <N, T> T getFirstQField(TKey<N, T> key, Set<Enum<?>> qualifiers);

    /**
     * Retrieves all field values of this key (qualified and unqualified fields).
     * 
     * @param <N>
     *            the namespace (type) in which the field is defined
     * @param <T>
     *            the runtime type of the values for this field
     * @param key
     *            typed key which holds namespace, name and type information
     * @return values as list of qualified values (value + known qualifiers)
     */
    <N, T> List<QualifiedValue<T>> getField(TKey<N, T> key);
    
    /**
     * Retrieves as list the field values matching the given qualifiers and key.
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
    <N, T> List<T> getQField(TKey<N, T> key, Set<Enum<?>> qualifiers);
    
    /**
     * Retrieves all field values of this key without qualifier information.
     * 
     * @param <N>
     *            the namespace (type) in which the field is defined
     * @param <T>
     *            the runtime type of the values for this field
     * @param key
     *            typed key which holds namespace, name and type information
     * @return values as list of values
     */
    <N, T> List<T> getPlainField(TKey<N, T> key);

    /**
     * Adds the value to the internal list of values under the specified key. Note, it can be
     * expected to get the same ordering back when calling getFields.
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
     *            optional information typed by enumerations to provide additional data (null or
     *            empty set means unqualified)
     */
    <N, T> void addField(TKey<N, T> key, T value);

    /**
     * Adds value to the list of values under the specified key and qualifiers.
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
    <N, T> void addQField(TKey<N, T> key, T value, Set<Enum<?>> qualifiers);

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
    <N, T> List<QualifiedValue<T>> deleteField(TKey<N, T> key);

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
    public class QualifiedValue<T> {
        /**
         * generic value
         */
        private final T            value;
        /**
         * how the given value has been qualified
         */
        private final Set<Enum<?>> qualifiers;

        /**
         * Creates a new instance of this class.
         * 
         * @param value
         *            generic value
         * @param qualifiers
         *            how the given value has been qualified
         */
        public QualifiedValue(T value, Set<Enum<?>> qualifiers) {
            this.value = value;
            this.qualifiers = qualifiers;
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
    }
}

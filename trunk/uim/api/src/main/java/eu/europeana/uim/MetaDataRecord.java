package eu.europeana.uim;

import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Request;

import java.io.Serializable;
import java.util.List;

/**
 * Abstract MetaDataRecord. StorageEngine implementations provide their own implementation of it.
 * A meta data record represents the fines unit of a record in the Europeana and The European Library 
 * sense.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface MetaDataRecord extends DataSet {
    
	/**
	 * @return the request in which this record is valid.
	 */
	Request getRequest();

	/** a unique record identifier 
	 * 
	 * @return the database wide unique identifier of this record.
	 */
    @Override
	String getIdentifier();

    
    /** setter for the first value in the list of values for the specified
     * key.
     * 
     * @param <N>
     * @param <T>
     * @param key
     * @param value
     */
    <N, T extends Serializable> void setFirstField(TKey<N, T> key, T value);

    /** getter for the first value of the list of values represented by that
     * key qualified or unqualified.
     * 
     * @param <N>
     * @param <T>
     * @param key
     * @return the first value or null
     */
    <N, T extends Serializable> T getFirstField(TKey<N, T> key);

    /** setter for the first qualified field value for this key.
     * 
     * @param <N>
     * @param <T>
     * @param key
     * @param qualifier
     * @param value
     */
    <N, T extends Serializable> void setFirstQField(TKey<N, T> key, String qualifier, T value);

    
    /** getter for the first qualified field value for this key and qualifier.
     * @param <N>
     * @param <T>
     * @param key
     * @param qualifier
     * @return the first
     */
    <N, T extends Serializable> T getFirstQField(TKey<N, T> key, String qualifier);

    /** method to add an unqualified field value.
     * @param <N>
     * @param <T>
     * @param key
     * @param value
     */
    <N, T extends Serializable> void addField(TKey<N, T> key, T value);

    /** getter for all field values of this key (including qualified fields)
     * @param <N>
     * @param <T>
     * @param key
     * @return the list of values, combination from all qualified and unqualified values
     */
    <N, T extends Serializable> List<T> getField(TKey<N, T> key);

    /** method to add a new qualified field
     * @param <N>
     * @param <T>
     * @param key
     * @param qualifier
     * @param value
     */
    <N, T extends Serializable> void addQField(TKey<N, T> key, String qualifier, T value);

    /** getter for the qualified field 
     * @param <N>
     * @param <T>
     * @param key
     * @param qualifier
     * @return the list of values qualified with the given qualifier
     */
    <N, T extends Serializable> List<T> getQField(TKey<N, T> key, String qualifier);

}

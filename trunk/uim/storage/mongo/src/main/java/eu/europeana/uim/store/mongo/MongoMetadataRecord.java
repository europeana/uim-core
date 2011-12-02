package eu.europeana.uim.store.mongo;


import java.util.HashMap;
import java.util.List;


import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.bean.MetaDataRecordBean;

/**
 * Mongo implementation of the {@link MetaDataRecord}. We make use of Mongo's document-storage
 * nature and store MDRs as basic documents (i.e. basic mongo db objects). In order to conform to
 * the {@link MetaDataRecord} interface, we wrap a {@link DBObject} which is the object persisted by
 * MongoDB.
 * 
 * We store unqualified fields with the prefix FIELD, qualified fields have as prefix the name() of
 * the qualifying Enum.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * 
 * FIXME: Qualifiers not handled correct.
 */

@Entity
public final class MongoMetadataRecord<I> extends MetaDataRecordBean<I> implements MetaDataRecord<I> {

	
    /**
     * unique ID as Long
     */
    private I id;
	
    /**
     * the collection that is responsible for this record
     */
	@Reference
    private MongodbCollection<I>  collection;

    /**
     * holds for each key a list of known qualified values
     */
    private HashMap<TKey<?, ?>, List<QualifiedValue<?>>> fields  = new HashMap<TKey<?, ?>, List<QualifiedValue<?>>>();

    /**
     * Maintain index in order to retain ordering. null: not calculated yet
     */
    private Integer  nextOrderIndex = null;
    
    
    /**
     * Creates a new instance of this class.
     * 
     * @param id
     *            unique ID
     * @param collection
     *            the collection that is responsible for this record
     */
    public MongoMetadataRecord(I id, Collection<I> collection) {
        super(id,collection);
        //this.collection = collection;
    }


}

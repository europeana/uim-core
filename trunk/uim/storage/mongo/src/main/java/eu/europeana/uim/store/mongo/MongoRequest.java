package eu.europeana.uim.store.mongo;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Request;

import java.util.Date;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
@Entity
public class MongoRequest extends AbstractMongoEntity implements Request {


    @Reference
    private MongodbCollection collection = null;

    private Date date = new Date();

    public MongoRequest() {
    }

    public MongoRequest(long id, MongodbCollection c) {
        super(id);
        this.collection = c;
    }

    public MongoRequest(long id, MongodbCollection c, Date d) {
        super(id);
        this.collection = c;
        this.date = d;
    }

	
	public String getIdentifier() {
		return "Request:" + getCollection().getMnemonic() + " " + getDate();
	}
	

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(MongodbCollection collection) {
        this.collection = collection;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MongoRequest that = (MongoRequest) o;

        if (!collection.equals(that.collection)) return false;
        if (!date.equals(that.date)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + collection.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}

package eu.europeana.uim.store.mongo;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import eu.europeana.uim.store.UimEntity;
import org.bson.types.ObjectId;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
@Entity
public class AbstractMongoEntity implements UimEntity {

    public static final String LID = "lid";

    @Id
    private ObjectId id;

    // long id for now
    private long lid;

    private String mnemonic;
    private String name;

    public long getId() {
        return lid;
    }

    public ObjectId getMongoId() {
        return id;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AbstractMongoEntity() {

    }

    public AbstractMongoEntity(long id) {
        this.lid = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractMongoEntity that = (AbstractMongoEntity) o;

        if (lid != that.lid) return false;
        if (mnemonic != null ? !mnemonic.equals(that.mnemonic) : that.mnemonic != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (lid ^ (lid >>> 32));
        result = 31 * result + (mnemonic != null ? mnemonic.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String toString() {
        return getMnemonic() + "\t" + getName();
    }

}

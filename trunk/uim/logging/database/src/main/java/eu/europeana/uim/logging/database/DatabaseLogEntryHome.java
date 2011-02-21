package eu.europeana.uim.logging.database;
// Generated Feb 9, 2011 10:08:39 PM by Hibernate Tools 3.2.0.CR1
// Template by: Andreas Juffinger


import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;


/**
 * Home object for domain model class DatabaseLogEntry.
 * @see eu.europeana.uim.logging.database.DatabaseLogEntry
 * @author Hibernate Tools
 */
 
public class DatabaseLogEntryHome {

    private static final Logger log = Logger.getLogger(DatabaseLogEntryHome.class.getName());
    
    @PersistenceContext
    protected EntityManager entityManager;
    
    @Transactional
    public DatabaseLogEntry update(DatabaseLogEntry entity) {
        return entityManager.merge(entity);
    }
    
    @Transactional
    public void insert(DatabaseLogEntry... entities) {
    	if (entities != null)  {
    		for (DatabaseLogEntry entity : entities) {
        		entityManager.persist(entity);
        	}
    	}
    }

    @Transactional
    public boolean delete(DatabaseLogEntry... entities) {
   		boolean deleted = false;
    	if (entities != null)  {
    		for (DatabaseLogEntry entity : entities) {
		    	if (entity.getOid() != null) {
		    		// need to reattach detached entity
		    		entity = entityManager.merge(entity);
		    		entityManager.remove(entity);
		    		deleted |= true;
		    	}
		    }
		}
        return deleted;
    }


    @Transactional
    public int truncate(){
   		Query query = entityManager.createQuery("DELETE FROM DatabaseLogEntry");
        return query.executeUpdate();
    }


    @Transactional
    public int execute(String statement){
   		Query query = entityManager.createQuery(statement);
        return query.executeUpdate();
    }


    @Transactional
    public DatabaseLogEntry findByOid(Long oid) {
    	if (oid == null) return null;
        try {
            return entityManager.find(DatabaseLogEntry.class, oid);
        } catch (NoResultException exc) {
            log.fine("DatabaseLogEntry not found for oid: " + oid);
            return null;
        }
    }
    

}


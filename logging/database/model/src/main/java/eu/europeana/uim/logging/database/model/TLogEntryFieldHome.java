package eu.europeana.uim.logging.database.model;
// Generated Sep 25, 2015 12:39:37 PM by Hibernate Tools 3.2.2.GA
// Template by: Andreas Juffinger


import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;


/**
 * Home object for domain model class TLogEntryField.
 * @see eu.europeana.uim.logging.database.model.TLogEntryField
 * @author Hibernate Tools
 */
 
public class TLogEntryFieldHome {

    private static final Logger log = Logger.getLogger(TLogEntryFieldHome.class.getName());
    
    @PersistenceContext
    protected EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
    	this.entityManager = entityManager;
    }
    
    public EntityManager getEntityManager() {
       	return this.entityManager;
    }
    
    @Transactional
    public TLogEntryField update(TLogEntryField entity) {
        return getEntityManager().merge(entity);
    }
    
    @Transactional
    public void insert(TLogEntryField... entities) {
    	if (entities != null)  {
    		for (TLogEntryField entity : entities) {
        		getEntityManager().persist(entity);
        	}
    	}
    }

    @Transactional
    public boolean delete(TLogEntryField... entities) {
   		boolean deleted = false;
    	if (entities != null)  {
    		for (TLogEntryField entity : entities) {
		    	if (entity.getOid() != null) {
		    		// need to reattach detached entity
		    		entity = getEntityManager().merge(entity);
		    		getEntityManager().remove(entity);
		    		deleted |= true;
		    	}
		    }
		}
        return deleted;
    }


    @Transactional
    public int truncate(){
   		Query query = getEntityManager().createQuery("DELETE FROM TLogEntryField");
        return query.executeUpdate();
    }


    @Transactional
    public int execute(String statement){
   		Query query = getEntityManager().createQuery(statement);
        return query.executeUpdate();
    }


    @Transactional
    public TLogEntryField findByOid(Long oid) {
    	if (oid == null) return null;
        try {
            return getEntityManager().find(TLogEntryField.class, oid);
        } catch (NoResultException exc) {
            log.fine("TLogEntryField not found for oid: " + oid);
            return null;
        }
    }
    






}


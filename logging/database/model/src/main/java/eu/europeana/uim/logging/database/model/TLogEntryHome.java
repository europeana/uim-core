package eu.europeana.uim.logging.database.model;
// Generated Sep 25, 2015 12:39:37 PM by Hibernate Tools 3.2.2.GA
// Template by: Andreas Juffinger


import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;


/**
 * Home object for domain model class TLogEntry.
 * @see eu.europeana.uim.logging.database.model.TLogEntry
 * @author Hibernate Tools
 */
 
public class TLogEntryHome {

    private static final Logger log = Logger.getLogger(TLogEntryHome.class.getName());
    
    @PersistenceContext
    protected EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
    	this.entityManager = entityManager;
    }
    
    public EntityManager getEntityManager() {
       	return this.entityManager;
    }
    
    @Transactional
    public TLogEntry update(TLogEntry entity) {
        return getEntityManager().merge(entity);
    }
    
    @Transactional
    public void insert(TLogEntry... entities) {
    	if (entities != null)  {
    		for (TLogEntry entity : entities) {
        		getEntityManager().persist(entity);
        	}
    	}
    }

    @Transactional
    public boolean delete(TLogEntry... entities) {
   		boolean deleted = false;
    	if (entities != null)  {
    		for (TLogEntry entity : entities) {
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
   		Query query = getEntityManager().createQuery("DELETE FROM TLogEntry");
        return query.executeUpdate();
    }


    @Transactional
    public int execute(String statement){
   		Query query = getEntityManager().createQuery(statement);
        return query.executeUpdate();
    }


    @Transactional
    public TLogEntry findByOid(Long oid) {
    	if (oid == null) return null;
        try {
            return getEntityManager().find(TLogEntry.class, oid);
        } catch (NoResultException exc) {
            log.fine("TLogEntry not found for oid: " + oid);
            return null;
        }
    }
    




    @SuppressWarnings("unchecked")
    @Transactional
    public List<TLogEntry> findByExecution(String executionId) {
        Query query = getEntityManager().createNamedQuery("eu.europeana.uim.logging.database.model.TLogEntry.findByExecution");
        query.setParameter("executionId", executionId);
        return (List<TLogEntry>) query.getResultList();
    }



    @SuppressWarnings("unchecked")
    @Transactional
    public List<TLogEntry> findByExecution(String executionId, int start, int limit) {
        Query query = getEntityManager().createNamedQuery("eu.europeana.uim.logging.database.model.TLogEntry.findByExecution");
        query.setParameter("executionId", executionId);
        query.setMaxResults(limit);
        query.setFirstResult(start);
        return (List<TLogEntry>) query.getResultList();
    }




}


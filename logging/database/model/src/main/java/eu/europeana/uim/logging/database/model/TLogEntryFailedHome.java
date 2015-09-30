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
 * Home object for domain model class TLogEntryFailed.
 * @see eu.europeana.uim.logging.database.model.TLogEntryFailed
 * @author Hibernate Tools
 */
 
public class TLogEntryFailedHome {

    private static final Logger log = Logger.getLogger(TLogEntryFailedHome.class.getName());
    
    @PersistenceContext
    protected EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
    	this.entityManager = entityManager;
    }
    
    public EntityManager getEntityManager() {
       	return this.entityManager;
    }
    
    @Transactional
    public TLogEntryFailed update(TLogEntryFailed entity) {
        return getEntityManager().merge(entity);
    }
    
    @Transactional
    public void insert(TLogEntryFailed... entities) {
    	if (entities != null)  {
    		for (TLogEntryFailed entity : entities) {
        		getEntityManager().persist(entity);
        	}
    	}
    }

    @Transactional
    public boolean delete(TLogEntryFailed... entities) {
   		boolean deleted = false;
    	if (entities != null)  {
    		for (TLogEntryFailed entity : entities) {
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
   		Query query = getEntityManager().createQuery("DELETE FROM TLogEntryFailed");
        return query.executeUpdate();
    }


    @Transactional
    public int execute(String statement){
   		Query query = getEntityManager().createQuery(statement);
        return query.executeUpdate();
    }


    @Transactional
    public TLogEntryFailed findByOid(Long oid) {
    	if (oid == null) return null;
        try {
            return getEntityManager().find(TLogEntryFailed.class, oid);
        } catch (NoResultException exc) {
            log.fine("TLogEntryFailed not found for oid: " + oid);
            return null;
        }
    }
    






    @SuppressWarnings("unchecked")
    @Transactional
    public List<TLogEntryFailed> findByExecution(String executionId) {
        Query query = getEntityManager().createNamedQuery("eu.europeana.uim.logging.database.model.TLogEntryFailed.findByExecution");
        query.setParameter("executionId", executionId);
        return (List<TLogEntryFailed>) query.getResultList();
    }



    @SuppressWarnings("unchecked")
    @Transactional
    public List<TLogEntryFailed> findByExecution(String executionId, int start, int limit) {
        Query query = getEntityManager().createNamedQuery("eu.europeana.uim.logging.database.model.TLogEntryFailed.findByExecution");
        query.setParameter("executionId", executionId);
        query.setMaxResults(limit);
        query.setFirstResult(start);
        return (List<TLogEntryFailed>) query.getResultList();
    }


}


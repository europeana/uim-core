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
 * Home object for domain model class TLogEntryDuration.
 * @see eu.europeana.uim.logging.database.model.TLogEntryDuration
 * @author Hibernate Tools
 */
 
public class TLogEntryDurationHome {

    private static final Logger log = Logger.getLogger(TLogEntryDurationHome.class.getName());
    
    @PersistenceContext
    protected EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
    	this.entityManager = entityManager;
    }
    
    public EntityManager getEntityManager() {
       	return this.entityManager;
    }
    
    @Transactional
    public TLogEntryDuration update(TLogEntryDuration entity) {
        return getEntityManager().merge(entity);
    }
    
    @Transactional
    public void insert(TLogEntryDuration... entities) {
    	if (entities != null)  {
    		for (TLogEntryDuration entity : entities) {
        		getEntityManager().persist(entity);
        	}
    	}
    }

    @Transactional
    public boolean delete(TLogEntryDuration... entities) {
   		boolean deleted = false;
    	if (entities != null)  {
    		for (TLogEntryDuration entity : entities) {
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
   		Query query = getEntityManager().createQuery("DELETE FROM TLogEntryDuration");
        return query.executeUpdate();
    }


    @Transactional
    public int execute(String statement){
   		Query query = getEntityManager().createQuery(statement);
        return query.executeUpdate();
    }


    @Transactional
    public TLogEntryDuration findByOid(Long oid) {
    	if (oid == null) return null;
        try {
            return getEntityManager().find(TLogEntryDuration.class, oid);
        } catch (NoResultException exc) {
            log.fine("TLogEntryDuration not found for oid: " + oid);
            return null;
        }
    }
    



    @SuppressWarnings("unchecked")
    @Transactional
    public List<TLogEntryDuration> findByPlugin(String module) {
        Query query = getEntityManager().createNamedQuery("eu.europeana.uim.logging.database.model.TLogEntryDuration.findByPlugin");
        query.setParameter("module", module);
        return (List<TLogEntryDuration>) query.getResultList();
    }



    @SuppressWarnings("unchecked")
    @Transactional
    public List<TLogEntryDuration> findByPlugin(String module, int start, int limit) {
        Query query = getEntityManager().createNamedQuery("eu.europeana.uim.logging.database.model.TLogEntryDuration.findByPlugin");
        query.setParameter("module", module);
        query.setMaxResults(limit);
        query.setFirstResult(start);
        return (List<TLogEntryDuration>) query.getResultList();
    }





}


package eu.europeana.uim.logging.database;

// Generated Feb 9, 2011 10:08:39 PM by Hibernate Tools 3.2.0.CR1
// Template by: Andreas Juffinger

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.store.Execution;

/**
 * Home object for domain model class DatabaseLogEntry.
 * 
 * @see eu.europeana.uim.logging.database.DatabaseLogEntry
 * @author Hibernate Tools
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 31, 2011
 */
public class DatabaseLogEntryHome {
    private static final Logger log = Logger.getLogger(DatabaseLogEntryHome.class.getName());

    /** to access the actual data in the backend **/
    @PersistenceContext
    protected EntityManager     entityManager;

    /**
     * @param entity
     * @return updated entry
     */
    @Transactional
    public DatabaseLogEntry<?> update(DatabaseLogEntry<?> entity) {
        return entityManager.merge(entity);
    }

    /**
     * @param entities
     *            newly created, added to the backend
     */
    @SuppressWarnings("rawtypes")
    @Transactional
    public void insert(DatabaseLogEntry... entities) {
        if (entities != null) {
            for (DatabaseLogEntry<?> entity : entities) {
                entityManager.persist(entity);
            }
        }
    }

    /**
     * @param entities
     * @return true, if the entities have been successfully removed
     */
    @SuppressWarnings("rawtypes")
    @Transactional
    public boolean delete(DatabaseLogEntry... entities) {
        boolean deleted = false;
        if (entities != null) {
            for (DatabaseLogEntry<?> entity : entities) {
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

    /**
     * @param entity
     * @return updated entry
     */
    @Transactional
    public DurationLogEntry update(DurationLogEntry entity) {
        return entityManager.merge(entity);
    }

    /**
     * @param entities
     *            newly created, added to the backend
     */
    @Transactional
    public void insert(DurationLogEntry... entities) {
        if (entities != null) {
            for (DurationLogEntry entity : entities) {
                entityManager.persist(entity);
            }
        }
    }

    /**
     * @param entities
     * @return true, if the entities have been successfully removed
     */
    @Transactional
    public boolean delete(DurationLogEntry... entities) {
        boolean deleted = false;
        if (entities != null) {
            for (DurationLogEntry entity : entities) {
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

    /**
     * @return delete all database entries
     */
    @Transactional
    public int truncate() {
        Query query = entityManager.createQuery("DELETE FROM DatabaseLogEntry");
        return query.executeUpdate();
    }

    /**
     * @param statement
     * @return executes an arbitrary query
     */
    @Transactional
    public int execute(String statement) {
        Query query = entityManager.createQuery(statement);
        return query.executeUpdate();
    }

    /**
     * @param oid
     * @return null, if there is no suche entity or the entity
     */
    @Transactional
    public DatabaseLogEntry<?> findByOid(Long oid) {
        if (oid == null) return null;
        try {
            return entityManager.find(DatabaseLogEntry.class, oid);
        } catch (NoResultException exc) {
            log.fine("DatabaseLogEntry not found for oid: " + oid);
            return null;
        }
    }

    /**
     * @param execution
     * @return data base entries connected to given execution
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public List<DatabaseLogEntry<?>> retrieveEntriesForExecution(Execution<Long> execution) {
        Query query = entityManager.createQuery("select d from uim_abstractlogentry d where d.executionId = :executionId");
        query.setParameter("executionId", execution.getId());
        return query.getResultList();
    }
    
    /**
     * @param plugin
     * @return duration entries connected to given plugin
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public List<DurationLogEntry> retrieveDurationEntries(IngestionPlugin plugin) {
        Query query = entityManager.createQuery("select d from uim_durationlogentry d where d.pluginName = :pluginName");
        query.setParameter("pluginName", plugin.getName());
        return query.getResultList();
    }
}

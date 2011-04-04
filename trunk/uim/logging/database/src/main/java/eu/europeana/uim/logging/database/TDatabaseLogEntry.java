package eu.europeana.uim.logging.database;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import eu.europeana.uim.api.LogEntry;
import eu.europeana.uim.api.LoggingEngine.Level;

/**
 * Implementation of {@link LogEntry} using JPA to persist the logging information to a data base.
 * 
 * @param <T>
 *            generic message
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 31, 2011
 */
@Entity
@Table(name = "uim_abstractlogentry")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "SEQ_UIM_LOGENTRY", sequenceName = "seq_uim_logentry")
@DiscriminatorColumn(name = "EMP_TYPE", discriminatorType = DiscriminatorType.INTEGER)
public abstract class TDatabaseLogEntry<T> implements LogEntry<Long, T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_UIM_LOGENTRY")
    private Long   oid;

    @Column
    private Level  level;

    @Column
    private Date   date;

    @Column
    private Long   executionId;

    @Column
    private String pluginName;

    @Column
    private Long   metaDataRecordId;

    /**
     * @return unique identifier used as primary key on database (is automatically set when
     *         persisted)
     */
    public Long getOid() {
        return oid;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    /**
     * @param level
     *            level of logging
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     *            date of creation
     */
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Long getExecutionId() {
        return executionId;
    }

    /**
     * @param executionId
     *            for which execution
     */
    public void setExecutionId(Long executionId) {
        this.executionId = executionId;
    }

    @Override
    public String getPluginName() {
        return pluginName;
    }

    /**
     * @param pluginName
     *            name of plugin
     */
    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    @Override
    public Long getMetaDataRecordId() {
        return metaDataRecordId;
    }

    /**
     * @param metaDataRecordId
     *            metadata record ID
     */
    public void setMetaDataRecordId(Long metaDataRecordId) {
        this.metaDataRecordId = metaDataRecordId;
    }
}

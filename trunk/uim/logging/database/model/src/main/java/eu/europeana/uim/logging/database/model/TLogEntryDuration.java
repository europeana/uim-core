package eu.europeana.uim.logging.database.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import eu.europeana.uim.api.LoggingEngine.LogEntryDuration;

/**
 * An entity representing a log entry concerning the duration of a plugin.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 31, 2011
 */
@Entity
@Table(name = "uim_logentry_duration")
@SequenceGenerator(name = "SEQ_UIM_LOGENTRY_DURATION", sequenceName = "seq_uim_logentry_duration")
public class TLogEntryDuration implements LogEntryDuration<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_UIM_LOGENTRY_DURATION")
    private Long   oid;

    @Column
    private Long   execution;

    @Column
    private String module;

    @Column
    private Date     date;

    @Column
    private Long   duration;

    /**
     * Creates a new instance of this class.
     */
    public TLogEntryDuration() {
    }

    /**
     * Creates a new instance of this class.
     * @param module
     * @param date 
     * @param duration
     */
    public TLogEntryDuration(String module, Date date, Long duration) {
        super();
        this.module = module;
        this.date = date;
        this.duration = duration;
    }

    /**
     * Creates a new instance of this class.
     * @param execution
     * @param module
     * @param date 
     * @param duration
     */
    public TLogEntryDuration(Long execution, String module, Date date, Long duration) {
        super();
        this.execution = execution;
        this.module = module;
        this.date = date;
        this.duration = duration;
    }

    /**
     * @return unique Name used as primary key on database (is automatically set when persisted)
     */
    public Long getOid() {
        return oid;
    }

    @Override
    public String getModule() {
        return module;
    }

    /**
     * @param module
     *            name of plugin
     */
    public void setModule(String module) {
        this.module = module;
    }
    
    @Override
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date to the given value.
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Long getDuration() {
        return duration;
    }

    /**
     * @param duration
     *            duration
     */
    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public Long getExecution() {
        return execution;
    }

    /**
     * Sets the execution to the given value.
     * @param execution the execution to set
     */
    public void setExecution(Long execution) {
        this.execution = execution;
    }
    
    
}

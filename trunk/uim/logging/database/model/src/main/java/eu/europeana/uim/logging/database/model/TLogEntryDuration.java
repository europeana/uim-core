package eu.europeana.uim.logging.database.model;

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
    private Long   duration;

    public TLogEntryDuration() {
    }

    public TLogEntryDuration(String module, Long duration) {
        super();
        this.module = module;
        this.duration = duration;
    }

    public TLogEntryDuration(Long execution, String module, Long duration) {
        super();
        this.execution = execution;
        this.module = module;
        this.duration = duration;
    }

    /**
     * @return unique Name used as primary key on database (is automatically set when persisted)
     */
    public Long getOid() {
        return oid;
    }

    /**
     * @return name of plugin
     */
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

    /**
     * @return duration
     */
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

    /**
     * Returns the execution.
     * @return the execution
     */
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

package eu.europeana.uim.logging.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * An entity representing a log entry concerning the duration of a plugin.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 31, 2011
 */
@Entity
@Table(name = "uim_durationlogentry")
@SequenceGenerator(name = "SEQ_UIM_DURATIONLOGENTRY", sequenceName = "seq_uim_durationlogentry")
public class TDurationDatabaseEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_UIM_DURATIONLOGENTRY")
    private Long   oid;

    @Column
    private String pluginName;

    @Column
    private Long   duration;

    /**
     * @return unique Name used as primary key on database (is automatically set when persisted)
     */
    public Long getOid() {
        return oid;
    }

    /**
     * @return name of plugin
     */
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
}

package eu.europeana.uim.logging.database.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import eu.europeana.uim.api.LoggingEngine.LogEntry;

/**
 * Implementation of simple log entry using JPA to persist the logging information to a data base.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 31, 2011
 */
@Entity
@Table(name = "uim_logentry")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "SEQ_UIM_LOGENTRY", sequenceName = "seq_uim_logentry")
public class TLogEntry implements LogEntry<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_UIM_LOGENTRY")
    private Long   oid;

    @Column
    private String module;

    @Column
    private Long   execution;

    @Column
    private Long   metaDataRecord;

    @Column
    private Level  level;

    @Column
    private Date   date;

    @Column
    private String   message0;

    @Column
    private String   message1;

    @Column
    private String   message2;

    @Column
    private String   message3;

    @Column
    private String   message4;

    @Column
    private String   message5;

    @Column
    private String   message6;

    @Column
    private String   message7;

    @Column
    private String   message8;

    @Column
    private String   message9;

    @Transient
    private String[] messages;
    
    public TLogEntry() {
    }
    
    public TLogEntry(Level level, String module, Date date, String... messages) {
        super();
        this.level = level;
        this.module = module;
        this.date = date;
        
        setMessage(messages);
    }


    public TLogEntry(Long execution, Level level, String module, Date date, String... messages) {
        super();
        this.execution = execution;
        this.level = level;
        this.module = module;
        this.date = date;
        
        setMessage(messages);
    }


    /**
     * @return unique identifier used as primary key on database (is automatically set when
     *         persisted)
     */
    public Long getOid() {
        return oid;
    }

    
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

    public Long getExecution() {
        return execution;
    }

    /**
     * @param execution
     *            for which execution
     */
    public void setExecution(Long execution) {
        this.execution = execution;
    }

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
     * @return
     */
    public Long getMetaDataRecord() {
        return metaDataRecord;
    }

    /**
     * @param metaDataRecord
     *            metadata record ID
     */
    public void setMetaDataRecord(Long metaDataRecord) {
        this.metaDataRecord = metaDataRecord;
    }

    public String[] getMessages() {
        if (messages == null) {
            List<String> msgs = new ArrayList<String>();
            if (message0 != null) {
                msgs.add(message0);
                if (message1 != null) {
                    msgs.add(message1);
                    if (message2 != null) {
                        msgs.add(message2);
                        if (message3 != null) {
                            msgs.add(message3);
                            if (message4 != null) {
                                msgs.add(message4);
                                if (message5 != null) {
                                    msgs.add(message5);
                                    if (message6 != null) {
                                        msgs.add(message6);
                                        if (message7 != null) {
                                            msgs.add(message7);
                                            if (message8 != null) {
                                                msgs.add(message8);
                                                if (message9 != null) {
                                                    msgs.add(message9);

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            messages = msgs.toArray(new String[msgs.size()]);
        }
        return messages;
    }
    
    
    /**
     * @param messages
     *            string messages, note that only maximum 10 messages are supported by this entry
     */
    protected void setMessage(String[] messages) {
        this.messages = messages;
        if (messages.length > 0) {
            message0 = messages[0];
        }
        if (messages.length > 1) {
            message1 = messages[1];
        }
        if (messages.length > 2) {
            message2 = messages[2];
        }
        if (messages.length > 3) {
            message3 = messages[3];
        }
        if (messages.length > 4) {
            message4 = messages[4];
        }
        if (messages.length > 5) {
            message5 = messages[5];
        }
        if (messages.length > 6) {
            message6 = messages[6];
        }
        if (messages.length > 7) {
            message7 = messages[7];
        }
        if (messages.length > 8) {
            message8 = messages[8];
        }
        if (messages.length > 9) {
            message9 = messages[9];
        }
    }
}

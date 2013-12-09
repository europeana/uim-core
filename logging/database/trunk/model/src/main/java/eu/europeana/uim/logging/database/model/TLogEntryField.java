package eu.europeana.uim.logging.database.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import eu.europeana.uim.logging.LoggingEngine.LogEntryField;

/**
 * Implementation of failed log entry using JPA to persist the logging information to a data base.
 * 
 * This is to large parts a duplication of @see {@field TLogEntry}. Given the expected high
 * numbers of log entries these tables/entities are not derived from each other.
 * 
 * They a kept separate to reduce the number of rows per table.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 31, 2011
 */
@Entity
@Table(name = "uim_logentry_field")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "SEQ_UIM_LOGENTRY_FIELD", sequenceName = "seq_uim_logentry_field")
public class TLogEntryField implements LogEntryField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_UIM_LOGENTRY_FIELD")
    private Long     oid;

    @Column
    private String   module;

    @Column
    private String   stringExecutionId;

    @Column
    private String   stringUimDatasetId;

    @Column
    private String   field;

    @Column
    private String   qualifier;

    @Column
    private Date     date;

    @Column
    private int      status;

    @Column(length = 4000)
    private String   message0;

    @Column(length = 4000)
    private String   message1;

    @Column(length = 4000)
    private String   message2;

    @Column(length = 4000)
    private String   message3;

    @Column(length = 4000)
    private String   message4;

    @Column(length = 4000)
    private String   message5;

    @Column(length = 4000)
    private String   message6;

    @Column(length = 4000)
    private String   message7;

    @Column(length = 4000)
    private String   message8;

    @Column(length = 4000)
    private String   message9;

    @Transient
    private String[] messages;

    /**
     * Creates a new instance of this class.
     */
    public TLogEntryField() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param module
     * @param field
     * @param qualifier
     * @param date
     * @param status
     * @param messages
     */
    public TLogEntryField(String module, String field, String qualifier, Date date, int status,
                          String... messages) {
        super();
        this.module = module;
        this.field = field;
        this.qualifier = qualifier;
        this.date = date;

        if (messages != null && messages.length > 0) {
            String[] localMessages = new String[messages.length];
            for (int i = 0; i < messages.length; i++) {
                if (messages[i].length() < 4000) {
                    localMessages[i] = messages[i];
                } else {
                    localMessages[i] = messages[i].substring(0, 3999);
                }
            }
            setMessage(localMessages);
        }
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param stringExecutionId
     * @param module
     * @param mdr
     * @param field
     * @param qualifier
     * @param date
     * @param status
     * @param messages
     */
    public TLogEntryField(String stringExecutionId, String module, String mdr, String field,
                          String qualifier, Date date, int status, String... messages) {
        super();
        this.stringExecutionId = stringExecutionId;
        this.status = status;
        this.module = module;
        this.stringUimDatasetId = mdr;
        this.field = field;
        this.qualifier = qualifier;
        this.date = date;

        if (messages != null && messages.length > 0) {
            String[] localMessages = new String[messages.length];
            for (int i = 0; i < messages.length; i++) {
                if (messages[i].length() < 4000) {
                    localMessages[i] = messages[i];
                } else {
                    localMessages[i] = messages[i].substring(0, 3999);
                }
            }
            setMessage(localMessages);
        }
    }

    /**
     * @return unique identifier used as primary key on database (is automatically set when
     *         persisted)
     */
    public Long getOid() {
        return oid;
    }

    @Override
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status to the given value.
     * 
     * @param status
     *            the status to set
     */
    public void setStatus(int status) {
        this.status = status;
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
    public String getStringExecutionId() {
        return stringExecutionId;
    }

    /**
     * @param stringExecutionId
     *            for which stringExecutionId
     */
    public void setStringExecutionId(String stringExecutionId) {
        this.stringExecutionId = stringExecutionId;
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
    public String getStringUimDatasetId() {
        return stringUimDatasetId;
    }

    /**
     * @param stringUimDatasetId
     *            data set ID
     */
    public void setStringUimDatasetId(String stringUimDatasetId) {
        this.stringUimDatasetId = stringUimDatasetId;
    }

    @Override
    public String getField() {
        return field;
    }

    /**
     * Sets the field to the given value.
     * 
     * @param field
     *            the field to set
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Returns the qualifier.
     * 
     * @return the qualifier
     */
    @Override
    public String getQualifier() {
        return qualifier;
    }

    /**
     * Sets the qualifier to the given value.
     * 
     * @param qualifier
     *            the qualifier to set
     */
    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    @Override
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

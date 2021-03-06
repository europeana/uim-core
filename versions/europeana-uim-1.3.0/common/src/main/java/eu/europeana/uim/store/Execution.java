package eu.europeana.uim.store;

import java.util.Date;
import java.util.Map;

/**
 * Interface for executions to retrieve information about start, end or cancellation time and the
 * data set to be worked on and the used workflow.
 * 
 * @param <I>
 *            generic ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface Execution<I> extends UimEntity<I> {
    /**
     * @return arbitrary name of this execution (must not be unique)
     */
    String getName();

    /**
     * @param name
     *            arbitrary name of this execution (must not be unique)
     */
    void setName(String name);

    /**
     * @return name of the underlying workflow
     */
    String getWorkflow();

    /**
     * @param identifier
     *            identifier of the underlying workflow
     */
    void setWorkflow(String identifier);

    /**
     * @return data set object on which the execution works
     */
    UimDataSet<I> getDataSet();

    /**
     * @param dataSet
     *            data set object on which the execution works
     */
    void setDataSet(UimDataSet<I> dataSet);

    /**
     * @return Is this execution active?
     */
    boolean isActive();

    /**
     * @param active
     *            Is this execution active?
     */
    void setActive(boolean active);

    /**
     * @return When has it started?
     */
    Date getStartTime();

    /**
     * @param start
     *            When has it started?
     */
    void setStartTime(Date start);

    /**
     * @return When has it ended?
     */
    Date getEndTime();

    /**
     * @param end
     *            When has it ended?
     */
    void setEndTime(Date end);

    /**
     * @return Has the execution been run through (false) or cancelled (true)? Only together with an
     *         end time this value makes sense, before that the execution is still running!
     */
    boolean isCanceled();

    /**
     * @param canceled
     *            Has the execution been run through (false) or cancelled (true)? Only together with
     *            an end time this value makes sense, before that the execution is still running!
     */
    void setCanceled(boolean canceled);

    /**
     * gives the number of tasks/records which are completly finished successful by all steps.
     * 
     * @return amount of completed tasks
     */
    int getSuccessCount();

    /**
     * @param number
     *            amount of completed tasks
     */
    void setSuccessCount(int number);

    /**
     * gives the number of tasks/records which have failed on the way through the workflow no matter
     * where.
     * 
     * @return amount of failures
     */
    int getFailureCount();

    /**
     * @param number
     *            amount of failures
     */
    void setFailureCount(int number);

    /**
     * gives the number of tasks/records which have been scheduled to be processed in the first
     * place. So scheduled = finished + failure.
     * 
     * @return amount of scheduled ones
     */
    int getProcessedCount();

    /**
     * @param number
     *            amount of scheduled ones
     */
    void setProcessedCount(int number);

    /**
     * string key,value pairs for arbitrary information on execution level
     * 
     * @param key
     * @param value
     */
    void putValue(String key, String value);

    /**
     * retrieve the string value for the specific key
     * 
     * @param key
     * @return the string value or null
     */
    String getValue(String key);

    /**
     * string key,value pairs for arbitrary information on execution level
     * 
     * @param key
     * @param value
     */
    void putValue(ControlledVocabularyKeyValue key, String value);

    /**
     * retrieve the string value for the specific key
     * 
     * @param key
     * @return the string value or null
     */
    String getValue(ControlledVocabularyKeyValue key);

    /**
     * @return the values map
     */
    Map<String, String> values();

    /**
     * @return the path to the log file
     */
    String getLogFile();

    /**
     * @param logfile
     */
    void setLogFile(String logfile);
}

package eu.europeana.uim.store;

import java.util.Date;

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
    String getWorkflowName();

    /**
     * @param name
     *            name of the underlying workflow
     */
    void setWorkflowName(String name);

    /**
     * @return data set object on which the execution works
     */
    DataSet<I> getDataSet();

    /**
     * @param dataSet
     *            data set object on which the execution works
     */
    void setDataSet(DataSet<I> dataSet);

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
    Boolean isCanceled();

    /**
     * @param canceled
     *            Has the execution been run through (false) or cancelled (true)? Only together with
     *            an end time this value makes sense, before that the execution is still running!
     */
    void setCanceled(Boolean canceled);
}

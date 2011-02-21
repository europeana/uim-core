package eu.europeana.uim.store;

import java.util.Date;

public interface Execution extends UimEntity {

    boolean isActive();
    void setActive(boolean active);

    Date getStartTime();
    void setStartTime(Date start);

    Date getEndTime();
    void setEndTime(Date end);

    DataSet getDataSet();
    void setDataSet(DataSet entity);

    String getWorkflowName();
    void setWorkflowName(String name);

}

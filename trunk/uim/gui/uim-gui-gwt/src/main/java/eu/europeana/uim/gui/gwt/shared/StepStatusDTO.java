package eu.europeana.uim.gui.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class StepStatusDTO implements IsSerializable {

    private String stepName, parentName;
    private int successes, failures;

    public StepStatusDTO() {

    }

    public StepStatusDTO(String stepName, String parentName, int successes, int failures) {
        this.stepName = stepName;
        this.parentName = parentName;
        this.successes = successes;
        this.failures = failures;
    }

    public String getStep() {
        return stepName;
    }

    public int successes() {
        return successes;
    }

    public int failures() {
        return failures;
    }

    public String getParentName() {
        return parentName;
    }
}

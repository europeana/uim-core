package org.theeuropeanlibrary.uim.gui.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Status of a step.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 28, 2011
 */
public class StepStatusDTO implements IsSerializable {

    private String stepName, parentName;
    private int    successes, failures;

    /**
     * Creates a new instance of this class.
     */
    public StepStatusDTO() {

    }

    /**
     * Creates a new instance of this class.
     * 
     * @param stepName
     * @param parentName
     * @param successes
     * @param failures
     */
    public StepStatusDTO(String stepName, String parentName, int successes, int failures) {
        this.stepName = stepName;
        this.parentName = parentName;
        this.successes = successes;
        this.failures = failures;
    }

    /**
     * @return name of step
     */
    public String getStep() {
        return stepName;
    }

    /**
     * @return successes count
     */
    public int successes() {
        return successes;
    }

    /**
     * @return failures count
     */
    public int failures() {
        return failures;
    }

    /**
     * @return parent name
     */
    public String getParentName() {
        return parentName;
    }
}

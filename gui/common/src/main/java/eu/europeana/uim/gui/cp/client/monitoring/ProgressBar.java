/* ProgressBar.java - created on Apr 11, 2014, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.client.monitoring;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * <P>
 * A simple progress bar that uses table elements to show progress and with a basic time remaining
 * calculation built in.
 * 
 * <P>
 * You can optionally display some text above the progress bar and/or display time remaining
 * underneath the progress bar. To control the display of those features, set the options in the
 * constructor as shown in the following usage example:
 * 
 * <PRE>
 * final ProgressBar progressBar = new ProgressBar(20, ProgressBar.SHOW_TIME_REMAINING +
 *                                                     ProgressBar.SHOW_TEXT);
 * progressBar.setText(&quot;Doing something...&quot;);
 * RootPanel.get().add(progressBar);
 * Timer t = new Timer() {
 *     public void run() {
 *         int progress = progressBar.getProgress() + 4;
 *         if (progress &gt; 100) cancel();
 *         progressBar.setProgress(progress);
 *     }
 * };
 * t.scheduleRepeating(1000);
 * </PRE>
 * 
 * <P>
 * How the time remaining is displayed can be controlled by setting the relevant messages using the
 * language of your choice.
 * 
 * <P>
 * The default setting for the messages are as follows:
 * 
 * <PRE>
 * setSecondsMessage(&quot;Time remaining: {0} Seconds&quot;);
 * setMinutesMessage(&quot;Time remaining: {0} Minutes&quot;);
 * setHoursMessage(&quot;Time remaining: {0} Hours&quot;);
 * </PRE>
 * 
 * <P>
 * To reset the time remaining/set the start time, simply set the progress to zero.
 * 
 * <P>
 * Some basic CSS styling is available to control the text, border around the progress bar itself
 * and the colour of the progress bar elements.
 * 
 * <PRE>
 * .progressbar-text {
 *     font-weight: bold;
 * }
 * 
 * .progressbar-remaining {
 *     font-size: 12px;
 *     font-style: italic;
 * }
 * 
 * .progressbar-outer {
 *     border: 1px solid black;
 * }
 * 
 * .progressbar-inner {
 *     border: 1px solid black;
 *     margin: 1px;
 * }
 * 
 * .progressbar-bar {
 *     width: 5px;
 *     height: 15px;
 *     margin: 1px;
 * }
 * 
 * .progressbar-fullbar {
 *     background: blue;
 * }
 * 
 * .progressbar-blankbar {
 *     background: #eee;
 * }
 * </PRE>
 * 
 * <P>
 * You can take advantage of the default style by adding the following to the head of your HTML
 * page.
 * 
 * <P>
 * &lt;link rel="stylesheet" type="text/css" href="style/gwl-progressBar.css">
 * 
 * <P>
 * This style sheet also has two additional styles which you can use by adding the stye name to the
 * widget. You can use either one of these, or use both combined.
 * 
 * <PRE>
 * ProgressBar progressBar = new ProgressBar(20);
 * progressBar.addStyleName(&quot;progressbar-solid&quot;);
 * progressBar.addStyleName(&quot;progressbar-noborder&quot;);
 * </PRE>
 * 
 * @author Bjarne Matzen - Bjarne[dot]Matzen[at]gmail[dot]com
 */
public class ProgressBar extends VerticalPanel {

    /**
     * Option to show text label above progress bar
     */
    public static final int SHOW_TEXT           = 2;

    /**
     * Option to show time remaining
     */
    public static final int SHOW_TIME_REMAINING = 1;

    /**
     * The time the progress bar was started
     */
    private long            startTime           = System.currentTimeMillis();

    /**
     * The number of bar elements to show
     */
    private int             elements            = 20;

    /**
     * Time element text
     */
    private String          secondsMessage      = "Time remaining: {0} Seconds";
    private String          minutesMessage      = "Time remaining: {0} Minutes";
    private String          hoursMessage        = "Time remaining: {0} Hours";

    /**
     * Current progress (as a percentage)
     */
    private int             progress            = 0;

    /**
     * This is the frame around the progress bar
     */
    private FlexTable       barFrame            = new FlexTable();

    /**
     * This is the grid used to show the elements
     */
    private Grid            elementGrid;

    /**
     * This is the current text label below the progress bar
     */
    private Label           remainLabel         = new Label();

    /**
     * This is the current text label above the progress bar
     */
    private Label           textLabel           = new Label();

    /**
     * internal flags for options
     */
    private boolean         showRemaining       = false;
    private boolean         showText            = false;

    /**
     * Base constructor for this widget
     * 
     * @param elements
     *            The number of elements (bars) to show on the progress bar
     * @param options
     *            The display options for the progress bar
     */
    public ProgressBar(int elements, int options) {
        // Read the options and set convenience variables
        if ((options & SHOW_TIME_REMAINING) == SHOW_TIME_REMAINING) showRemaining = true;
        if ((options & SHOW_TEXT) == SHOW_TEXT) showText = true;

        // Set element count
        this.elements = elements;

        // Styling
        remainLabel.setStyleName("progressbar-remaining");
        textLabel.setStyleName("progressbar-text");

        // Initialize the progress elements
        elementGrid = new Grid(1, elements);
        elementGrid.setStyleName("progressbar-inner");
        elementGrid.setCellPadding(0);
        elementGrid.setCellSpacing(0);

        for (int loop = 0; loop < elements; loop++) {
            Grid elm = new Grid(1, 1);
            // elm.setHTML(0, 0, "&nbsp;");
            elm.setHTML(0, 0, "");
            elm.setStyleName("progressbar-blankbar");
            elm.addStyleName("progressbar-bar");
            elementGrid.setWidget(0, loop, elm);
        }

        // Create the container around the elements
        Grid containerGrid = new Grid(1, 1);
        containerGrid.setCellPadding(0);
        containerGrid.setCellSpacing(0);
        containerGrid.setWidget(0, 0, elementGrid);
        containerGrid.setStyleName("progressbar-outer");
        // containerGrid.setBorderWidth(1);

        // Set up the surrounding flex table based on the options
        int row = 0;
        if (showText) barFrame.setWidget(row++, 0, textLabel);
        barFrame.setWidget(row++, 0, containerGrid);
        if (showRemaining) barFrame.setWidget(row++, 0, remainLabel);

        barFrame.setWidth("100%");

        // Add the frame to the panel
        this.add(barFrame);

        // Initialize progress bar
        setProgress(0);
    }

    /**
     * Constructor without options
     * 
     * @param elements
     *            The number of elements (bars) to show on the progress bar
     */
    public ProgressBar(int elements) {
        this(elements, 0);
    }

    /**
     * Set the current progress as a percentage
     * 
     * @param percentage
     *            Set current percentage for the progress bar
     */
    public void setProgress(int percentage) {
        // Make sure we are error-tolerant
        if (percentage > 100) { throw new IllegalArgumentException(
                "Percentage is not a valid number (between 0 and 100) '" + percentage + "'!"); }

        // Set the internal variable
        progress = percentage;

        // Update the elements in the progress grid to
        // reflect the status
        int completed = elements * percentage / 100;
        for (int loop = 0; loop < elements; loop++) {
            Grid elm = (Grid)elementGrid.getWidget(0, loop);
            if (loop < completed) {
                elm.setStyleName("progressbar-fullbar");
                elm.addStyleName("progressbar-bar");
            } else {
                elm.setStyleName("progressbar-blankbar");
                elm.addStyleName("progressbar-bar");
            }
        }

        if (percentage > 0) {
            // Calculate the new time remaining
            long soFar = (System.currentTimeMillis() - startTime) / 1000;
            long remaining = soFar * (100 - percentage) / percentage;
            // Select the best UOM
            String remainText = secondsMessage;
            if (remaining > 120) {
                remaining = remaining / 60;
                remainText = minutesMessage;
                if (remaining > 120) {
                    remaining = remaining / 60;
                    remainText = hoursMessage;
                }
            }
            // Locate the position to insert out time remaining
            int pos = remainText.indexOf("{0}");
            if (pos >= 0) {
                String trail = "";
                if (pos + 3 < remainText.length()) trail = remainText.substring(pos + 3);
                remainText = remainText.substring(0, pos) + remaining + trail;
            }
            // Set the label
            remainLabel.setText(remainText);
        } else {
            // If progress is 0, reset the start time
            startTime = System.currentTimeMillis();
        }
    }

    /**
     * Get the current progress as a percentage
     * 
     * @return Current percentage for the progress bar
     */
    public int getProgress() {
        return (progress);
    }

    /**
     * Get the text displayed above the progress bar
     * 
     * @return the text
     */
    public String getText() {
        return this.textLabel.getText();
    }

    /**
     * Set the text displayed above the progress bar
     * 
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.textLabel.setText(text);
    }

    /**
     * Get the message used to format the time remaining text for hours
     * 
     * @return the hours message
     */
    public String getHoursMessage() {
        return hoursMessage;
    }

    /**
     * Set the message used to format the time remaining text below the progress bar. There are 3
     * messages used for hours, minutes and seconds respectively.
     * 
     * The message must contain a placeholder for the value. The placeholder must be {0}. For
     * example, the following is a valid message:
     * 
     * "Hours remaining: {0}"
     * 
     * @param hoursMessage
     *            the hours message to set
     */
    public void setHoursMessage(String hoursMessage) {
        this.hoursMessage = hoursMessage;
    }

    /**
     * Get the message used to format the time remaining text for minutes
     * 
     * @return the minutesMessage
     */
    public String getMinutesMessage() {
        return minutesMessage;
    }

    /**
     * Set the message used to format the time remaining text below the progress bar. There are 3
     * messages used for hours, minutes and seconds respectively.
     * 
     * The message must contain a placeholder for the value. The placeholder must be {0}. For
     * example, the following is a valid message:
     * 
     * "Minutes remaining: {0}"
     * 
     * @param minutesMessage
     *            the minutes message to set
     */
    public void setMinutesMessage(String minutesMessage) {
        this.minutesMessage = minutesMessage;
    }

    /**
     * Get the message used to format the time remaining text for seconds
     * 
     * @return the secondsMessage
     */
    public String getSecondsMessage() {
        return secondsMessage;
    }

    /**
     * Set the message used to format the time remaining text below the progress bar. There are 3
     * messages used for hours, minutes and seconds respectively.
     * 
     * The message must contain a placeholder for the value. The placeholder must be {0}. For
     * example, the following is a valid message:
     * 
     * "Seconds remaining: {0}"
     * 
     * @param secondsMessage
     *            the secondsMessage to set
     */
    public void setSecondsMessage(String secondsMessage) {
        this.secondsMessage = secondsMessage;
    }
}

package org.theeuropeanlibrary.uim.gui.gwt.client.content;

import org.theeuropeanlibrary.uim.gui.gwt.client.IngestionCockpitWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * Triggers execution.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class ExecutionTriggerWidget extends IngestionCockpitWidget {
    /**
     * The UiBinder interface used by this example.
     */
    interface Binder extends UiBinder<Widget, ExecutionTriggerWidget> {
    }

    /**
     * The CellTree.
     */
    @UiField(provided = true)
    CellTree     cellTree;

    /**
     * The contact form used to update contacts.
     */
    @UiField
    ExecutionForm executionForm;

    /**
     * The button used to generate more contacts.
     */
    @UiField
    Button       startButton;

    /**
     * Creates a new instance of this class.
     * 
     * @param name
     * @param description
     */
    public ExecutionTriggerWidget(String name, String description) {
        super(name, description);
    }

    /**
     * Initialize this example.
     */
    @Override
    public Widget onInitialize() {
// // Create a CellList.
// ContactCell contactCell = new ContactCell(images.contact());
//
// // Set a key provider that provides a unique key for each contact. If key is
// // used to identify contacts when fields (such as the name and address)
// // change.
// cellList = new CellList<ContactInfo>(contactCell, ContactDatabase.ContactInfo.KEY_PROVIDER);
// cellList.setPageSize(30);
// cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
// cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
//
// // Add a selection model so we can select cells.
// final SingleSelectionModel<ContactInfo> selectionModel = new SingleSelectionModel<ContactInfo>(
// ContactDatabase.ContactInfo.KEY_PROVIDER);
// cellList.setSelectionModel(selectionModel);
// selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
// public void onSelectionChange(SelectionChangeEvent event) {
// contactForm.setContact(selectionModel.getSelectedObject());
// }
// });

        // Create the UiBinder.
        Binder uiBinder = GWT.create(Binder.class);
        Widget widget = uiBinder.createAndBindUi(this);

// // Add the CellList to the data provider in the database.
// ContactDatabase.get().addDataDisplay(cellList);
//
// // Set the cellList as the display of the pagers. This example has two
// // pagers. pagerPanel is a scrollable pager that extends the range when the
// // user scrolls to the bottom. rangeLabelPager is a pager that displays the
// // current range, but does not have any controls to change the range.
// pagerPanel.setDisplay(cellList);
// rangeLabelPager.setDisplay(cellList);
//
// // Handle events from the generate button.
// generateButton.addClickHandler(new ClickHandler() {
// public void onClick(ClickEvent event) {
// ContactDatabase.get().generateContacts(50);
// }
// });

        return widget;
    }

    @Override
    protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(ExecutionTriggerWidget.class, new RunAsyncCallback() {

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}

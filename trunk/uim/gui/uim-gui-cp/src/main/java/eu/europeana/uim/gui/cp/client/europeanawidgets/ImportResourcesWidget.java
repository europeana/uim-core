/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europeana.uim.gui.cp.client.europeanawidgets;

import javax.xml.bind.Binder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.ListDataProvider;

import eu.europeana.uim.gui.cp.client.IngestionWidget;
import eu.europeana.uim.sugarcrmclient.plugin.objects.SugarCrmRecord;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.RetrievableField;


/**
 * 
 * 
 * @author Georgios Markakis
 *
 */
public class ImportResourcesWidget extends IngestionWidget {

	 /**
	   * The main CellTable.
	   */
	  @UiField(provided = true)
	  CellTable<SugarCrmRecord> cellTable;
	
	  /**
	   * The pager used to change the range of data.
	   */
	  @UiField(provided = true)
	  SimplePager pager;
  
	    /**
	     * The key provider that provides the unique ID of a contact.
	     */
	    public static final ProvidesKey<SugarCrmRecord> KEY_PROVIDER = new ProvidesKey<SugarCrmRecord>() {
	      public Object getKey(SugarCrmRecord item) {
	        return item == null ? null : item.getItemValue(RetrievableField.ID);
	      }
	    };
	  
	    private ListDataProvider<SugarCrmRecord> dataProvider = new ListDataProvider<SugarCrmRecord>();
	    
	    
	/**
	 * @param name
	 * @param description
	 */
	public ImportResourcesWidget(String name, String description) {
		super(name, description);

	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.gui.cp.client.IngestionWidget#onInitialize()
	 */
	@Override
	public Widget onInitialize() {
		   // Create a CellTable.

	    // Set a key provider that provides a unique key for each contact. If key is
	    // used to identify contacts when fields (such as the name and address)
	    // change.
		
	    cellTable = new CellTable<SugarCrmRecord>(KEY_PROVIDER);
	    cellTable.setWidth("100%", true);

	    // Attach a column sort handler to the ListDataProvider to sort the list.
	    ListHandler<SugarCrmRecord> sortHandler = new ListHandler<SugarCrmRecord>(dataProvider.getList());
	    cellTable.addColumnSortHandler(sortHandler);

	    // Create a Pager to control the table.
	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
	    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
	    pager.setDisplay(cellTable);

	    // Add a selection model so we can select cells.
	    final SelectionModel<SugarCrmRecord> selectionModel = new MultiSelectionModel<SugarCrmRecord>(KEY_PROVIDER);
	    cellTable.setSelectionModel(selectionModel,
	        DefaultSelectionEventManager.<SugarCrmRecord> createCheckboxManager());

	    // Initialize the columns.
	    //initTableColumns(selectionModel, sortHandler);

	    // Add the CellList to the adapter in the database.
	    //ContactDatabase.get().addDataDisplay(cellTable);

	    // Create the UiBinder.
	    //Binder uiBinder = GWT.create(Binder.class);
	    //Widget widget = uiBinder.createAndBindUi(this);

	    //return widget;
	    return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.gui.cp.client.IngestionWidget#asyncOnInitialize(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	protected void asyncOnInitialize(AsyncCallback<Widget> callback) {


	}

}

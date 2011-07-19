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

import java.util.Comparator;



import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.ListDataProvider;
import eu.europeana.uim.gui.cp.client.IngestionWidget;
import eu.europeana.uim.gui.cp.client.management.ResourceManagementWidget;
import eu.europeana.uim.gui.cp.client.services.RepositoryServiceAsync;
import eu.europeana.uim.gui.cp.client.services.ResourceServiceAsync;
import eu.europeana.uim.gui.cp.shared.SugarCRMRecordDTO;



/**
 * 
 * 
 * @author Georgios Markakis
 *
 */
public class ImportResourcesWidget extends IngestionWidget {

    private final RepositoryServiceAsync repositoryService;
    private final ResourceServiceAsync   resourceService;
	
    interface Binder extends UiBinder<Widget, ImportResourcesWidget> {
    }
	
	 /**
	   * The main CellTable.
	   */
	  @UiField(provided = true)
	  CellTable<SugarCRMRecordDTO> cellTable;
	
	  /**
	   * The pager used to change the range of data.
	   */
	  @UiField(provided = true)
	  SimplePager pager;
  
	  @UiField(provided = true)
	  Button button;
	    /**
	     * The key provider that provides the unique ID of a contact.
	     */
	    public static final ProvidesKey<SugarCRMRecordDTO> KEY_PROVIDER = new ProvidesKey<SugarCRMRecordDTO>() {
	      public Object getKey(SugarCRMRecordDTO item) {
	        return item == null ? null : item.getSugarCrmId();
	      }
	    };
	  
	    private ListDataProvider<SugarCRMRecordDTO> dataProvider = new ListDataProvider<SugarCRMRecordDTO>();
	    
	    
	    
		/**
		 * @param name
		 * @param description
		 */
		public ImportResourcesWidget(RepositoryServiceAsync repositoryService,
	            ResourceServiceAsync resourceService) {
	        super("Import Resources", "This view allows to import resources into UIM.");
	        this.repositoryService = repositoryService;
	        this.resourceService = resourceService;
	        


		}


	/* (non-Javadoc)
	 * @see eu.europeana.uim.gui.cp.client.IngestionWidget#onInitialize()
	 */
	@Override
	public Widget onInitialize() {
		
		button = new Button();
		button.setText("Import Selected");
		button.setTitle("Populate UIM and Repox with Data from SugarCrm");
		// Create a CellTable.

	    // Set a key provider that provides a unique key for each contact. If key is
	    // used to identify contacts when fields (such as the name and address)
	    // change.
		
	    cellTable = new CellTable<SugarCRMRecordDTO>(KEY_PROVIDER);
	    cellTable.setWidth("100%", true);

	    // Attach a column sort handler to the ListDataProvider to sort the list.
	    ListHandler<SugarCRMRecordDTO> sortHandler = new ListHandler<SugarCRMRecordDTO>(dataProvider.getList());
	    cellTable.addColumnSortHandler(sortHandler);

	    // Create a Pager to control the table.
	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
	    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
	    pager.setDisplay(cellTable);

	    // Add a selection model so we can select cells.
	    final SelectionModel<SugarCRMRecordDTO> selectionModel = new MultiSelectionModel<SugarCRMRecordDTO>(KEY_PROVIDER);
	    cellTable.setSelectionModel(selectionModel,
	        DefaultSelectionEventManager.<SugarCRMRecordDTO> createCheckboxManager());

	    // Initialize the columns.
	    initTableColumns(selectionModel, sortHandler);

	    // Add the CellList to the adapter in the database.
	    dataProvider.addDataDisplay(cellTable);
	    
	    // Create the UiBinder.
	    
	    Binder uiBinder = GWT.create(Binder.class);
	    Widget widget = (Widget) uiBinder.createAndBindUi(this);

	    return widget;

	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.gui.cp.client.IngestionWidget#asyncOnInitialize(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {

        GWT.runAsync(ImportResourcesWidget.class,new RunAsyncCallback() {
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
	
	
	  /**
	   * Add the columns to the table.
	   */
	  private void initTableColumns(
	      final SelectionModel<SugarCRMRecordDTO> selectionModel,
	      ListHandler<SugarCRMRecordDTO> sortHandler) {
	    // Checkbox column. This table will uses a checkbox column for selection.
	    // Alternatively, you can call cellTable.setSelectionEnabled(true) to enable
	    // mouse selection.
	    Column<SugarCRMRecordDTO, Boolean> checkColumn = new Column<SugarCRMRecordDTO, Boolean>(
	        new CheckboxCell(true, false)) {
	      @Override
	      public Boolean getValue(SugarCRMRecordDTO object) {
	        // Get the value from the selection model.
	        return selectionModel.isSelected(object);
	      }
	    };
	    cellTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
	    cellTable.setColumnWidth(checkColumn, 40, Unit.PX);

	    // First name.
	    Column<SugarCRMRecordDTO, String> firstNameColumn = new Column<SugarCRMRecordDTO, String>(
	        new TextCell()) {
	      @Override
	      public String getValue(SugarCRMRecordDTO object) {
	        return object.getSugarCrmId();
	      }
	    };
	    firstNameColumn.setSortable(true);
	    
	    sortHandler.setComparator(firstNameColumn, new Comparator<SugarCRMRecordDTO>() {
	      public int compare(SugarCRMRecordDTO o1, SugarCRMRecordDTO o2) {
	        return o1.getSugarCrmId().compareTo(o2.getSugarCrmId());
	      }
	    });
	    cellTable.addColumn(firstNameColumn, "ID");
	    firstNameColumn.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
	      public void update(int index, SugarCRMRecordDTO object, String value) {

	    	  dataProvider.refresh();
	      }
	    });
	    cellTable.setColumnWidth(firstNameColumn, 20, Unit.PCT);

	  }

	

}

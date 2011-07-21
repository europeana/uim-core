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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.ListDataProvider;
import eu.europeana.uim.gui.cp.client.IngestionWidget;
import eu.europeana.uim.gui.cp.client.management.ResourceManagementWidget;
import eu.europeana.uim.gui.cp.client.services.IntegrationSeviceProxyAsync;
import eu.europeana.uim.gui.cp.client.services.RepositoryServiceAsync;
import eu.europeana.uim.gui.cp.client.services.ResourceServiceAsync;
import eu.europeana.uim.gui.cp.shared.ImportResultDTO;
import eu.europeana.uim.gui.cp.shared.ParameterDTO;
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
    private final IntegrationSeviceProxyAsync integrationservice;
	
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
	  Button searchButton;
	  
	  @UiField(provided = true)
	  TextBox searchField;
	  
	  @UiField(provided = true)
	  Button importButton;
	  

	  DialogBox searchDialog;
	  
	  DialogBox importDialog;
	  
	  
	  
	    /**
	     * The key provider that provides the unique ID of a contact.
	     */
	    public static final ProvidesKey<SugarCRMRecordDTO> KEY_PROVIDER = new ProvidesKey<SugarCRMRecordDTO>() {
	      public Object getKey(SugarCRMRecordDTO item) {
	        return item == null ? null : item.getId();
	      }
	    };
	  
	    private ListDataProvider<SugarCRMRecordDTO> dataProvider = new ListDataProvider<SugarCRMRecordDTO>();
	    
	    
	    
		/**
		 * @param name
		 * @param description
		 */
		public ImportResourcesWidget(RepositoryServiceAsync repositoryService,
	            ResourceServiceAsync resourceService,
	            IntegrationSeviceProxyAsync integrationservice) {
	        super("Import Resources", "This view allows to import resources into UIM.");
	        this.repositoryService = repositoryService;
	        this.resourceService = resourceService;
	        this.integrationservice = integrationservice;
	        
		}


	/* (non-Javadoc)
	 * @see eu.europeana.uim.gui.cp.client.IngestionWidget#onInitialize()
	 */
	@Override
	public Widget onInitialize() {
		
		searchDialog = createDialogBox();
		
		
		searchButton = new Button();
		searchButton.setText("Search SugarCRM");
		searchButton.setTitle("Search SugarCRM for Records");
		
		searchButton.addClickHandler( new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDialog.center();
				performSearch();
			}
	          });
				
		searchField = new TextBox();
		searchField.setName("Search term");
		
		importButton = new Button();
		importButton.addClickHandler( new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				performImport();
			}
	          });
		
		importButton.setText("Import Selected");
		importButton.setTitle("Populate UIM and Repox with Data from SugarCrm");
		
		
		
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
	
	
	
	
	
	/*
	 * Private Methods
	 */
	
	
	/**
	 * 
	 */
	private void performImport(){
		
		List<SugarCRMRecordDTO> selList = new ArrayList<SugarCRMRecordDTO>();
		List<SugarCRMRecordDTO> currList = dataProvider.getList();
		
		
		SelectionModel<? super SugarCRMRecordDTO> model = cellTable.getSelectionModel();
		
		for(SugarCRMRecordDTO record:currList ){
			if(model.isSelected(record)){
				selList.add(record);
			}
		}
		
		if (!selList.isEmpty()){
			
			for(SugarCRMRecordDTO record : selList){
				integrationservice.processSelectedRecord(record, new AsyncCallback<ImportResultDTO>() {
		            @Override
		            public void onFailure(Throwable throwable) {
		                throwable.printStackTrace();
		                //searchDialog.hide();
		            }

		            @Override
		            public void onSuccess(ImportResultDTO searchresults) {
		            	//dataProvider.setList(searchresults);
		            	//searchDialog.hide();

		            }
		        });
			}
			
			
		}
		
	}
	
	
	
	
	/**
	 * 
	 */
	private void performSearch(){
		String query = generateQuery(); 
		integrationservice.executeSugarCRMQuery(query, new AsyncCallback<List<SugarCRMRecordDTO>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                searchDialog.hide();
            }

            @Override
            public void onSuccess(List<SugarCRMRecordDTO> searchresults) {
            	dataProvider.setList(searchresults);
            	searchDialog.hide();

            }
        });
	}
	
	
	
	
	
	private String generateQuery(){
		String query = "contacts.first_name LIKE '%M%'"; 
		
		return query;
		
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

	    // ID column
	    /*
	    Column<SugarCRMRecordDTO, String> idColumn = new Column<SugarCRMRecordDTO, String>(
	        new TextCell()) {
	      @Override
	      public String getValue(SugarCRMRecordDTO object) {
	        return object.getId();
	      }
	    };
	    idColumn.setSortable(true);
	    
	    sortHandler.setComparator(idColumn, new Comparator<SugarCRMRecordDTO>() {
	      public int compare(SugarCRMRecordDTO o1, SugarCRMRecordDTO o2) {
	        return o1.getId().compareTo(o2.getId());
	      }
	    });
	    cellTable.addColumn(idColumn, "ID");
	    idColumn.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
	      public void update(int index, SugarCRMRecordDTO object, String value) {

	    	  dataProvider.refresh();
	      }
	    });
	    cellTable.setColumnWidth(idColumn, 20, Unit.PCT);
	    */
	    
	    // Colection name Name Column
	    Column<SugarCRMRecordDTO, String> collectionColumn = new Column<SugarCRMRecordDTO, String>(
		        new TextCell()) {
		      @Override
		      public String getValue(SugarCRMRecordDTO object) {
		        return object.getName();
		      }
		    };
		    collectionColumn.setSortable(true);
		    
		    sortHandler.setComparator(collectionColumn, new Comparator<SugarCRMRecordDTO>() {
		      public int compare(SugarCRMRecordDTO o1, SugarCRMRecordDTO o2) {
		        return o1.getName().compareTo(o2.getName());
		      }
		    });
		    cellTable.addColumn(collectionColumn, "Collection Identifier");
		    collectionColumn.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
		      public void update(int index, SugarCRMRecordDTO object, String value) {

		    	  dataProvider.refresh();
		      }
		    });
		    cellTable.setColumnWidth(collectionColumn, 20, Unit.PCT);
	    
	    
	    // Organization Name Column
		    Column<SugarCRMRecordDTO, String> organizationColumn = new Column<SugarCRMRecordDTO, String>(
			        new TextCell()) {
			      @Override
			      public String getValue(SugarCRMRecordDTO object) {
			        return object.getOrganization_name();
			      }
			    };
			    collectionColumn.setSortable(true);
			    
			    sortHandler.setComparator(organizationColumn, new Comparator<SugarCRMRecordDTO>() {
			      public int compare(SugarCRMRecordDTO o1, SugarCRMRecordDTO o2) {
			        return o1.getOrganization_name().compareTo(o2.getOrganization_name());
			      }
			    });
			    cellTable.addColumn(organizationColumn, "Organization Name");
			    organizationColumn.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
			      public void update(int index, SugarCRMRecordDTO object, String value) {

			    	  dataProvider.refresh();
			      }
			    });
			    cellTable.setColumnWidth(organizationColumn, 20, Unit.PCT);
			    
	    // Country Column
	    
			    Column<SugarCRMRecordDTO, String> countryColumn = new Column<SugarCRMRecordDTO, String>(
				        new TextCell()) {
				      @Override
				      public String getValue(SugarCRMRecordDTO object) {
				        return object.getCountry_c();
				      }
				    };
				    countryColumn.setSortable(true);
				    
				    sortHandler.setComparator(countryColumn, new Comparator<SugarCRMRecordDTO>() {
				      public int compare(SugarCRMRecordDTO o1, SugarCRMRecordDTO o2) {
				        return o1.getCountry_c().compareTo(o2.getCountry_c());
				      }
				    });
				    cellTable.addColumn(countryColumn, "Country");
				    countryColumn.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
				      public void update(int index, SugarCRMRecordDTO object, String value) {

				    	  dataProvider.refresh();
				      }
				    });
				    cellTable.setColumnWidth(countryColumn, 20, Unit.PCT);
			    
	                // Status Column
				    Column<SugarCRMRecordDTO, String> statusColumn = new Column<SugarCRMRecordDTO, String>(
					        new TextCell()) {
					      @Override
					      public String getValue(SugarCRMRecordDTO object) {
					        return object.getStatus();
					      }
					    };
					    statusColumn.setSortable(true);
					    
					    sortHandler.setComparator(statusColumn, new Comparator<SugarCRMRecordDTO>() {
					      public int compare(SugarCRMRecordDTO o1, SugarCRMRecordDTO o2) {
					        return o1.getStatus().compareTo(o2.getStatus());
					      }
					    });
					    cellTable.addColumn(statusColumn, "Status");
					    statusColumn.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
					      public void update(int index, SugarCRMRecordDTO object, String value) {

					    	  dataProvider.refresh();
					      }
					    });
					    cellTable.setColumnWidth(statusColumn, 20, Unit.PCT);

					    
					    // Amount Column
	    
					    Column<SugarCRMRecordDTO, String> amountColumn = new Column<SugarCRMRecordDTO, String>(
						        new TextCell()) {
						      @Override
						      public String getValue(SugarCRMRecordDTO object) {
						        return object.getIngested_total_c();
						      }
						    };
						    amountColumn.setSortable(true);
						    
						    sortHandler.setComparator(amountColumn, new Comparator<SugarCRMRecordDTO>() {
						      public int compare(SugarCRMRecordDTO o1, SugarCRMRecordDTO o2) {
						        return o1.getIngested_total_c().compareTo(o2.getIngested_total_c());
						      }
						    });
						    cellTable.addColumn(amountColumn, "Amount of Ingested Objects");
						    amountColumn.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
						      public void update(int index, SugarCRMRecordDTO object, String value) {

						    	  dataProvider.refresh();
						      }
						    });
						    cellTable.setColumnWidth(amountColumn, 20, Unit.PCT);

						    // Ingestion Date Column
						    Column<SugarCRMRecordDTO, String> ingestionDateColumn = new Column<SugarCRMRecordDTO, String>(
							        new TextCell()) {
							      @Override
							      public String getValue(SugarCRMRecordDTO object) {
							        return object.getExpected_ingestion_date();
							      }
							    };
							    ingestionDateColumn.setSortable(true);
							    
							    sortHandler.setComparator(ingestionDateColumn, new Comparator<SugarCRMRecordDTO>() {
							      public int compare(SugarCRMRecordDTO o1, SugarCRMRecordDTO o2) {
							        return o1.getExpected_ingestion_date().compareTo(o2.getExpected_ingestion_date());
							      }
							    });
							    cellTable.addColumn(ingestionDateColumn, "Planned Ingestion Date");
							    ingestionDateColumn.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
							      public void update(int index, SugarCRMRecordDTO object, String value) {

							    	  dataProvider.refresh();
							      }
							    });
							    cellTable.setColumnWidth(ingestionDateColumn, 20, Unit.PCT);
	    
	    
							    // User Column
							    
							    Column<SugarCRMRecordDTO, String> userColumn = new Column<SugarCRMRecordDTO, String>(
								        new TextCell()) {
								      @Override
								      public String getValue(SugarCRMRecordDTO object) {
								        return object.getAssigned_user_name();
								      }
								    };
								    userColumn.setSortable(true);
								    
								    sortHandler.setComparator(userColumn, new Comparator<SugarCRMRecordDTO>() {
								      public int compare(SugarCRMRecordDTO o1, SugarCRMRecordDTO o2) {
								    	  
								        return o1.getAssigned_user_name().compareTo(o2.getAssigned_user_name());
								      }
								    });
								    
								    cellTable.addColumn(userColumn, "SugarCRM User");
								    userColumn.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
								      public void update(int index, SugarCRMRecordDTO object, String value) {

								    	  dataProvider.refresh();
								      }
								    });
								    cellTable.setColumnWidth(userColumn, 20, Unit.PCT);

	  }

	
	  
	  /**
	   * Create the dialog box for this example.
	   *
	   * @return the new dialog box
	   */
	  private DialogBox createDialogBox() {
	    // Create a dialog box and set the caption text
	    final DialogBox dialogBox = new DialogBox();
	    dialogBox.ensureDebugId("cwDialogBox");
	    dialogBox.setText("Searching for SugarCRM entries");

	    dialogBox.setModal(true);
	    //dialogBox.setSize("200", "100");
	    
	    // Create a table to layout the content
	    VerticalPanel dialogContents = new VerticalPanel();
	    dialogContents.setSpacing(4);
	    dialogBox.setWidget(dialogContents);

	    // Add some text to the top of the dialog
	    HTML details = new HTML("");
	    dialogContents.add(details);
	    dialogContents.setCellHorizontalAlignment(
	        details, HasHorizontalAlignment.ALIGN_CENTER);

	    // Add an image to the dialog
	    //Image image = new Image(Showcase.images.jimmy());
	    //dialogContents.add(image);
	    //dialogContents.setCellHorizontalAlignment(
	    //    image, HasHorizontalAlignment.ALIGN_CENTER);

	    
	    // Return the dialog box
	    return dialogBox;
	  }

}

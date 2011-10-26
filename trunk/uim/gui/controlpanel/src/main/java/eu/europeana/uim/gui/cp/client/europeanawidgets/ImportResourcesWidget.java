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
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.widgetideas.client.ProgressBar;

import eu.europeana.uim.gui.cp.client.IngestionWidget;
import eu.europeana.uim.gui.cp.client.services.IntegrationSeviceProxyAsync;
import eu.europeana.uim.gui.cp.client.services.RepositoryServiceAsync;
import eu.europeana.uim.gui.cp.client.services.ResourceServiceAsync;
import eu.europeana.uim.gui.cp.client.utils.EuropeanaClientConstants;
import eu.europeana.uim.gui.cp.client.utils.RecordStates;
import eu.europeana.uim.gui.cp.shared.ImportResultDTO;
import eu.europeana.uim.gui.cp.shared.SugarCRMRecordDTO;

/**
 * The component for importing information from SugarCRM
 * 
 * @author Georgios Markakis
 * 
 */
public class ImportResourcesWidget extends IngestionWidget{

	private final RepositoryServiceAsync repositoryService;
	private final ResourceServiceAsync resourceService;
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

	Button searchButton;

	@UiField(provided = true)
	VerticalPanel searchPanel;

	@UiField(provided = true)
	Button importButton;

	DialogBox searchDialog;

	DialogBox importDialog;

	FlexTable impResultsTable;

	ProgressBar progressBar;
	
	private ListDataProvider<SugarCRMRecordDTO> dataProvider = new ListDataProvider<SugarCRMRecordDTO>();
	
	
	
	/**
	 * The key provider that provides the unique ID of a contact.
	 */
	public static final ProvidesKey<SugarCRMRecordDTO> KEY_PROVIDER = new ProvidesKey<SugarCRMRecordDTO>() {
		public Object getKey(SugarCRMRecordDTO item) {
			return item == null ? null : item.getId();
		}
	};

	
	


	
	/**
	 * Constructor
	 * 
	 * @param repositoryService
	 * @param resourceService
	 * @param integrationservice
	 */
	public ImportResourcesWidget(RepositoryServiceAsync repositoryService,
			ResourceServiceAsync resourceService,
			IntegrationSeviceProxyAsync integrationservice) {
		super(EuropeanaClientConstants.PANELLABEL,
				EuropeanaClientConstants.PANELDESCRIPTION);
		this.repositoryService = repositoryService;
		this.resourceService = resourceService;
		this.integrationservice = integrationservice;

	}

	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.gui.cp.client.IngestionWidget#onInitialize()
	 */
	@Override
	public Widget onInitialize() {
		
		searchPanel = new VerticalPanel();
		searchPanel.setSpacing(8);
		searchPanel.setWidth("32em");
		searchPanel.add(createAdvancedForm());
		
		impResultsTable = new FlexTable();
		FlexCellFormatter cellFormatter = impResultsTable
				.getFlexCellFormatter();
		impResultsTable.addStyleName("cw-FlexTable");
		impResultsTable.setWidth("32em");
		impResultsTable.setCellSpacing(5);
		impResultsTable.setCellPadding(3);


		// Add some text
		cellFormatter.setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_LEFT);

		cellFormatter.setColSpan(0, 0, 2);

		searchDialog = createSearchDialogBox();
		importDialog = createImportDialog();

		importButton = new Button();
		importButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				importDialog.center();
				performImport();
			}
		});

		importButton.setText(EuropeanaClientConstants.IMPORTBUTTONLABEL);
		importButton.setTitle(EuropeanaClientConstants.IMPORTBUTTONTITLE);

		// Create a CellTable.

		// Set a key provider that provides a unique key for each contact. If
		// key is
		// used to identify contacts when fields (such as the name and address)
		// change.

		cellTable = new CellTable<SugarCRMRecordDTO>(KEY_PROVIDER);
		cellTable.setWidth("100%", true);

		// Attach a column sort handler to the ListDataProvider to sort the
		// list.
		ListHandler<SugarCRMRecordDTO> sortHandler = new ListHandler<SugarCRMRecordDTO>(
				dataProvider.getList());
		cellTable.addColumnSortHandler(sortHandler);

		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager.setDisplay(cellTable);

		// Add a selection model so we can select cells.
		final SelectionModel<SugarCRMRecordDTO> selectionModel = new MultiSelectionModel<SugarCRMRecordDTO>(
				KEY_PROVIDER);
		cellTable.setSelectionModel(selectionModel,
				DefaultSelectionEventManager
						.<SugarCRMRecordDTO> createCheckboxManager());

		// Initialize the columns.
		initTableColumns(selectionModel, sortHandler);

		// Add the CellList to the adapter in the database.
		dataProvider.addDataDisplay(cellTable);

		// Create the UiBinder.

		Binder uiBinder = GWT.create(Binder.class);
		Widget widget = (Widget) uiBinder.createAndBindUi(this);

		return widget;

	}

	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.gui.cp.client.IngestionWidget#asyncOnInitialize(com.
	 * google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {

		GWT.runAsync(ImportResourcesWidget.class, new RunAsyncCallback() {
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
	 * Performs an asynchronous import request.
	 */
	private void performImport() {

		List<SugarCRMRecordDTO> selList = new ArrayList<SugarCRMRecordDTO>();
		List<SugarCRMRecordDTO> currList = dataProvider.getList();

		SelectionModel<? super SugarCRMRecordDTO> model = cellTable
				.getSelectionModel();

		for (SugarCRMRecordDTO record : currList) {
			if (model.isSelected(record)) {
				selList.add(record);
			}
		}

		if (!selList.isEmpty()) {


			progressBar.setMaxProgress(selList.size());
			progressBar.setProgress(0);
			impResultsTable.removeAllRows();
			
			for (SugarCRMRecordDTO record : selList) {
				

				
				integrationservice.processSelectedRecord(record,
						new AsyncCallback<ImportResultDTO>() {
					
					  
							@Override
							public void onFailure(Throwable throwable) {
								throwable.printStackTrace();
								int numRows = impResultsTable.getRowCount();
								

								impResultsTable.setWidget(numRows, 0, new Image(
										EuropeanaClientConstants.ERRORIMAGELOC));
								impResultsTable.setWidget(numRows, 1, new HTML(
										"A system exception has occured"));
								impResultsTable.setWidget(numRows, 2, new HTML(
										throwable.getCause().getStackTrace().toString()));
								
								progressBar.setProgress(impResultsTable.getRowCount());
							}

							@Override
							public void onSuccess(ImportResultDTO searchresults) {

								int numRows = impResultsTable.getRowCount();

								progressBar.setProgress(numRows);

								impResultsTable.setWidget(numRows, 0, new Image(
										searchresults.getResult()));
								impResultsTable.setWidget(numRows, 1, new HTML(
										searchresults.getCollectionName()));
								impResultsTable.setWidget(numRows, 2, new HTML(
										searchresults.getDescription()));
								impResultsTable.setWidget(numRows, 3, new HTML(
										searchresults.getCause()));
								
								progressBar.setProgress(impResultsTable.getRowCount());

							}
						});
			}

		}

	}

	
	
	/**
	 * Performs an asynchronous search in SugarCRM.
	 */
	private void performSearch() {
		String query = generateQuery();
		integrationservice.executeSugarCRMQuery(query,
				new AsyncCallback<List<SugarCRMRecordDTO>>() {
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

	
	
	/**
	 * Generates a query from existing GUI fields.
	 * 
	 * @return the query String
	 */
	private String generateQuery() {

		StringBuffer querybuffer = new StringBuffer();
		String dsname = DOM.getElementById("dsnameSearchField").<InputElement>cast().getValue();
		String type = DOM.getElementById("typeSearchField").<InputElement>cast().getValue();
		String status = DOM.getElementById("statusSearchField").<InputElement>cast().getValue();

		ArrayList<StringBuffer> fieldinventory = new ArrayList<StringBuffer>();
		
		
        if(!"".equals(dsname)){
        	StringBuffer queryitem = new StringBuffer();
        	queryitem.append("opportunities.name LIKE '");
        	queryitem.append(dsname);
        	queryitem.append("'");
        	fieldinventory.add(queryitem);
		}
		

		if(!"".equals(type)){
        	StringBuffer queryitem = new StringBuffer();
        	queryitem.append("opportunities.opportunity_type LIKE '");
        	queryitem.append(type);
        	queryitem.append("'");
        	fieldinventory.add(queryitem);
		}
		
		if(!"".equals(status)){
        	StringBuffer queryitem = new StringBuffer();
        	queryitem.append("opportunities.sales_stage LIKE '");
        	queryitem.append(status);
        	queryitem.append("'");
        	fieldinventory.add(queryitem);
		}
		
				
		
        for(int i=0;i<fieldinventory.size();i++){
        	if(i==0){
        		querybuffer.append(fieldinventory.get(i));
        		
        	}
        	else{
        		querybuffer.append(" AND ");
        		querybuffer.append(fieldinventory.get(i));
        	}
        }
        
		return querybuffer.toString();

	}

	
	
	
	
	/**
	 * Add the columns to the table.
	 */
	private void initTableColumns(
			final SelectionModel<SugarCRMRecordDTO> selectionModel,
			ListHandler<SugarCRMRecordDTO> sortHandler) {

		// Checkbox column. This table will uses a checkbox column for
		// selection.
		// Alternatively, you can call cellTable.setSelectionEnabled(true) to
		// enable
		// mouse selection.
		Column<SugarCRMRecordDTO, Boolean> checkColumn = new Column<SugarCRMRecordDTO, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(SugarCRMRecordDTO object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		cellTable.addColumn(checkColumn,
				SafeHtmlUtils.fromSafeConstant("<br/>"));
		cellTable.setColumnWidth(checkColumn, 40, Unit.PX);

		
		// IsImported column
		Column<SugarCRMRecordDTO, String> isImportedColumn = new Column<SugarCRMRecordDTO, String>(
				new ImageCell()) {
			@Override
			public String getValue(SugarCRMRecordDTO object) {
				return object.getImportedIMG();
			}
		};
		isImportedColumn.setSortable(true);

		sortHandler.setComparator(isImportedColumn,
				new Comparator<SugarCRMRecordDTO>() {
					public int compare(SugarCRMRecordDTO o1,
							SugarCRMRecordDTO o2) {
						return o1.getImportedIMG().compareTo(
								o2.getImportedIMG());
					}
				});
		cellTable.addColumn(isImportedColumn, EuropeanaClientConstants.UIMSTATELABEL);
		isImportedColumn
				.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
					public void update(int index, SugarCRMRecordDTO object,
							String value) {

						dataProvider.refresh();
					}
				});
		cellTable.setColumnWidth(isImportedColumn, 7, Unit.PCT);
		
		
		
		
		// Collection Name Column
		Column<SugarCRMRecordDTO, Anchor> collectionColumn = new Column<SugarCRMRecordDTO, Anchor>(
				new AnchorCell()) {
			@Override
			public Anchor getValue(SugarCRMRecordDTO object) {

				Anchor hyper = new Anchor();
				hyper.setName(object.getName());
				hyper.setText(object.getName());
				hyper.setHref("http://sip-manager.isti.cnr.it/sugarcrm/index.php?module=Opportunities&action=DetailView&record="+object.getId());
				hyper.setTarget("TOP");
				
				return hyper;
			}
		};
		collectionColumn.setSortable(true);

		sortHandler.setComparator(collectionColumn,
				new Comparator<SugarCRMRecordDTO>() {
					public int compare(SugarCRMRecordDTO o1,
							SugarCRMRecordDTO o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
		cellTable.addColumn(collectionColumn, EuropeanaClientConstants.DSNAMESEARCHLABEL);
		collectionColumn
				.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, Anchor>() {
					public void update(int index, SugarCRMRecordDTO object,
							Anchor value) {

						dataProvider.refresh();
					}
				});
		cellTable.setColumnWidth(collectionColumn, 40, Unit.PCT);

		
		
		// Organization Name Column
		Column<SugarCRMRecordDTO, String> organizationColumn = new Column<SugarCRMRecordDTO, String>(
				new TextCell()) {
			@Override
			public String getValue(SugarCRMRecordDTO object) {
				return object.getOrganization_name();
			}
		};
		
		collectionColumn.setSortable(true);

		sortHandler.setComparator(organizationColumn,
				new Comparator<SugarCRMRecordDTO>() {
					public int compare(SugarCRMRecordDTO o1,
							SugarCRMRecordDTO o2) {
						return o1.getOrganization_name().compareTo(
								o2.getOrganization_name());
					}
				});
		
		cellTable.addColumn(organizationColumn,EuropeanaClientConstants.ORGANIZATIONSEARCHLABEL);
		organizationColumn
				.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
					public void update(int index, SugarCRMRecordDTO object,
							String value) {

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

		sortHandler.setComparator(countryColumn,
				new Comparator<SugarCRMRecordDTO>() {
					public int compare(SugarCRMRecordDTO o1,
							SugarCRMRecordDTO o2) {
						return o1.getCountry_c().compareTo(o2.getCountry_c());
					}
				});
		cellTable.addColumn(countryColumn,EuropeanaClientConstants.COUNTRYSEARCHLABEL);
		countryColumn
				.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
					public void update(int index, SugarCRMRecordDTO object,
							String value) {

						dataProvider.refresh();
					}
				});
		cellTable.setColumnWidth(countryColumn, 5, Unit.PCT);

		// Status Column
		Column<SugarCRMRecordDTO, String> statusColumn = new Column<SugarCRMRecordDTO, String>(
				new TextCell()) {
			@Override
			public String getValue(SugarCRMRecordDTO object) {
				return object.getStatus();
			}
		};
		statusColumn.setSortable(true);

		sortHandler.setComparator(statusColumn,
				new Comparator<SugarCRMRecordDTO>() {
					public int compare(SugarCRMRecordDTO o1,
							SugarCRMRecordDTO o2) {
						return o1.getStatus().compareTo(o2.getStatus());
					}
				});
		cellTable.addColumn(statusColumn,EuropeanaClientConstants.STATUSSEARCHLABEL);
		statusColumn
				.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
					public void update(int index, SugarCRMRecordDTO object,
							String value) {

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

		sortHandler.setComparator(amountColumn,
				new Comparator<SugarCRMRecordDTO>() {
					public int compare(SugarCRMRecordDTO o1,
							SugarCRMRecordDTO o2) {
						return o1.getIngested_total_c().compareTo(
								o2.getIngested_total_c());
					}
				});
		cellTable.addColumn(amountColumn,EuropeanaClientConstants.AMOUNTSEARCHLABEL);
		amountColumn
				.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
					public void update(int index, SugarCRMRecordDTO object,
							String value) {

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

		sortHandler.setComparator(ingestionDateColumn,
				new Comparator<SugarCRMRecordDTO>() {
					public int compare(SugarCRMRecordDTO o1,
							SugarCRMRecordDTO o2) {
						return o1.getExpected_ingestion_date().compareTo(
								o2.getExpected_ingestion_date());
					}
				});
		cellTable.addColumn(ingestionDateColumn,EuropeanaClientConstants.INGESTDATESEARCHLABEL);
		ingestionDateColumn
				.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
					public void update(int index, SugarCRMRecordDTO object,
							String value) {

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

		sortHandler.setComparator(userColumn,
				new Comparator<SugarCRMRecordDTO>() {
					public int compare(SugarCRMRecordDTO o1,
							SugarCRMRecordDTO o2) {

						return o1.getAssigned_user_name().compareTo(
								o2.getAssigned_user_name());
					}
				});

		cellTable.addColumn(userColumn,EuropeanaClientConstants.USERSEARCHLABEL);
		userColumn
				.setFieldUpdater(new FieldUpdater<SugarCRMRecordDTO, String>() {
					public void update(int index, SugarCRMRecordDTO object,
							String value) {

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
	private DialogBox createSearchDialogBox() {
		// Create a dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		dialogBox.ensureDebugId("cwDialogBox");
		dialogBox.setText(EuropeanaClientConstants.SEARCHDIALOGMSG);
		dialogBox.setModal(true);

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setSpacing(0);
		dialogBox.setWidget(dialogContents);
		Image activity = new Image(EuropeanaClientConstants.QUERYIMAGELOC);
		
		// Add some text to the top of the dialog

		dialogContents.add(activity);
		dialogContents.setCellHorizontalAlignment(activity,
				HasHorizontalAlignment.ALIGN_CENTER);

		// Return the dialog box
		return dialogBox;
	}

	
	
	
	
	/**
	 * Creates an import dialog
	 * @return
	 */
	private DialogBox createImportDialog() {
		// Create a dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		progressBar = new ProgressBar(0, 100);
		progressBar.setWidth("100%");

		dialogBox.ensureDebugId("impDialogBox");
		dialogBox.setText(EuropeanaClientConstants.IMPORTMENULABEL);
		dialogBox.setModal(true);

		Button closeButton = new Button();
		closeButton.setText("Close");

		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				progressBar.setProgress(0);
				impResultsTable.removeAllRows();
			}
		});

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		
		
		
		dialogContents.setSpacing(4);
		dialogBox.setWidget(dialogContents);
		dialogContents.add(progressBar);
		
		ScrollPanel scrollPanel = new ScrollPanel();
		
		scrollPanel.setWidth("600px");
		scrollPanel.setHeight("500px");
		
		scrollPanel.add(impResultsTable);
		
		dialogContents.add(scrollPanel);
		
		dialogContents.add(closeButton);
		HTML details = new HTML("ImportStatus");

		dialogContents.setCellHorizontalAlignment(details,
				HasHorizontalAlignment.ALIGN_CENTER);
		return dialogBox;
	}
	
	
	
	  /**
	   * Create a form that contains undisclosed advanced options.
	   */
	  private Widget createAdvancedForm() {
	    // Create a table to layout the form options
	    FlexTable layout = new FlexTable();
	    layout.setCellSpacing(6);
	    layout.setWidth("500px");
	    FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

	    // Add a title to the form
	    layout.setHTML(0, 2, "<b>Search Criteria</b>");
	    cellFormatter.setColSpan(0, 0, 2);
	    cellFormatter.setHorizontalAlignment(
	        0, 0, HasHorizontalAlignment.ALIGN_CENTER);

	    // Add some standard form options

	    TextBox dsnameSearchField = new TextBox();	
	    setDOMID(dsnameSearchField,"dsnameSearchField");


	    final ListBox statusSearchField = new ListBox(false);
	    statusSearchField.addItem("--", "");	    
	    statusSearchField.addItem(RecordStates.OAI_PMH_TESTING.getDescription(),
	    		RecordStates.OAI_PMH_TESTING.getSysId());
	    statusSearchField.addItem(RecordStates.OAI_PMH_SENT_TO_ORG.getDescription(),
	    		RecordStates.OAI_PMH_SENT_TO_ORG.getSysId());
	    statusSearchField.addItem(RecordStates.READY_FOR_HARVESTING.getDescription(), 
	    		RecordStates.READY_FOR_HARVESTING.getSysId());
	    statusSearchField.addItem(RecordStates.MAPPING_AND_NORMALIZATION.getDescription(), 
	    		RecordStates.MAPPING_AND_NORMALIZATION.getSysId());
	    statusSearchField.addItem(RecordStates.READY_FOR_REPLICATION.getDescription(), 
	    		RecordStates.READY_FOR_REPLICATION.getSysId());
	    statusSearchField.addItem(RecordStates.ONGOING_SCHEDULED_UPDATES.getDescription(), 
	    		RecordStates.ONGOING_SCHEDULED_UPDATES.getSysId());
	    statusSearchField.addItem(RecordStates.INGESTION_COMPLETE.getDescription(), 
	    		RecordStates.INGESTION_COMPLETE.getSysId());
	    statusSearchField.addItem(RecordStates.DISABLED_AND_REPLACED.getDescription(), 
	    		RecordStates.DISABLED_AND_REPLACED.getSysId());
	    statusSearchField.addItem(RecordStates.HARVESTING_PENDING.getDescription(), 
	    		RecordStates.HARVESTING_PENDING.getSysId());
	    setDOMID(statusSearchField,"statusSearchField");

	    
	    final ListBox typeSearchField = new ListBox(false);
	    typeSearchField.addItem("--", "");	    
	    typeSearchField.addItem("Update", "Update");
	    typeSearchField.addItem("New Dataset", "New%Dataset");
	    setDOMID(typeSearchField,"typeSearchField");
	    
	   
	    
		searchButton = new Button();
		searchButton.setText(EuropeanaClientConstants.SEARCHBUTTONLABEL);
		searchButton.setTitle(EuropeanaClientConstants.SEARCHBUTTONTITLE);

		searchButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDialog.center();
				performSearch();
			}
		});
	    
	    
	    layout.setHTML(1, 0, EuropeanaClientConstants.DSNAMESEARCHLABEL);
	    layout.setWidget(1, 1, dsnameSearchField);
	    
	    layout.setHTML(1, 2, EuropeanaClientConstants.TYPESEARCHLABEL);
	    layout.setWidget(1, 3, typeSearchField);
	    
	    layout.setHTML(1, 4, EuropeanaClientConstants.STATUSSEARCHLABEL);
	    layout.setWidget(1, 5, statusSearchField);
	    

	 
	    layout.setWidget(3, 0, searchButton);
	    

	    // Wrap the contents in a DecoratorPanel
	    DecoratorPanel decPanel = new DecoratorPanel();
	    decPanel.setWidget(layout);
	    return decPanel;
	  }
	  
	  
	  private void setDOMID(Widget widg,String id){
		    DOM.setElementProperty(widg.getElement(),"id", id);
	  }

		@Override
		public void fireEvent(GwtEvent<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	  
	
}

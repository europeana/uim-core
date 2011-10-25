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

import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import eu.europeana.uim.gui.cp.client.management.ResourceManagementWidget;
import eu.europeana.uim.gui.cp.client.services.IntegrationSeviceProxyAsync;
import eu.europeana.uim.gui.cp.client.services.RepositoryServiceAsync;
import eu.europeana.uim.gui.cp.client.services.ResourceServiceAsync;
import eu.europeana.uim.gui.cp.client.utils.RepoxOperationType;
import eu.europeana.uim.gui.cp.shared.IntegrationStatusDTO;
import eu.europeana.uim.gui.cp.shared.IntegrationStatusDTO.TYPE;
import eu.europeana.uim.gui.cp.shared.ParameterDTO;
import eu.europeana.uim.gui.cp.shared.RepoxExecutionStatusDTO;


/**
 * An extended version of the ResourceMangementWidgets
 * 
 * @author Georgios Markakis
 */
public class ExpandedResourceManagementWidget extends ResourceManagementWidget{

	private final IntegrationSeviceProxyAsync integrationservice;

	@UiField(provided = true)
	public FlexTable integrationTable;
	
	@UiField
	public TabLayoutPanel tabInfoSubPanel;
	
	public DialogBox operationDialog;
	
	public ListBox operationsListBox;
	
	

	/**
     * The UiBinder interface used by this example.
     */
    interface Binder extends UiBinder<Widget, ExpandedResourceManagementWidget> {
    }
	
    
	/**
	 * Constructor
	 * 
	 * @param repositoryService
	 * @param resourceService
	 * @param integrationservice
	 */
	public ExpandedResourceManagementWidget(
			RepositoryServiceAsync repositoryService,
			ResourceServiceAsync resourceService,
			IntegrationSeviceProxyAsync integrationservice) {
		super(repositoryService, resourceService);
		this.integrationservice = integrationservice;
	
	}
	

    
	/* (non-Javadoc)
	 * @see eu.europeana.uim.gui.cp.client.management.ResourceManagementWidget#postInitialize()
	 */
	@Override
    public Widget postInitialize(){
		
		tabInfoSubPanel = new TabLayoutPanel(2.5, Unit.PCT);
		tabInfoSubPanel.setAnimationDuration(1000);
		tabInfoSubPanel.getElement().getStyle().setMarginBottom(10.0, Unit.PX);
		tabInfoSubPanel.setVisible(false);
		
		integrationTable = new FlexTable();
		operationDialog = createOperationsDialogBox();
		operationsListBox = new ListBox(false);
		
		operationsListBox.addItem(RepoxOperationType.INITIATE_COMPLETE_HARVESTING.getDescription());   
		operationsListBox.addItem(RepoxOperationType.INITIATE_INCREMENTAL_HARVESTING.getDescription()); 
  		operationsListBox.addItem(RepoxOperationType.VIEW_HARVEST_LOG.getDescription());    
  	    //operationsListBox.addItem(RepoxOperationType.SCHEDULE_HARVESTING.getDescription());   
		
        Binder uiBinder = GWT.create(Binder.class);
        Widget widget = uiBinder.createAndBindUi(this);
        
        return widget;
    }

	
    /**
     * Retrieve parameters for given settings.
     */
	@Override
    public void updateParameters() {
        getResourceService().getParameters(getProvider() != null ? getProvider().getId() : null,
                getCollection() != null ? getCollection().getId() : null,
                getWorkflow() != null ? getWorkflow().getIdentifier() : null,
                new AsyncCallback<List<ParameterDTO>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onSuccess(List<ParameterDTO> parameters) {
                    	getActiveParameters().clear();
                    	getActiveParameters().addAll(parameters);
                        cellTable.setRowData(0, getActiveParameters());
                        cellTable.setRowCount(getActiveParameters().size());
                        cellTable.setHeight((30 + 20 * parameters.size()) + "px");
                    }
                });
        
        
        integrationservice.retrieveIntegrationInfo(getProvider() != null ? getProvider().getMnemonic() : null,
        getCollection() != null ? getCollection().getMnemonic() : null,
            new AsyncCallback<IntegrationStatusDTO>() {
        	
            @Override
            public void onFailure(Throwable throwable) {
            	
            	integrationTable.setWidget(0, 0, new HTML("An unknown exception has occured:"));
            	integrationTable.setWidget(0, 1, new HTML(throwable.getMessage()));
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(IntegrationStatusDTO status) {

            	generateIntergationInfoPanel(status);

            }
        }		
        
        );
        
    }
	
	
	
	//Private Methods
	
	
	/**
	 * 
	 * 
	 * @param status
	 */
	private void generateIntergationInfoPanel(IntegrationStatusDTO status){

		integrationTable.clear();
		
		if(!status.getType().equals(TYPE.UNIDENTIFIED)){
		
		
		tabInfoSubPanel.setVisible(true);	
		tabInfoSubPanel.getTabWidget(1).setVisible(false);	
		
    	integrationTable.setWidget(0, 0, new HTML("Type:"));
    	integrationTable.setWidget(0, 1, new HTML(status.getType().toString()));
    	
    	integrationTable.setWidget(1, 0, new HTML("Name:"));          	
    	integrationTable.setWidget(1, 1, new HTML(status.getInfo()));
    	
    	integrationTable.setWidget(2, 0, new HTML("Identifier:"));
    	integrationTable.setWidget(2, 1, new HTML(status.getId()));
    	
    	integrationTable.setWidget(3, 0, new HTML("SugarCRM Link:"));
    	
    	if(status.getSugarCRMID() == null){
    		integrationTable.setWidget(3, 1, new HTML("Not represented in SugarCRM")); 
    	}
    	else{
    		
			Anchor hyper = new Anchor();
			hyper.setName("SugarCRMLink");
			hyper.setText("Click here to edit information in SugarCRM.");
			hyper.setHref(status.getSugarURL());
			hyper.setTarget("TOP");
			integrationTable.setWidget(3, 1, hyper);
   		
    	}

    	
    	integrationTable.setWidget(4, 0, new HTML("Repox Link:"));
    	
    	if(status.getRepoxID() == null){
    		integrationTable.setWidget(4, 1, new HTML("Not represented in Repox")); 
    	}
    	else{
    		
			Anchor hyper = new Anchor();
			hyper.setName("RepoxLink");
			hyper.setText("Click here to edit REPOX configuration.");
			hyper.setHref(status.getRepoxURL());
			hyper.setTarget("TOP");
			
			integrationTable.setWidget(4, 1,hyper);  

    	}
    	
    	  if(status.getType().equals(TYPE.COLLECTION)  ){
    		tabInfoSubPanel.getTabWidget(1).setVisible(true);
          	integrationTable.setWidget(5, 0, new HTML("Harvesting Status:"));
          	integrationTable.setWidget(5, 1, new HTML(status.getHarvestingStatus().getStatus().getDescription()));         	
  		    integrationTable.setWidget(6, 0, new HTML("<hr></hr>"));           		    	
          	integrationTable.setWidget(7, 0, new HTML("Permitted operations:"));
          	integrationTable.setWidget(7, 1, operationsListBox);
          	integrationTable.setWidget(7, 2, generateRepoxCommandButton());
          	
    	  }
	
    	}
		else{
			tabInfoSubPanel.setVisible(false);	
		}
	}
	
	
	
	
	
	/**
	 * 
	 * 
	 * @return
	 */
	private Button generateRepoxCommandButton(){
		Button actionButton = new Button("Execute");
		actionButton.addClickHandler(new ClickHandler() {
		 
		
			
			@Override
			public void onClick(ClickEvent event) {

				operationDialog.center();
				
				int index = operationsListBox.getSelectedIndex();
				
				String value = operationsListBox.getValue(index);
				
				RepoxOperationType optype = null;
				
				if (value.equals(RepoxOperationType.INITIATE_COMPLETE_HARVESTING.getDescription())){
					optype = RepoxOperationType.INITIATE_COMPLETE_HARVESTING;
				}
				else if(value.equals(RepoxOperationType.SCHEDULE_HARVESTING.getDescription())){
					optype = RepoxOperationType.SCHEDULE_HARVESTING;
				}
				else if(value.equals(RepoxOperationType.VIEW_HARVEST_LOG.getDescription())){
					optype = RepoxOperationType.VIEW_HARVEST_LOG;
				}
				
				
				if(optype != null){
				integrationservice.performRepoxRemoteOperation(optype, getCollection().getMnemonic(), new AsyncCallback<RepoxExecutionStatusDTO>(){

					@Override
					public void onFailure(Throwable caught) {
						operationDialog.clear();
						
						VerticalPanel vp = new VerticalPanel();
						vp.add(new HTML("<verbatim>"+caught.getMessage()+"</verbatim>"));
						vp.add(createOperationsCloseButton());
						operationDialog.setText("An Unclassified Exception Occured (send this to you know whom)");
						operationDialog.setWidget(vp);

					}

					@Override
					public void onSuccess(RepoxExecutionStatusDTO result) {

						operationDialog.clear();
						
						VerticalPanel vp = new VerticalPanel();
						vp.add(new HTML("<verbatim>"+result.getLogMessage()+"</verbatim>"));
						vp.add(createOperationsCloseButton());
						operationDialog.setWidget(vp);
						operationDialog.setText(result.getOperationMessage());

					}	
				});
				}
			}
		});
		
		return actionButton;
	}
	
	
	/**
	 * Create the dialog box for this example.
	 * 
	 * @return the new dialog box
	 */
	private DialogBox createOperationsDialogBox() {
		final DialogBox dialogBox = new DialogBox();

		dialogBox.setModal(true);
		
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setSpacing(0);
		dialogBox.setWidget(dialogContents);


		return dialogBox;
	}
	
	
	
	private Button createOperationsCloseButton(){
		
		Button closebutton = new Button("Close");
		

		closebutton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				operationDialog.hide();
				
			}
			
		});
		
		return closebutton;
	}
	
	
	
	
	
	
	
    @Override
    protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(ExpandedResourceManagementWidget.class, new RunAsyncCallback() {
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
	 * @return the integrationTable
	 */
	public FlexTable getIntegrationTable() {
		return integrationTable;
	}



	/**
	 * @param integrationTable the integrationTable to set
	 */
	public void setIntegrationTable(FlexTable integrationTable) {
		this.integrationTable = integrationTable;
	}
    
}

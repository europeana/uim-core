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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import eu.europeana.uim.gui.cp.client.management.ResourceManagementWidget;
import eu.europeana.uim.gui.cp.client.services.IntegrationSeviceProxyAsync;
import eu.europeana.uim.gui.cp.client.services.RepositoryServiceAsync;
import eu.europeana.uim.gui.cp.client.services.ResourceServiceAsync;
import eu.europeana.uim.gui.cp.shared.IntegrationStatusDTO;
import eu.europeana.uim.gui.cp.shared.IntegrationStatusDTO.TYPE;
import eu.europeana.uim.gui.cp.shared.ParameterDTO;


/**
 * An extended version of the ResourceMangementWidgets
 * 
 * @author Georgios Markakis
 */
public class ExpandedResourceManagementWidget extends ResourceManagementWidget{

	private final IntegrationSeviceProxyAsync integrationservice;

	@UiField(provided = true)
	FlexTable integrationTable;
	

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
	

    
	@Override
    public Widget postInitialize(){
		integrationTable = new FlexTable();
		
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
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(IntegrationStatusDTO status) {
            	integrationTable.clear();


				if(!status.getType().equals(TYPE.UNIDENTIFIED)){
            		

            	
            	integrationTable.setWidget(0, 0, new HTML("Type:"));
            	integrationTable.setWidget(0, 1, new HTML(status.getType().toString()));
            	
            	integrationTable.setWidget(1, 0, new HTML("Name:"));          	
            	integrationTable.setWidget(1, 1, new HTML(status.getInfo()));
            	
            	integrationTable.setWidget(2, 0, new HTML("Identifier:"));
            	integrationTable.setWidget(2, 1, new HTML(status.getId()));
            	
            	integrationTable.setWidget(3, 0, new HTML("SugarCRM ID:"));
            	
            	if(status.getSugarCRMID() == null){
            		integrationTable.setWidget(3, 1, new HTML("Not represented in SugarCRM")); 
            	}
            	else{
                	integrationTable.setWidget(3, 1, new HTML(status.getSugarCRMID()));           		
            	}

            	
            	integrationTable.setWidget(4, 0, new HTML("Repox ID:"));
            	
            	if(status.getRepoxID() == null){
            		integrationTable.setWidget(4, 1, new HTML("Not represented in Repox")); 
            	}
            	else{
            		integrationTable.setWidget(4, 1, new HTML(status.getRepoxID()));  		
            	}
            	
            	  if(status.getType().equals(TYPE.COLLECTION)){
                  	integrationTable.setWidget(5, 0, new HTML("Harvesting Status:"));
                  	integrationTable.setWidget(5, 1, new HTML(status.getHarvestingStatus().getStatus().toString()));
                  	
          		    integrationTable.setWidget(6, 0, new HTML("<hr></hr>"));
                  	
          		    ListBox listBox = new ListBox(false);
					listBox.addItem("Initiate Harvesting (Complete)");
					listBox.addItem("Initiate Harvesting (Incremental)");                 	
					listBox.addItem("View Harvest Log:");    
					listBox.addItem("Download Harvested Metadata");   
          		    
					Button actionButton = new Button("Perform Operation");
					actionButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							
							AsyncCallback<IntegrationStatusDTO> async = null;
							integrationservice.retrieveIntegrationInfo("asda", "collection", async);

						}
					});
					
					
					
                  	integrationTable.setWidget(7, 0, new HTML("Permitted operations:"));
                  	integrationTable.setWidget(7, 1, listBox);
                  	integrationTable.setWidget(7, 2, actionButton);
                  	

            	  }
            	
            	
            	
            	}
            	
            	
            	
            	
            	


            	
            	
            }
        }		
        
        );
        
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

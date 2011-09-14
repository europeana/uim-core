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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import eu.europeana.uim.gui.cp.client.management.ResourceManagementWidget;
import eu.europeana.uim.gui.cp.client.services.IntegrationSeviceProxyAsync;
import eu.europeana.uim.gui.cp.client.services.RepositoryServiceAsync;
import eu.europeana.uim.gui.cp.client.services.ResourceServiceAsync;
import eu.europeana.uim.gui.cp.shared.IntegrationStatusDTO;
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

            	integrationTable.setWidget(0, 1, new HTML(status.getInfo()));
            	integrationTable.setWidget(0, 2, new HTML(status.getId()));
            	integrationTable.setWidget(0, 3, new HTML(status.getRepoxID()));
            	integrationTable.setWidget(0, 4, new HTML(status.getSugarCRMID()));
            	
            	
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

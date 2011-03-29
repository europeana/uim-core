package eu.europeana.uim.gui.gwt.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

import eu.europeana.uim.gui.gwt.shared.CollectionDTO;

/**
 * The panel displaying all collections
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class CollectionsPanel extends ScrollPanel {

    private OrchestrationServiceAsync orchestrationService = null;

    private Application application;

    private final CellTable<CollectionDTO> collectionsCellTable = new CellTable<CollectionDTO>();

    private List<CollectionDTO> collections = new ArrayList<CollectionDTO>();
    private Timer collectionsRefreshTimer = new Timer() {
        @Override
        public void run() {
            updateCollections();
        }
    };


    public CollectionsPanel(OrchestrationServiceAsync orchestrationService, Application application) {
        this.orchestrationService = orchestrationService;
        this.application = application;

        add(collectionsCellTable);

        updateCollections();

        // auto-refresh this panel if it is active
        application.addTabSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> integerSelectionEvent) {
                if (integerSelectionEvent.getSelectedItem().equals(2)) {
                    updateCollections();
                    collectionsRefreshTimer.scheduleRepeating(10000);
                } else {
                    collectionsRefreshTimer.cancel();
                }
            }
        });

        buildCollectionsCellTable();


    }

    private void buildCollectionsCellTable() {
        final ListDataProvider<CollectionDTO> dataProvider = new ListDataProvider<CollectionDTO>();
        dataProvider.setList(collections);
        dataProvider.addDataDisplay(collectionsCellTable);


        final SingleSelectionModel<CollectionDTO> selectionModel = new SingleSelectionModel<CollectionDTO>();
        collectionsCellTable.setSelectionModel(selectionModel);

        CellTableUtils.addColumn(collectionsCellTable, new TextCell(), "Collection", new CellTableUtils.GetValue<String, CollectionDTO>() {
            public String getValue(CollectionDTO collection) {
                return collection.getName();
            }
        });
        CellTableUtils.addColumn(collectionsCellTable, new TextCell(), "Provider", new CellTableUtils.GetValue<String, CollectionDTO>() {
            public String getValue(CollectionDTO collection) {
                return collection.getProvider().getName();
            }
        });
        CellTableUtils.addColumn(collectionsCellTable, new NumberCell(), "Total records", new CellTableUtils.GetValue<Number, CollectionDTO>() {
            public Integer getValue(CollectionDTO collection) {
                return collection.getTotal();
            }
        });

    }

    private void updateCollections(List<CollectionDTO> collections) {
        this.collections.clear();
        this.collections.addAll(collections);
        CellTableUtils.updateCellTableData(collectionsCellTable, collections);
    }

    private void updateCollections() {
        orchestrationService.getAllCollections(new AsyncCallback<List<CollectionDTO>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(List<CollectionDTO> collections) {
                updateCollections(collections);
            }
        });
    }
}

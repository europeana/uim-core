package org.theeuropeanlibrary.uim.gui.gwt.client.content.validation;

import java.util.ArrayList;
import java.util.List;

import org.theeuropeanlibrary.uim.gui.gwt.client.IngestionControlPanelWidget;
import org.theeuropeanlibrary.uim.gui.gwt.client.OrchestrationServiceAsync;
import org.theeuropeanlibrary.uim.gui.gwt.shared.CollectionDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.MetaDataRecordDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ProviderDTO;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

/**
 * Table view showing current exectuions.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class RepositoryValidationWidget extends IngestionControlPanelWidget {
    /**
     * The UiBinder interface used by this example.
     */
    interface Binder extends UiBinder<Widget, RepositoryValidationWidget> {
    }

    private final OrchestrationServiceAsync orchestrationServiceAsync;
    private final List<MetaDataRecordDTO>   records     = new ArrayList<MetaDataRecordDTO>();
    private final List<ProviderDTO>         providers   = new ArrayList<ProviderDTO>();
    private final List<CollectionDTO>       collections = new ArrayList<CollectionDTO>();

    /**
     * Box with providers for selection
     */
    @UiField
    ListBox                                 providerBox;
    /**
     * Box with collections for selection
     */
    @UiField
    ListBox                                 collectionBox;
    /**
     * Button to trigger search in repository
     */
    @UiField
    Button                                  searchButton;

    /**
     * The main CellTable.
     */
    @UiField(provided = true)
    CellTable<MetaDataRecordDTO>            cellTable;

    /**
     * The pager used to change the range of data.
     */
    @UiField(provided = true)
    SimplePager                             pager;

    /**
     * Creates a new instance of this class.
     * 
     * @param orchestrationServiceAsync
     */
    public RepositoryValidationWidget(OrchestrationServiceAsync orchestrationServiceAsync) {
        super(
                "Central Repository",
                "This view allows the validation of the repository by browsing through the content filtered by provider and collection!");
        this.orchestrationServiceAsync = orchestrationServiceAsync;
    }

    @Override
    public Widget onInitialize() {
        cellTable = new CellTable<MetaDataRecordDTO>(MetaDataRecordDTO.KEY_PROVIDER);
        cellTable.setWidth("100%", true);
        cellTable.setPageSize(10);
        cellTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent arg0) {
                CollectionDTO collection = collections.get(collectionBox.getSelectedIndex());
                if (collection != null) {
                    Range range = cellTable.getVisibleRange();
                    int start = range.getStart();
                    int length = range.getLength();
                    updateRows(collection, start, length);
                }
            }
        });

        final ListDataProvider<MetaDataRecordDTO> dataProvider = new ListDataProvider<MetaDataRecordDTO>();
        dataProvider.setList(records);
        dataProvider.addDataDisplay(cellTable);

        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(cellTable);

        initTableColumns();

        Binder uiBinder = GWT.create(Binder.class);
        Widget widget = uiBinder.createAndBindUi(this);

        orchestrationServiceAsync.getProviders(new AsyncCallback<List<ProviderDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<ProviderDTO> result) {
                providers.clear();
                providerBox.clear();
                providers.addAll(result);
                for (ProviderDTO provider : providers) {
                    providerBox.addItem(provider.getName());
                }
            }
        });

        providerBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                ProviderDTO provider = providers.get(providerBox.getSelectedIndex());

                orchestrationServiceAsync.getCollections(provider.getId(),
                        new AsyncCallback<List<CollectionDTO>>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                caught.printStackTrace();
                            }

                            @Override
                            public void onSuccess(List<CollectionDTO> result) {
                                collections.clear();
                                collectionBox.clear();
                                collections.addAll(result);
                                for (CollectionDTO collection : collections) {
                                    collectionBox.addItem(collection.getName());
                                }
                            }
                        });
            }
        });
        searchButton.setText("Search");
        searchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                CollectionDTO collection = collections.get(collectionBox.getSelectedIndex());
                if (collection != null) {
                    updateRowsCount(collection);
                    updateRows(collection, 0, 10);
                }
            }
        });

        return widget;
    }

// @Override
// protected void onLoad() {
// super.onLoad();
// updateRowsCount();
// }

    private void updateRowsCount(CollectionDTO collection) {
        orchestrationServiceAsync.getCollectionTotal(collection.getId(),
                new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        caught.printStackTrace();
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        records.clear();
                        cellTable.setRowData(0, records);
                        cellTable.setRowCount(result);
                    }
                });
    }

    private void updateRows(final CollectionDTO collection, final int offset, final int maxSize) {
        orchestrationServiceAsync.getRecordsForCollection(collection.getId(), offset, maxSize,
                new AsyncCallback<List<MetaDataRecordDTO>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        caught.printStackTrace();
                    }

                    @Override
                    public void onSuccess(List<MetaDataRecordDTO> result) {
                        records.clear();
                        records.addAll(result);
                        cellTable.setRowData(offset, records);
                    }
                });
    }

    @Override
    protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(RepositoryValidationWidget.class, new RunAsyncCallback() {

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
    private void initTableColumns() {
        // ID
        Column<MetaDataRecordDTO, String> idColumn = new Column<MetaDataRecordDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(MetaDataRecordDTO object) {
                return object.getId().toString();
            }
        };
        cellTable.addColumn(idColumn, "ID");
        cellTable.setColumnWidth(idColumn, 10, Unit.PCT);

        // Title
        Column<MetaDataRecordDTO, String> titleColumn = new Column<MetaDataRecordDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(MetaDataRecordDTO object) {
                return object.getTitle();
            }
        };
        cellTable.addColumn(titleColumn, "Title");
        cellTable.setColumnWidth(titleColumn, 15, Unit.PCT);

        // Contributor
        Column<MetaDataRecordDTO, String> contributorColumn = new Column<MetaDataRecordDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(MetaDataRecordDTO object) {
                return object.getContributor();
            }
        };
        cellTable.addColumn(contributorColumn, "Contributor");
        cellTable.setColumnWidth(contributorColumn, 13, Unit.PCT);

        // Creator
        Column<MetaDataRecordDTO, String> creatorColumn = new Column<MetaDataRecordDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(MetaDataRecordDTO object) {
                return object.getCreator();
            }
        };
        cellTable.addColumn(creatorColumn, "Creator");
        cellTable.setColumnWidth(creatorColumn, 13, Unit.PCT);

        // Year
        Column<MetaDataRecordDTO, String> yearColumn = new Column<MetaDataRecordDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(MetaDataRecordDTO object) {
                return object.getYear();
            }
        };
        cellTable.addColumn(yearColumn, "Year");
        cellTable.setColumnWidth(yearColumn, 9, Unit.PCT);

        // Language
        Column<MetaDataRecordDTO, String> langColumn = new Column<MetaDataRecordDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(MetaDataRecordDTO object) {
                return object.getLanguage();
            }
        };
        cellTable.addColumn(langColumn, "Language");
        cellTable.setColumnWidth(langColumn, 10, Unit.PCT);

        // Language
        Column<MetaDataRecordDTO, String> countryColumn = new Column<MetaDataRecordDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(MetaDataRecordDTO object) {
                return object.getCountry();
            }
        };
        cellTable.addColumn(countryColumn, "Country");
        cellTable.setColumnWidth(countryColumn, 10, Unit.PCT);

        // Show Record Details
        Column<MetaDataRecordDTO, MetaDataRecordDTO> plainColumn = new Column<MetaDataRecordDTO, MetaDataRecordDTO>(
                new ActionCell<MetaDataRecordDTO>("Content",
                        new ActionCell.Delegate<MetaDataRecordDTO>() {
                            @Override
                            public void execute(MetaDataRecordDTO record) {
                                final DialogBox updateBox = new RecordDetailsDialogBox(record.getId(), orchestrationServiceAsync);
                                updateBox.show();
                            }
                        })) {
            @Override
            public MetaDataRecordDTO getValue(MetaDataRecordDTO object) {
                return object;
            }
        };
        cellTable.addColumn(plainColumn, "Show");
        cellTable.setColumnWidth(plainColumn, 10, Unit.PX);
    }
}

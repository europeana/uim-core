package org.theeuropeanlibrary.uim.gui.gwt.client.content.validation;

import java.util.ArrayList;
import java.util.List;

import org.theeuropeanlibrary.uim.gui.gwt.client.IngestionControlPanelWidget;
import org.theeuropeanlibrary.uim.gui.gwt.client.OrchestrationServiceAsync;
import org.theeuropeanlibrary.uim.gui.gwt.shared.CollectionDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ProviderDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.SearchRecordDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.SearchResultDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.SearchResultDTO.FacetValue;

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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

/**
 * Table view showing current exectuions.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class SearchIndexValidationWidget extends IngestionControlPanelWidget {
    /**
     * The UiBinder interface used by this example.
     */
    interface Binder extends UiBinder<Widget, SearchIndexValidationWidget> {
    }

    private final OrchestrationServiceAsync orchestrationServiceAsync;
    private final List<SearchRecordDTO>     records       = new ArrayList<SearchRecordDTO>();
    private final List<ProviderDTO>         providers     = new ArrayList<ProviderDTO>();
    private final List<CollectionDTO>       collections   = new ArrayList<CollectionDTO>();
    private String                          lastQuery     = null;

    private static final List<String>       SEARCH_FACETS = new ArrayList<String>() {
                                                              {
                                                                  add("language");
                                                              }
                                                          };
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
     * Box with languages to provide a specific index field
     */
    @UiField
    ListBox                                 languageBox;
    /**
     * Box with index fields for selection
     */
    @UiField
    ListBox                                 fieldBox;
    /**
     * Box with collections for selection
     */
    @UiField
    TextBox                                 queryBox;
    /**
     * Button to trigger search in repository
     */
    @UiField
    Button                                  searchButton;
    /**
     * Facet of search results.
     */
    @UiField
    Grid                                    languageFacet;

    /**
     * The main CellTable.
     */
    @UiField(provided = true)
    CellTable<SearchRecordDTO>              cellTable;

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
    public SearchIndexValidationWidget(OrchestrationServiceAsync orchestrationServiceAsync) {
        super(
                "Search Index",
                "This view allows the validation of the search index by querying terms in specific fields filtered by provider and collection!");
        this.orchestrationServiceAsync = orchestrationServiceAsync;
    }

    /**
     * Initialize this example.
     */
    @Override
    public Widget onInitialize() {
        cellTable = new CellTable<SearchRecordDTO>(SearchRecordDTO.KEY_PROVIDER);
        cellTable.setWidth("100%", true);
        cellTable.setPageSize(10);

        final ListDataProvider<SearchRecordDTO> dataProvider = new ListDataProvider<SearchRecordDTO>();
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

        fieldBox.addItem("all");
        for (String field : IndexFieldNames.languageIndexFields) {
            fieldBox.addItem(field);
        }
        for (String field : IndexFieldNames.plainIndexFields) {
            fieldBox.addItem(field);
        }

        languageBox.addItem("Undefined (und)");
        for (String field : IndexFieldNames.languages) {
            languageBox.addItem(field);
        }

        searchButton.setText("Search");
        searchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                String indexField = fieldBox.getItemText(fieldBox.getSelectedIndex());
                if (indexField == null || indexField.equals("all")) {
                    lastQuery = queryBox.getText();
                } else {
                    if (IndexFieldNames.languageIndexFields.contains(indexField)) {
                        String language = languageBox.getItemText(languageBox.getSelectedIndex());
                        int start = language.indexOf("(");
                        int end = language.indexOf(")");
                        lastQuery = indexField + "_" + language.substring(start, end) + ":" +
                                    queryBox.getText();
                    } else {
                        lastQuery = indexField + ":" + queryBox.getText();
                    }
                }
                CollectionDTO collection = collections.get(collectionBox.getSelectedIndex());
                if (collection != null) {
                    lastQuery += " AND collection:" + collection;
                }
                updateSearchResult(lastQuery, 0, 10);
            }
        });

        languageFacet.setVisible(false);

        return widget;
    }

    private void updateSearchResult(final String searchQuery, final int offset, final int maxSize) {
        orchestrationServiceAsync.searchIndex(searchQuery, offset, maxSize, SEARCH_FACETS,
                new AsyncCallback<SearchResultDTO>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        caught.printStackTrace();
                    }

                    @Override
                    public void onSuccess(SearchResultDTO result) {
                        records.clear();
                        records.addAll(result.getRecords());
                        cellTable.setRowData(offset, records);
                        if (cellTable.getRowCount() != result.getNumberRecords()) {
                            cellTable.setRowCount(result.getNumberRecords());
                        }

                        List<FacetValue> facets = result.getFacets().get("language");
                        if (facets != null) {
                            languageFacet.clear();
                            languageFacet.setVisible(true);

                            int size = facets.size();
                            languageFacet.resize(1 + size / 2, 2);

                            languageFacet.setWidget(0, 0, new Label("language"));
                            int rows = 1;
                            int cols = 0;
                            for (FacetValue facet : facets) {
                                Anchor label = new Anchor(facet.getValue() + " (" +
                                                          facet.getCount() + ")");
                                label.addClickHandler(new FacetClickHandler("language",
                                        facet.getValue()));
                                label.setStyleName("hyperlink_style_label");
                                languageFacet.setWidget(rows, rows, label);
                                if (cols == 1) {
                                    rows++;
                                    cols = 0;
                                } else {
                                    cols++;
                                }
                            }
                        }
                    }
                });
    }

    private class FacetClickHandler implements ClickHandler {
        private final String facetName;
        private final String facetValue;

        public FacetClickHandler(String facetName, String facetValue) {
            this.facetName = facetName;
            this.facetValue = facetValue;
        }

        @Override
        public void onClick(ClickEvent event) {
            lastQuery += " AND " + facetName + ":" + facetValue;
            updateSearchResult(lastQuery, 0, 10);
        }
    }

    @Override
    protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(SearchIndexValidationWidget.class, new RunAsyncCallback() {

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
        Column<SearchRecordDTO, String> idColumn = new Column<SearchRecordDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(SearchRecordDTO object) {
                return object.getId().toString();
            }
        };
        cellTable.addColumn(idColumn, "ID");
        cellTable.setColumnWidth(idColumn, 10, Unit.PCT);

        // Title
        Column<SearchRecordDTO, String> titleColumn = new Column<SearchRecordDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(SearchRecordDTO object) {
                return object.getTitle();
            }
        };
        cellTable.addColumn(titleColumn, "Title");
        cellTable.setColumnWidth(titleColumn, 30, Unit.PCT);

        // Creator
        Column<SearchRecordDTO, String> creatorColumn = new Column<SearchRecordDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(SearchRecordDTO object) {
                return object.getCreator();
            }
        };
        cellTable.addColumn(creatorColumn, "Author");
        cellTable.setColumnWidth(creatorColumn, 15, Unit.PCT);

        // Year
        Column<SearchRecordDTO, String> yearColumn = new Column<SearchRecordDTO, String>(
                new TextCell()) {
            @Override
            public String getValue(SearchRecordDTO object) {
                return object.getYear();
            }
        };
        cellTable.addColumn(yearColumn, "Creator");
        cellTable.setColumnWidth(yearColumn, 15, Unit.PCT);

        // Show Record Details
        Column<SearchRecordDTO, SearchRecordDTO> plainColumn = new Column<SearchRecordDTO, SearchRecordDTO>(
                new ActionCell<SearchRecordDTO>("Content",
                        new ActionCell.Delegate<SearchRecordDTO>() {
                            @Override
                            public void execute(SearchRecordDTO record) {
                                final DialogBox updateBox = new RecordDetailsDialogBox(
                                        record.getId(), orchestrationServiceAsync);
                                updateBox.show();
                            }
                        })) {
            @Override
            public SearchRecordDTO getValue(SearchRecordDTO object) {
                return object;
            }
        };
        cellTable.addColumn(plainColumn, "Show");
        cellTable.setColumnWidth(plainColumn, 10, Unit.PX);
    }
}

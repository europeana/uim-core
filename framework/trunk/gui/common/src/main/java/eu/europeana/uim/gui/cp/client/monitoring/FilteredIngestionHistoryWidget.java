/**
 * 
 */
package eu.europeana.uim.gui.cp.client.monitoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import eu.europeana.uim.gui.cp.client.services.ExecutionServiceAsync;
import eu.europeana.uim.gui.cp.client.services.RepositoryServiceAsync;
import eu.europeana.uim.gui.cp.shared.CollectionDTO;
import eu.europeana.uim.gui.cp.shared.ExecutionDTO;
import eu.europeana.uim.gui.cp.shared.ProviderDTO;
import eu.europeana.uim.gui.cp.shared.WorkflowDTO;

/**
 * Expanded version of the IngestionHistoryWidget which allows filtering on the
 * already submitted executions.
 * 
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @since 6 Mar 2013
 */
public class FilteredIngestionHistoryWidget extends IngestionHistoryWidget {

	/**
	 * Box with providers for selection
	 */
	@UiField(provided = true)
	ListBox providerBox;

	/**
	 * Box with collections for selection
	 */
	@UiField(provided = true)
	ListBox collectionBox;

	/**
	 * Box with workflows for selection
	 */
	@UiField(provided = true)
	ListBox workflowBox;

	/**
	 * Box with workflows for selection
	 */
	@UiField(provided = true)
	TextBox startDateText;

	/**
	 * Start Date date select component field
	 */
	@UiField(provided = true)
	DatePicker startDate;

	/**
	 * Box with workflows for selection
	 */
	@UiField(provided = true)
	TextBox endDateText;

	/**
	 * End Date date select component field
	 */
	@UiField(provided = true)
	DatePicker endDate;

	/**
	 * Clear StartDateButton Button
	 */
	@UiField(provided = true)
	Button clearStartDateBTN;

	/**
	 * Clear EndDateButton Button
	 */
	@UiField(provided = true)
	Button clearEndDateBTN;

	/**
	 * Update Button
	 */
	@UiField(provided = true)
	Button searchButton;

	/**
	 * Update Button
	 */
	@UiField(provided = true)
	Button clearButton;

	/**
	 * The cached value of the start date.
	 */
	private Date startDateval;

	/**
	 * The cached value of the end date.
	 */
	private Date endDateval;

	/**
	 * The UiBinder interface used by this example.
	 */
	interface XBinder extends UiBinder<Widget, FilteredIngestionHistoryWidget> {
	}

	/**
	 * A reference to the repository service
	 */
	private final RepositoryServiceAsync repositoryService;

	/**
	 * The list of currently retrieved providers
	 */
	private List<ProviderDTO> providers = new ArrayList<ProviderDTO>();

	/**
	 * The list of currently retrieved collections
	 */
	private List<CollectionDTO> collections = new ArrayList<CollectionDTO>();

	/**
	 * @param executionService
	 * @param repositoryService
	 */
	public FilteredIngestionHistoryWidget(
			ExecutionServiceAsync executionService,
			RepositoryServiceAsync repositoryService) {
		super(executionService);
		this.repositoryService = repositoryService;
	}

	/**
	 * This method provides extra functionality for components that are meant to
	 * extend this one. It initializes all GWT components and attaches listeners
	 * to them.
	 * 
	 * @return widget
	 */
	@Override
	public Widget postInitialize() {
		// Initialize all components
		providerBox = new ListBox();
		collectionBox = new ListBox();
		workflowBox = new ListBox(true);
		workflowBox.setVisibleItemCount(8);
		startDate = new DatePicker();
		startDate.setVisible(false);
		endDate = new DatePicker();
		endDate.setVisible(false);
		startDateText = new TextBox();
		endDateText = new TextBox();
		searchButton = new Button();
		clearButton = new Button();
		clearStartDateBTN = new Button();
		clearEndDateBTN = new Button();

		clearStartDateBTN.setText("Clear");
		clearStartDateBTN.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {

				startDateText.setText("");
				startDateval = null;
				startDate.setValue(new Date());

			}

		});

		clearEndDateBTN.setText("Clear");
		clearEndDateBTN.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {

				endDateText.setText("");
				endDateval = null;
				endDate.setValue(new Date());

			}

		});

		startDateText.setReadOnly(true);
		startDateText.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				startDate.setVisible(true);
				clearStartDateBTN.setVisible(false);
			}

		});

		endDateText.setReadOnly(true);
		endDateText.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				endDate.setVisible(true);
				clearEndDateBTN.setVisible(false);
			}

		});

		startDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date date = event.getValue();
				startDateval = date;
				String dateString = date.toString();
				startDateText.setText(dateString);
				startDate.setVisible(false);
				clearStartDateBTN.setVisible(true);
			}
		});

		endDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date date = event.getValue();
				endDateval = date;
				String dateString = date.toString();
				endDateText.setText(dateString);
				endDate.setVisible(false);
				clearEndDateBTN.setVisible(true);
			}
		});

		repositoryService.getWorkflows(new AsyncCallback<List<WorkflowDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();

			}

			@Override
			public void onSuccess(List<WorkflowDTO> results) {
				for (WorkflowDTO result : results) {
					workflowBox.addItem(result.getName(),
							result.getIdentifier());
				}

			}

		});

		repositoryService.getProviders(new AsyncCallback<List<ProviderDTO>>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(List<ProviderDTO> result) {
				providers.clear();
				providerBox.clear();

				Collections.sort(result, new Comparator<ProviderDTO>() {
					@Override
					public int compare(ProviderDTO o1, ProviderDTO o2) {
						String n1 = o1.getCountry() == null ? "eu: "
								+ o1.getName() : o1.getCountry() + ": "
								+ o1.getName();
						String n2 = o2.getCountry() == null ? "eu: "
								+ o2.getName() : o2.getCountry() + ": "
								+ o2.getName();

						return n1.compareTo(n2);
					}
				});

				ProviderDTO dummyprovider = new ProviderDTO();
				dummyprovider.setId("dummyID");
				providers.add(dummyprovider);
				providers.addAll(result);

				for (ProviderDTO provider : providers) {
					if (provider.getId().equals("dummyID")) {
						// Add dummy provider entry
						providerBox.addItem("--");
					} else {
						String name = provider.getCountry() == null ? "eu: "
								+ provider.getName() : provider.getCountry()
								+ ": " + provider.getName();
						providerBox.addItem(name);
					}

				}

				if (providers.size() == 0) {
					return;
				}

				collectionBox.addItem("--");
			}

		});

		providerBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				ProviderDTO provider = providers.get(providerBox
						.getSelectedIndex());

				String prid = (String) provider.getId();

				repositoryService.getCollections(prid,
						new AsyncCallback<List<CollectionDTO>>() {
							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(List<CollectionDTO> result) {
								collections.clear();
								collectionBox.clear();

								Collections.sort(result,
										new Comparator<CollectionDTO>() {
											@Override
											public int compare(
													CollectionDTO o1,
													CollectionDTO o2) {
												String n1 = o1.getMnemonic()
														+ ": " + o1.getName();
												String n2 = o2.getMnemonic()
														+ ": " + o2.getName();
												return n1.compareTo(n2);
											}
										});

								collections.addAll(result);

								for (CollectionDTO collection : collections) {
									String name = collection.getMnemonic()
											+ ": " + collection.getName();
									collectionBox.addItem(name);
								}

								if (result.isEmpty()) {
									collectionBox.addItem("--");
								}

							}
						});
			}
		});

		searchButton.setText("Search");
		searchButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if (collections.size() > 0
						&& collectionBox.getSelectedIndex() >= 0
						&& collectionBox.getSelectedIndex() < collections
								.size()) {
					CollectionDTO collection = collections.get(collectionBox
							.getSelectedIndex());
					if (collection != null) {
						updatePastExecutions();
					}
				} else {
					updatePastExecutions();
				}
			}

		});

		clearButton.setText("Reset");
		clearButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {

				// Reset the collection/provider boxes
				providerBox.setSelectedIndex(0);
				collectionBox.clear();
				collectionBox.addItem("--");

				// Reset the workflow list
				resetSelectedWorkflows();
				// Reset the time range fields
				startDateText.setText("");
				startDateval = null;
				startDate.setValue(new Date());

				endDateText.setText("");
				endDateval = null;
				endDate.setValue(new Date());

			}

		});

		cellTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent arg0) {
				if (collections.size() > 0
						&& collectionBox.getSelectedIndex() >= 0
						&& collectionBox.getSelectedIndex() < collections
								.size()) {
					CollectionDTO collection = collections.get(collectionBox
							.getSelectedIndex());
					if (collection != null) {
						Range range = cellTable.getVisibleRange();
						int start = range.getStart();
						int length = range.getLength();

						updatePastExecutions();
					}
				}
			}
		});
		XBinder uiBinder = GWT.create(XBinder.class);
		Widget widget = uiBinder.createAndBindUi(this);
		return widget;
	}

	/**
	 * Retrieve current past executions.
	 */

	@Override
	public void updatePastExecutions() {
		String mnemonic = null;

		if (collectionBox != null) {

			int index = collectionBox.getSelectedIndex();

			String collvalue = collectionBox.getItemText(index);
			if (!collvalue.equals("--")) {
				mnemonic = collectionBox.getItemText(index).split(":")[0];
			}

		}

		String[] workflows = getSelectedWorkflows();

		executionService.getPastExecutions(workflows, mnemonic, startDateval,
				endDateval, new AsyncCallback<List<ExecutionDTO>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(List<ExecutionDTO> result) {
						pastExecutions.clear();
						pastExecutions.addAll(result);
						cellTable.setRowData(0, pastExecutions);
						cellTable.setRowCount(pastExecutions.size());
					}
				});

	}

	/**
	 * Deselects all the currently selected workflows.
	 */
	private void resetSelectedWorkflows() {

		for (int i = 0, l = workflowBox.getItemCount(); i < l; i++) {
			if (workflowBox.isItemSelected(i)) {
				workflowBox.setItemSelected(i, false);
			}
		}
	}

	/**
	 * Get all currently selected workflows
	 * 
	 * @return An array containing the workflow ids or null if nothing is
	 *         selected
	 */
	private String[] getSelectedWorkflows() {

		ArrayList<String> retval = new ArrayList<String>();

		if (workflowBox != null) {
			for (int i = 0, l = workflowBox.getItemCount(); i < l; i++) {
				if (workflowBox.isItemSelected(i)) {
					retval.add(workflowBox.getValue(i));
				}
			}
		}

		if (retval.isEmpty()) {
			return null;
		} else {
			return retval.toArray(new String[retval.size()]);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.gui.cp.client.monitoring.IngestionHistoryWidget#
	 * asyncOnInitialize(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
		GWT.runAsync(FilteredIngestionHistoryWidget.class,
				new RunAsyncCallback() {
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

}

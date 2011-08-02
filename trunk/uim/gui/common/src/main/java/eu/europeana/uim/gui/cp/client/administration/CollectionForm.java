package eu.europeana.uim.gui.cp.client.administration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import eu.europeana.uim.gui.cp.client.services.RepositoryServiceAsync;
import eu.europeana.uim.gui.cp.shared.CollectionDTO;

/**
 * A form used for editing contacts.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class CollectionForm extends Composite {
    private static Binder uiBinder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, CollectionForm> {
    }

    @UiField
    TextBox               mnemonicBox;
    @UiField
    TextBox               nameBox;
    @UiField
    TextBox               languageBox;
    @UiField
    TextBox               oaiBaseUrlBox;
    @UiField
    TextBox               oaiMetadataPrefixBox;
    @UiField
    TextBox               oaiSetBox;
    @UiField
    Button                commitButton;
    @UiField
    Button                cancelButton;

    private CollectionDTO collection;

    /**
     * Creates a new instance of this class.
     * 
     * @param repositoryService
     * @param listener
     */
    public CollectionForm(final RepositoryServiceAsync repositoryService,
                          final UpdateListener listener) {
        initWidget(uiBinder.createAndBindUi(this));

        commitButton.setEnabled(false);

        commitButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                String mnemonic = mnemonicBox.getText();
                mnemonic = mnemonic.trim();
                if (!mnemonic.matches("[\\w|\\d|-]*")) {
                    Window.alert("The mnemonic '" + mnemonic + "' is not valid, please use letters, number and '-'!");
                    return;
                }
                
                collection.setMnemonic(mnemonic);
                collection.setName(nameBox.getText());
                collection.setLanguage(languageBox.getText());
                collection.setOaiBaseUrl(oaiBaseUrlBox.getText());
                collection.setOaiMetadataPrefix(oaiMetadataPrefixBox.getText());
                collection.setOaiSet(oaiSetBox.getText());

                repositoryService.updateCollection(collection, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        caught.printStackTrace();
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result == null || !result) {
                            Window.alert("Could not update collection!");
                        }
                    }
                });

                clearForm();

                listener.updated(collection);
            }
        });

        cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                clearForm();
            }
        });
    }

    private void clearForm() {
        mnemonicBox.setText("");
        setCollection(null);
    }

    /**
     * @param collection
     */
    public void setCollection(CollectionDTO collection) {
        commitButton.setEnabled(true);
        this.collection = collection;
        mnemonicBox.setText(collection != null ? collection.getMnemonic() : "");
        nameBox.setText(collection != null ? collection.getName() : "");
        languageBox.setText(collection != null ? collection.getLanguage() : "");
        oaiBaseUrlBox.setText(collection != null ? collection.getOaiBaseUrl(false) : "");
        oaiMetadataPrefixBox.setText(collection != null ? collection.getOaiMetadataPrefix(false)
                : "");
        oaiSetBox.setText(collection != null ? collection.getOaiSet() : "");
        if (collection != null && collection.getId() != null) {
            mnemonicBox.setEnabled(false);
        } else {
            mnemonicBox.setEnabled(true);
        }
    }
}

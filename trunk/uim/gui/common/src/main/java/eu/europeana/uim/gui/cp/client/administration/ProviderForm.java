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
import eu.europeana.uim.gui.cp.shared.ProviderDTO;

/**
 * A form used for editing contacts.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 27, 2011
 */
public class ProviderForm extends Composite {
    private static Binder uiBinder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, ProviderForm> {
    }

    @UiField
    TextBox             mnemonicBox;
    @UiField
    TextBox             nameBox;
    @UiField
    TextBox             oaiBaseUrlBox;
    @UiField
    TextBox             oaiMetadataPrefixBox;
    @UiField
    Button              commitButton;
    @UiField
    Button              cancelButton;

    private ProviderDTO provider;

    /**
     * Creates a new instance of this class.
     * 
     * @param repositoryService
     * @param listener
     */
    public ProviderForm(final RepositoryServiceAsync repositoryService,
                        final UpdateListener listener) {
        initWidget(uiBinder.createAndBindUi(this));

        commitButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                String mnemonic = mnemonicBox.getText();
                mnemonic = mnemonic.trim();
                if (!mnemonic.matches("[\\w|\\d|-]*")) {
                    Window.alert("The mnemonic '" + mnemonic + "' is not valid, please use letters, number and '-'!");
                    return;
                }
                
                provider.setMnemonic(mnemonic);
                provider.setName(nameBox.getText());
                provider.setOaiBaseUrl(oaiBaseUrlBox.getText());
                provider.setOaiMetadataPrefix(oaiMetadataPrefixBox.getText());

                repositoryService.updateProvider(provider, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        caught.printStackTrace();
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result == null || !result) {
                            Window.alert("Could not update provider!");
                        }
                    }
                });

                clearForm();

                listener.updated(provider);
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
        setProvider(null);
    }

    /**
     * @param provider
     */
    public void setProvider(ProviderDTO provider) {
        this.provider = provider;
        mnemonicBox.setText(provider != null ? provider.getMnemonic() : "");
        nameBox.setText(provider != null ? provider.getName() : "");
        oaiBaseUrlBox.setText(provider != null ? provider.getOaiBaseUrl() : "");
        oaiMetadataPrefixBox.setText(provider != null ? provider.getOaiMetadataPrefix() : "");
        if (provider != null && provider.getId() != null) {
            mnemonicBox.setEnabled(false);
        } else {
            mnemonicBox.setEnabled(true);
        }
    }
}

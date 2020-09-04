package it.eng.portlet.consolepec.gwt.client.widget.protocollazione;

import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.client.widget.protocollazione.ElementoGruppoProtocollazioneWidget.AbstractEliminaCommand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class ElencoElementiProtocollazioneWidget extends Composite {

	private static ElencoElementiProtocollazioneWidgetUiBinder uiBinder = GWT.create(ElencoElementiProtocollazioneWidgetUiBinder.class);

	interface ElencoElementiProtocollazioneWidgetUiBinder extends UiBinder<Widget, ElencoElementiProtocollazioneWidget> {
	}

	public ElencoElementiProtocollazioneWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	HTMLPanel mainPanel;
	@UiField
	InlineLabel labelOggetto;
	@UiField
	Image iconaLabel;
	@UiField
	Button buttonElimina;
	private String elemento;

	public void mostraElemento(String elemento, AbstractEliminaCommand eliminaCommand) {
		this.elemento = elemento;
		labelOggetto.setText(elemento);
		iconaLabel.setResource(ConsolePECIcons._instance.gruppo());
		initEliminaCommand(eliminaCommand);
	}

	private void initEliminaCommand(final AbstractEliminaCommand command) {
		buttonElimina.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.elimina(elemento);

			}
		});
	}
}

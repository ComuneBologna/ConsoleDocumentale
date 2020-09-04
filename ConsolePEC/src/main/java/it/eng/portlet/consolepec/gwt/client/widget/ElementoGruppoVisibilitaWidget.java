package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class ElementoGruppoVisibilitaWidget extends Composite {

	private static ElementoGruppoVisibilitaWidgetUiBinder uiBinder = GWT.create(ElementoGruppoVisibilitaWidgetUiBinder.class);

	interface ElementoGruppoVisibilitaWidgetUiBinder extends UiBinder<Widget, ElementoGruppoVisibilitaWidget> {
	}

	@UiField
	HTMLPanel mainPanel;
	@UiField
	InlineLabel labelOggetto;
	@UiField
	Image iconaLabel;
	@UiField
	Button buttonElimina;

	private GruppoVisibilita gruppoVisibilita;

	public ElementoGruppoVisibilitaWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public Button getButtonEliminaButton() {
		return buttonElimina;
	}

	public void mostraGruppoVisibilita(final GruppoVisibilita gruppoVisibilita) {
		this.gruppoVisibilita = gruppoVisibilita;
		String label = this.gruppoVisibilita.getLabel();
		labelOggetto.setText(label);
		if (this.gruppoVisibilita.getTipoVisibilita().equals(GruppoVisibilita.TipoVisibilita.GRUPPO))
			iconaLabel.setResource(ConsolePECIcons._instance.gruppo());
		else if (this.gruppoVisibilita.getTipoVisibilita().equals(GruppoVisibilita.TipoVisibilita.UTENTE))
			iconaLabel.setResource(ConsolePECIcons._instance.utente());
	}

}

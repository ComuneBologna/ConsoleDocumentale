package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.CapofilaFromBA01DTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class ElementoProtocollazioneBA01ElencoWidget extends Composite {

	private static ElementoProtocollazioneBA01ElencoWidgetUiBinder uiBinder = GWT.create(ElementoProtocollazioneBA01ElencoWidgetUiBinder.class);

	interface ElementoProtocollazioneBA01ElencoWidgetUiBinder extends UiBinder<Widget, ElementoProtocollazioneBA01ElencoWidget> {
	}

	@UiField
	HTMLPanel mainPanel;
	@UiField
	CheckBox checkBox;
	@UiField
	InlineLabel labelOggetto;
	@UiField
	Image iconaLabel;

	public ElementoProtocollazioneBA01ElencoWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		setCheckBoxVisibility(false);
	}

	public void setCheckBoxVisibility(boolean visible) {
		checkBox.setVisible(visible);
	}

	public void mostraCapofila(final CapofilaFromBA01DTO capofilaDTO) {
		String oggetto = capofilaDTO.getOggetto();
		if (oggetto.length() > ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN)
			oggetto = capofilaDTO.getOggetto().substring(0, ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN).concat("...");
		labelOggetto.setText(oggetto);
		iconaLabel.setResource(ConsolePECIcons._instance.fascicoloBa01());
	}

}

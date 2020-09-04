package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.model.CatenaDocumentaleDTO.PgDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class ElementoCatenaDocumentaleWidget extends Composite {
	
	@UiField
	HTMLPanel mainPanel;
	@UiField
	CheckBox checkBox;
	@UiField
	InlineLabel labelPG;
	@UiField
	Image iconaLabel;
	
	private Command<Void, ElementoCatenaDocumentaleWidget> selezionaPGCommand;
	private PgDTO pg;
	private boolean checked;
		
	private static ElementoCatenaDocumentaleWidgetUiBinder uiBinder = GWT.create(ElementoCatenaDocumentaleWidgetUiBinder.class);

	interface ElementoCatenaDocumentaleWidgetUiBinder extends UiBinder<Widget, ElementoCatenaDocumentaleWidget> {
	}
	
	public ElementoCatenaDocumentaleWidget(PgDTO pg) {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		this.pg = pg;
	}

	public void mostraDettaglio() {
		
		checkBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				checked = ((CheckBox) event.getSource()).getValue();
				selezionaPGCommand.exe(ElementoCatenaDocumentaleWidget.this);
			}
		});
		
		iconaLabel.setResource(ConsolePECIcons._instance.fascicoloBa01());
		
		String dataPG = ConsolePecUtils.getDateToString(pg.getDataProtocollazione());
		
		StringBuffer sb = new StringBuffer(dataPG);
		if(pg.getNumFascicolo() != null)
			sb.append(" (").append(pg.getNumFascicolo()).append(")");
		sb.append(" - ");
		sb.append(pg.getOggetto());
		sb.append(", ");
		sb.append(pg.getDescrCellaAssegnazione());
		String descrizione = sb.toString();
		//if (descrizione.length() > ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN)
		//	descrizione = descrizione.substring(0, ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN).concat("...");
		labelPG.setText(descrizione);
	}
	
	public Command<Void, ElementoCatenaDocumentaleWidget> getSelezionaPGCommand() {
		return selezionaPGCommand;
	}

	public void setSelezionaPGCommand(Command<Void, ElementoCatenaDocumentaleWidget> selezionaPGCommand) {
		this.selezionaPGCommand = selezionaPGCommand;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public PgDTO getPg() {
		return pg;
	}
}

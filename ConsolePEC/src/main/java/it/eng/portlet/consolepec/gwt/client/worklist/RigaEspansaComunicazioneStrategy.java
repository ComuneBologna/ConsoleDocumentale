package it.eng.portlet.consolepec.gwt.client.worklist;

import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;

public class RigaEspansaComunicazioneStrategy extends AbstractRigaEspansaStrategy {

	/*private final PecInPraticheDB praticheDB;
	private final SitemapMenu sitemapMenu;

	public RigaEspansaTemplateStrategy(PecInPraticheDB praticheDB, SitemapMenu sitemapMenu) {
		this.praticheDB = praticheDB;
		this.sitemapMenu = sitemapMenu;
	}*/

	@Override
	public void disegnaDettaglio(HTMLPanel dettaglioContent, PraticaDTO pratica) {
		// Dati dei bean
		// id documentale
		Element label = DOM.createSpan();
		label.addClassName("label");
		label.setInnerText("ID documentale");
		dettaglioContent.getElement().appendChild(label);
		HTMLPanel panel = new HTMLPanel(pratica.getNumeroRepertorio());
		panel.addStyleName("abstract_note");
		dettaglioContent.add(panel);

		// TODO custom di fascicolo
		label = DOM.createSpan();
		label.addClassName("label");
		label.setInnerText("Codice");
		dettaglioContent.getElement().appendChild(label);
		String descr = sanitizeNull(((ComunicazioneDTO) pratica).getCodice());
		if (descr.length() > ConsolePecConstants.MAX_NUMERO_CARATTERI_TESTO_LUNGO)
			descr = descr.substring(0, ConsolePecConstants.MAX_NUMERO_CARATTERI_TESTO_LUNGO);
		panel = new HTMLPanel(descr);
		panel.addStyleName("abstract_note");
		dettaglioContent.add(panel);

		
	}

	@Override
	public void disegnaOperazioni(HTMLPanel operations, final PraticaDTO pratica, final EspandiRigaEventListener listener) {

		/* Pulsante di chiusura del dettaglio */
		Button chiudiButton = new Button();
		chiudiButton.setText("Chiudi");
		chiudiButton.setStyleName("btn black");
		chiudiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				listener.onEspandiRiga(pratica.getClientID(), pratica.getTipologiaPratica(), true);
			}
		});
		operations.add(chiudiButton);
	}

	
}

package it.eng.portlet.consolepec.gwt.client.worklist;

import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;

public class RigaEspansaPraticaModulisticaStrategy extends AbstractRigaEspansaStrategy {

	@Override
	public void disegnaDettaglio(HTMLPanel dettaglioContent, PraticaDTO pratica) {
		
		creaLabel(dettaglioContent, "Id Documentale");
		creaAbstract(dettaglioContent, sanitizeNull(pratica.getNumeroRepertorio()));

		creaLabel(dettaglioContent, "Titolo");
		creaAbstract(dettaglioContent, sanitizeNull(((PraticaModulisticaDTO) pratica).getTitolo()));
		
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

	protected HTMLPanel creaAbstract(HTMLPanel dettaglioContent, String value) {
		HTMLPanel panel = new HTMLPanel(value);
		panel.addStyleName("abstract_note");
		dettaglioContent.add(panel);
		return panel;
	}

	protected SpanElement creaLabel(HTMLPanel dettaglioContent, String value) {
		SpanElement span = Document.get().createSpanElement();
		span.addClassName("label");
		span.setInnerText(value);
		dettaglioContent.getElement().appendChild(span);
		return span;
	}

}

package it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi;

import com.google.gwt.dom.client.FieldSetElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato;
import it.eng.cobo.consolepec.util.datiaggiuntivi.DatiAggiuntiviUtil;

public interface PanelContenitoreStrategy {

	public static final PanelContenitoreStrategy CREAZIONE = new CreazioneFascicoloPanelContenitoreStrategy();
	public static final PanelContenitoreStrategy DETTAGLIO = new DettaglioFascicoloPanelContenitoreStrategy();
	public static final PanelContenitoreStrategy RICERCA = new RicercaFascicoloPanelContenitoreStrategy();
	public static final PanelContenitoreStrategy MODIFICA = new ModificaFascicoloPanelContenitoreStrategy();

	Widget creaContenitore(DatoAggiuntivo datoAggiuntivo, Widget datoAggiuntivoWidget);

	public class CreazioneFascicoloPanelContenitoreStrategy implements PanelContenitoreStrategy {

		@Override
		public Widget creaContenitore(DatoAggiuntivo datoAggiuntivo, Widget datoAggiuntivoWidget) {
			HTMLPanel panel = new HTMLPanel("");
			panel.setStyleName("cell acapo");

			SpanElement label = SpanElement.as(DOM.createSpan());
			label.setClassName("label");
			label.setInnerText(datoAggiuntivo.getDescrizione() + (DatiAggiuntiviUtil.isObbligatorio(datoAggiuntivo) && !datoAggiuntivo.getDescrizione().endsWith("*") ? " *" : ""));
			panel.getElement().appendChild(label);

			panel.add(datoAggiuntivoWidget);
			return panel;
		}

	}

	public class DettaglioFascicoloPanelContenitoreStrategy implements PanelContenitoreStrategy {

		@Override
		public Widget creaContenitore(DatoAggiuntivo datoAggiuntivo, Widget datoAggiuntivoWidget) {
			HTMLPanel panel = new HTMLPanel("");

			SpanElement label = SpanElement.as(DOM.createSpan());
			label.setClassName("label");
			label.setInnerText(datoAggiuntivo.getDescrizione() + (DatiAggiuntiviUtil.isObbligatorio(datoAggiuntivo) && !datoAggiuntivo.getDescrizione().endsWith("*") ? " *" : ""));
			panel.getElement().appendChild(label);

			FieldSetElement fieldSet = DOM.createFieldSet().cast();
			panel.getElement().appendChild(fieldSet);

			HTMLPanel subPanel = new HTMLPanel("");
			subPanel.setStyleName("cell acapo");
			subPanel.add(datoAggiuntivoWidget);

			panel.add(subPanel, fieldSet);

			return panel;
		}

	}

	public class RicercaFascicoloPanelContenitoreStrategy implements PanelContenitoreStrategy {

		@Override
		public Widget creaContenitore(DatoAggiuntivo datoAggiuntivo, Widget datoAggiuntivoWidget) {
			// DivElement div = DOM.createDiv().cast();
			HTMLPanel panel = new HTMLPanel("");

			if (!datoAggiuntivo.getTipo().equals(TipoDato.MultiploTesto)) {
				panel.setStyleName("cell");
			} else {
				panel.setStyleName("cell multitestoCell");
			}

			panel.add(datoAggiuntivoWidget);

			return panel;
		}

	}

	public class ModificaFascicoloPanelContenitoreStrategy implements PanelContenitoreStrategy {

		@Override
		public Widget creaContenitore(DatoAggiuntivo datoAggiuntivo, Widget datoAggiuntivoWidget) {
			HTMLPanel panel = new HTMLPanel("");
			panel.setStyleName("cell acapo");

			SpanElement label = SpanElement.as(DOM.createSpan());
			label.setClassName("label");
			label.setInnerText(datoAggiuntivo.getDescrizione() + (DatiAggiuntiviUtil.isObbligatorio(datoAggiuntivo) && !datoAggiuntivo.getDescrizione().endsWith("*") ? " *" : ""));
			panel.getElement().appendChild(label);

			panel.add(datoAggiuntivoWidget);

			return panel;
		}

	}
}

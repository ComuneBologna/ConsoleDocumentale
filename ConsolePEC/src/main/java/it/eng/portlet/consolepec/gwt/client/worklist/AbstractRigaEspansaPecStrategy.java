package it.eng.portlet.consolepec.gwt.client.worklist;

import com.google.gwt.dom.client.BRElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTMLPanel;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.WidgetUtils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.TextUtils;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.Destinatario;
import it.eng.portlet.consolepec.gwt.shared.model.PecDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public abstract class AbstractRigaEspansaPecStrategy extends AbstractRigaEspansaStrategy {
	protected Command<Void, AllegatoDTO> downloadAllegatoCommand;

	@Override
	public void disegnaDettaglio(HTMLPanel dettaglioContent, PraticaDTO pratica) {
		PecDTO pec = (PecDTO) pratica;
		/* aggiunta dei destinatari */
		WidgetUtils.creaLabel(dettaglioContent, "Destinatari");
		HTMLPanel panel = WidgetUtils.creaAbstract(dettaglioContent, "", false);
		for (Destinatario destinatario : pec.getDestinatari()) {
			if (destinatario != null) {
				SpanElement span = Document.get().createSpanElement();
				span.setInnerSafeHtml(SafeHtmlUtils.fromString(destinatario.getDestinatario()));
				BRElement br = Document.get().createBRElement();
				panel.getElement().appendChild(span);
				panel.getElement().appendChild(br);
			}
		}
		if (panel.getElement().getChildCount() == 0)
			panel.getElement().setInnerHTML(sanitizeNull(null));

		/* aggiunta dell'oggetto */
		WidgetUtils.creaLabel(dettaglioContent, "Oggetto");
		WidgetUtils.creaAbstract(dettaglioContent, sanitizeNull(pec.getTitolo()), false);
		/* ID documentale */
		WidgetUtils.creaLabel(dettaglioContent, "ID documentale");
		WidgetUtils.creaAbstract(dettaglioContent, sanitizeNull(pec.getNumeroRepertorio()), false);
		/* Body */
		String body = sanitizeNull(pec.getBody());
		body = TextUtils.textToHTML(TextUtils.reduce(body, ConsolePecConstants.MAX_NUMERO_CARATTERI_TESTO_LUNGO, ConsolePecConstants.MAX_NUMERO_RIGHE_TESTO_LUNGO));
		WidgetUtils.creaLabel(dettaglioContent, "Corpo email");
		WidgetUtils.creaAbstract(dettaglioContent, body, false);
		/* Allegati */
		WidgetUtils.creaLabel(dettaglioContent, "Allegati");
		if (pec.getAllegati().size() > 0) {
			panel = WidgetUtils.creaAbstract(dettaglioContent, "", false);
			DivElement corpoDiv = Document.get().createDivElement();
			corpoDiv.setClassName("corpo");
			panel.getElement().appendChild(corpoDiv);
			DivElement boxMailDiv = Document.get().createDivElement();
			boxMailDiv.setClassName("box-mail");
			corpoDiv.appendChild(boxMailDiv);
			for (AllegatoDTO allegato : pec.getAllegati()) {
				ElementoAllegatoElencoWidget allgWidget = new ElementoAllegatoElencoWidget();
				panel.add(allgWidget, boxMailDiv);
				allgWidget.setCheckBoxVisible(false);
				allgWidget.setDownloadAllegatoCommand(downloadAllegatoCommand);
				if (getMostraDettaglioAllegatoCommand() != null) {
					allgWidget.setMostraDettaglioAllegatoCommand(getMostraDettaglioAllegatoCommand());
				}
				allgWidget.mostraDettaglio(allegato);
			}
		} else {
			WidgetUtils.creaAbstract(dettaglioContent, sanitizeNull(null), false);
		}
	}

	protected abstract Command<Void, AllegatoDTO> getMostraDettaglioAllegatoCommand();
}

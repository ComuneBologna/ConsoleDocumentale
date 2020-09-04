package it.eng.portlet.consolepec.gwt.client.worklist;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;

import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.portlet.consolepec.gwt.client.widget.WidgetUtils;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistCartellaFirmaStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioGruppoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioUtenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;

/**
 *
 * @author biagiot
 *
 */
public class RigaEspansaCartellaFirmaStrategy {

	public void disegnaDettaglio(HTMLPanel destination, DocumentoFirmaVistoDTO documentoFirmaVistoDTO) {

		WidgetUtils.creaLabel(destination, "Note");
		String noteText = GenericsUtil.sanitizeNull(documentoFirmaVistoDTO.getNote(), "-");
		noteText = GenericsUtil.convertNewLinesToHTML(noteText);

		if (noteText.length() > ConsolePecConstants.MAX_NUMERO_CARATTERI_TESTO_LUNGO)
			noteText = noteText.substring(0, ConsolePecConstants.MAX_NUMERO_CARATTERI_TESTO_LUNGO);
		WidgetUtils.creaAbstract(destination, noteText, true);

		WidgetUtils.creaLabel(destination, "Destinatari Firma");
		HTMLPanel panel = WidgetUtils.creaAbstract(destination, "", false);
		DivElement corpoDiv = Document.get().createDivElement();
		corpoDiv.setClassName("corpo");
		panel.getElement().appendChild(corpoDiv);
		DivElement boxMailDiv = Document.get().createDivElement();
		boxMailDiv.setClassName("box-mail");
		corpoDiv.appendChild(boxMailDiv);

		for (DestinatarioDTO destinatario : documentoFirmaVistoDTO.getDestinatariFirma()) {

			HTMLPanel p = new HTMLPanel("");
			p.setStyleName("documenti-mail");

			SpanElement spanIcona = Document.get().createSpanElement();
			Image icon = null;

			if (destinatario instanceof DestinatarioUtenteDTO)
				icon = new Image(ConsolePECIcons._instance.utente());
			else if (destinatario instanceof DestinatarioGruppoDTO)
				icon = new Image(ConsolePECIcons._instance.gruppo());

			spanIcona.appendChild(icon.getElement());
			p.getElement().appendChild(spanIcona);

			SpanElement spanDestinatari = Document.get().createSpanElement();
			spanDestinatari.addClassName("documento-semplice");
			LabelElement labelElement = Document.get().createLabelElement();
			StringBuilder sb = new StringBuilder(getNomeDestinatario(destinatario));
			sb.append(" - Stato richiesta: ").append(destinatario.getStatoRichiesta().getLabel());
			labelElement.setInnerText(" " + sb.toString());
			spanDestinatari.appendChild(labelElement);
			p.getElement().appendChild(spanDestinatari);

			boxMailDiv.appendChild(p.getElement());
		}
	}

	public void disegnaOperazioni(HTMLPanel operations, final DocumentoFirmaVistoDTO documentoFirmaVistoDTO, final EspandiRigaEventListener listener) {
		Button chiudiButton = new Button();
		chiudiButton.setText("Chiudi");
		chiudiButton.setStyleName("btn black");
		chiudiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				listener.onEspandiRiga(documentoFirmaVistoDTO, true);
			}
		});

		operations.add(chiudiButton);
	}

	private String getNomeDestinatario(DestinatarioDTO destinatario) {
		if (destinatario instanceof DestinatarioUtenteDTO) {
			return ((DestinatarioUtenteDTO) destinatario).getNomeCompleto();

		} else if (destinatario instanceof DestinatarioGruppoDTO) {
			return ((DestinatarioGruppoDTO) destinatario).getNomeGruppoDisplay();

		} else
			return "";
	}
}

package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Image;

/**
 *
 * @author biagiot
 *
 */
public class UtenteWidget extends ComplexPanel{

	DivElement divContainer;
	DivElement documento;
	DivElement azioni;

	private Command<Void, Utente> eliminaCommand;

	public UtenteWidget() {
		super();
		divContainer = Document.get().createDivElement();
		setElement(divContainer);
		divContainer.setClassName("documenti-mail");

		documento  = Document.get().createDivElement();
		documento.setClassName("documento-container");
		divContainer.appendChild(documento);

		azioni = Document.get().createDivElement();
		azioni.setClassName("documento-azioni");
		divContainer.appendChild(azioni);
	}

	public void showWidget(final Utente utenteDTO, boolean editable) {

		SpanElement span = Document.get().createSpanElement();
		span.setClassName("documento");
		documento.appendChild(span);

		Image icona = new Image(ConsolePECIcons._instance.utente());
		span.appendChild(icona.getElement());

		StringBuilder sb = new StringBuilder(utenteDTO.getNomeCompleto());

		if (!Strings.isNullOrEmpty(utenteDTO.getMatricola()))
			sb.append(" - ").append(utenteDTO.getMatricola());

		if (!Strings.isNullOrEmpty(utenteDTO.getDipartimento()))
			sb.append(" - ").append(utenteDTO.getDipartimento());

		LabelElement label = Document.get().createLabelElement();
		label.setInnerText(sb.toString());
		span.appendChild(label);

		Button eliminaButton = new Button("Elimina");
		eliminaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eliminaCommand.exe(utenteDTO);
				removeFromParent();
			}
		});

		eliminaButton.setStyleName("btn black");
		eliminaButton.setEnabled(editable);
		add(eliminaButton, azioni);
	}

	public void setEliminaCommand(Command<Void, Utente> eliminaCommand) {
		this.eliminaCommand = eliminaCommand;
	}
}

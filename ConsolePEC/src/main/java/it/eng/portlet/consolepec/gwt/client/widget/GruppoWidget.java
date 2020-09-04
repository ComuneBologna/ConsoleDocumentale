package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;

/**
 *
 * @author biagiot
 *
 */
public class GruppoWidget extends ComplexPanel{

	DivElement divContainer;
	DivElement documento;
	DivElement azioni;

	private Command<Void, AnagraficaRuolo> eliminaCommand;
	private Command<Void, String> selectCommand;

	public GruppoWidget(boolean box) {
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

	public GruppoWidget() {
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

	public void showWidgetWithRadioButton(final String ruolo) {
		SpanElement span = Document.get().createSpanElement();
		span.setClassName("documento");
		documento.appendChild(span);

		RadioButton radioButton = new RadioButton("group");
		radioButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectCommand.exe(ruolo);
			}
		});

		add(radioButton, span);

		Image icona = new Image(ConsolePECIcons._instance.gruppo());
		span.appendChild(icona.getElement());

		LabelElement label = Document.get().createLabelElement();
		label.setInnerText(ruolo);
		span.appendChild(label);
	}

	public void showWidget(final AnagraficaRuolo ruolo, boolean editable, boolean eliminaButtonEnabled) {

		SpanElement span = Document.get().createSpanElement();
		span.setClassName("documento");
		documento.appendChild(span);

		Image icona = new Image(ConsolePECIcons._instance.gruppo());
		span.appendChild(icona.getElement());

		LabelElement label = Document.get().createLabelElement();
		label.setInnerText(ruolo.getEtichetta());
		span.appendChild(label);

		if (eliminaButtonEnabled) {
			Button eliminaButton = new Button("Elimina");
			eliminaButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					eliminaCommand.exe(ruolo);
					removeFromParent();
				}
			});

			eliminaButton.setStyleName("btn black");
			eliminaButton.setEnabled(editable);
			add(eliminaButton, azioni);
		}
	}
	
	public void showWidget(final AnagraficaRuolo ruolo, boolean editable) {
		showWidget(ruolo, editable, true);
	}

	public void setEliminaCommand(Command<Void, AnagraficaRuolo> eliminaCommand) {
		this.eliminaCommand = eliminaCommand;
	}

	public void setSelectCommand(Command<Void, String> selectCommand) {
		this.selectCommand = selectCommand;
	}
}

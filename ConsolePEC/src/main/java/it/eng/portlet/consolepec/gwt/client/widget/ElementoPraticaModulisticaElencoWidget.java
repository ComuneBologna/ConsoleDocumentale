package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Widget che disegna un riferimento PEC in un elenco di un dettaglio
 * 
 * @author pluttero
 * 
 */
public class ElementoPraticaModulisticaElencoWidget extends ComplexPanel {

	// private static ElementoPECElencoWidgetUiBinder uiBinder = GWT.create(ElementoPECElencoWidgetUiBinder.class);
	//
	// interface ElementoPECElencoWidgetUiBinder extends UiBinder<Widget, ElementoPECElencoWidget> {
	// }

	Image iconaEmail;
	CheckBox checkBox;
	Anchor linkDettaglio;
	SpanElement labelDettaglio;
	ParagraphElement p;
	private boolean checkDisable = false;
	private Command<Void, MostraPraticaModulistica> mostraDettaglioPraticaModulistica;
	private Command<Void, SelezionePraticaModulistica> selezionaPraticaModulisticaCommand;

	public ElementoPraticaModulisticaElencoWidget() {
		super();
		p = Document.get().createPElement();
		setElement(p);
		p.setClassName("documenti-mail");
		checkBox = new CheckBox();
		linkDettaglio = new Anchor();
		labelDettaglio = Document.get().createSpanElement();
	}

	public void mostraDettaglio(final PraticaModulisticaDTO praticaModulistica) {

		/* Checkbox di selezione */
		checkBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				boolean checked = ((CheckBox) event.getSource()).getValue();
				SelezionePraticaModulistica sp = new SelezionePraticaModulistica();
				sp.checked = checked;
				sp.clientID = praticaModulistica.getClientID();
				selezionaPraticaModulisticaCommand.exe(sp);
			}
		});
		checkBox.setStyleName("checkbox-nonprot");
		add(checkBox, p);

		/* Icona e link */
		String titolo = praticaModulistica.getTitolo();
		if (titolo == null || titolo.trim().length() == 0)
			titolo = "Nessun oggetto";
		StringBuilder sb = new StringBuilder(titolo);

		iconaEmail = new Image(ConsolePECIcons._instance.praticamodulistica());
		iconaEmail.setTitle("Pratica Modulistica");
		
		add(iconaEmail, p);
		if (mostraDettaglioPraticaModulistica != null) {
			linkDettaglio.setHref("javascript:;");
			linkDettaglio.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					SelezionePraticaModulistica sel = new SelezionePraticaModulistica();
					sel.checked = false;
					sel.clientID = praticaModulistica.getClientID();
					mostraDettaglioPraticaModulistica.exe(sel);
				}
			});
			linkDettaglio.setText(sb.toString());
			linkDettaglio.setStyleName("documento");
			add(linkDettaglio, p);
		} else {
			labelDettaglio.setInnerText(sb.toString());
			labelDettaglio.setClassName("documento-semplice");
			getElement().appendChild(labelDettaglio);
		}
		// linkDettaglio.setTargetHistoryToken(NameTokens.dettagliopecoutbozza);
		iconaEmail.setStyleName("ico-mail");

	}

	public Command<Void, MostraPraticaModulistica> getMostraDettaglioPraticaModulistica() {
		return mostraDettaglioPraticaModulistica;
	}

	public void setMostraDettaglioPraticaModulistica(Command<Void, MostraPraticaModulistica> mostraDettaglioPraticaModulistica) {
		this.mostraDettaglioPraticaModulistica = mostraDettaglioPraticaModulistica;
	}

	public Command<Void, SelezionePraticaModulistica> getSelezionaPraticaModulisticaCommand() {
		return selezionaPraticaModulisticaCommand;
	}

	public void setSelezionaPraticaModulisticaCommand(Command<Void, SelezionePraticaModulistica> selezionaPraticaModulisticaCommand) {
		this.selezionaPraticaModulisticaCommand = selezionaPraticaModulisticaCommand;
	}

	public void setCheckBoxVisible(boolean show) {
		checkBox.setVisible(show);
	}

	public void setCheckBoxEnabled(boolean enabled) {
		if (checkDisable)
			checkBox.setEnabled(false);
		else
			checkBox.setEnabled(enabled);
	}

	public class MostraPraticaModulistica {
		
		String clientID;

		public String getClientID() {
			return clientID;
		}

		public void setClientID(String clientID) {
			this.clientID = clientID;
		}

	}

	public class SelezionePraticaModulistica extends MostraPraticaModulistica {
		boolean checked;

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}
	}
}

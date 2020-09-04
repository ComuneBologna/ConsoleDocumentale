package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;

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
public class ElementoComunicazioneElencoWidget extends ComplexPanel {

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
	private Command<Void, MostraComunicazione> mostraDettaglioComunicazioneCommand;
	private Command<Void, SelezioneComunicazione> selezionaComunicazioneCommand;

	public ElementoComunicazioneElencoWidget() {
		super();
		p = Document.get().createPElement();
		setElement(p);
		p.setClassName("oggetto-mail");
		checkBox = new CheckBox();
		linkDettaglio = new Anchor();
		labelDettaglio = Document.get().createSpanElement();
	}

	public void mostraDettaglio(final ComunicazioneDTO comunicazione) {

		/* Checkbox di selezione */
		checkBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				boolean checked = ((CheckBox) event.getSource()).getValue();
				SelezioneComunicazione sp = new SelezioneComunicazione();
				sp.checked = checked;
				sp.clientID = comunicazione.getClientID();
				selezionaComunicazioneCommand.exe(sp);
			}
		});
		checkBox.setStyleName("checkbox-nonprot");
		add(checkBox, p);

		/* Icona e link */
		String titolo = comunicazione.getCodice();
		if (titolo == null || titolo.trim().length() == 0)
			titolo = "Nessun oggetto";
		StringBuilder sb = new StringBuilder(titolo);

		iconaEmail = new Image(ConsolePECIcons._instance.praticamodulistica());
		iconaEmail.setTitle("Comunicazione");
		
		add(iconaEmail, p);
		if (mostraDettaglioComunicazioneCommand != null) {
			linkDettaglio.setHref("javascript:;");
			linkDettaglio.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					SelezioneComunicazione sel = new SelezioneComunicazione();
					sel.checked = false;
					sel.clientID = comunicazione.getClientID();
					mostraDettaglioComunicazioneCommand.exe(sel);
				}
			});
			linkDettaglio.setText(sb.toString());
			linkDettaglio.setStyleName("documento");
			add(linkDettaglio, p);
		} else {
			labelDettaglio.setInnerText(sb.toString());
			labelDettaglio.setClassName("documento");
			getElement().appendChild(labelDettaglio);
		}
		// linkDettaglio.setTargetHistoryToken(NameTokens.dettagliopecoutbozza);
		iconaEmail.setStyleName("ico-mail");

	}

	public Command<Void, MostraComunicazione> getMostraDettaglioComunicazione() {
		return mostraDettaglioComunicazioneCommand;
	}

	public void setMostraDettaglioComunicazione(Command<Void, MostraComunicazione> mostraDettaglioComunicazioneCommand) {
		this.mostraDettaglioComunicazioneCommand = mostraDettaglioComunicazioneCommand;
	}

	public Command<Void, SelezioneComunicazione> getSelezionaComunicazioneCommand() {
		return selezionaComunicazioneCommand;
	}

	public void setSelezionaComunicazioneCommand(Command<Void, SelezioneComunicazione> selezionaComunicazioneCommand) {
		this.selezionaComunicazioneCommand = selezionaComunicazioneCommand;
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

	public class MostraComunicazione {
		
		String clientID;

		public String getClientID() {
			return clientID;
		}

		public void setClientID(String clientID) {
			this.clientID = clientID;
		}

	}

	public class SelezioneComunicazione extends MostraComunicazione {
		boolean checked;

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}
	}
}

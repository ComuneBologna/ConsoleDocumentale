package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.PecDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

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
public class ElementoPECElencoWidget extends ComplexPanel {

	// private static ElementoPECElencoWidgetUiBinder uiBinder = GWT.create(ElementoPECElencoWidgetUiBinder.class);
	//
	// interface ElementoPECElencoWidgetUiBinder extends UiBinder<Widget, ElementoPECElencoWidget> {
	// }

	Image iconaEmail;
	CheckBox checkBox;
	Anchor linkDettaglio;
	Anchor linkImage;
	SpanElement labelDettaglio;
	ParagraphElement p;
	private boolean checkDisable = false;
	private Command<Void, MostraPEC> mostraDettaglioPEC;
	private Command<Void, SelezionePEC> selezionaPECCommand;

	public ElementoPECElencoWidget() {
		super();
		p = Document.get().createPElement();
		setElement(p);
		p.setClassName("documenti-mail");
		checkBox = new CheckBox();
		linkDettaglio = new Anchor();
		linkImage = new Anchor();
		labelDettaglio = Document.get().createSpanElement();
	}

	public void mostraDettaglio(final PecDTO pec) {

		if (pec instanceof PecOutDTO) {
			this.checkDisable = ((PecOutDTO) pec).isReinoltro();
			if (checkDisable)
				setCheckBoxEnabled(false);
		}

		/* Checkbox di selezione */
		checkBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				boolean checked = ((CheckBox) event.getSource()).getValue();
				SelezionePEC sp = new SelezionePEC();
				sp.checked = checked;
				sp.clientID = pec.getClientID();
				sp.tipo = TipoRiferimentoPEC.getTipo(pec);
				selezionaPECCommand.exe(sp);
			}
		});
		checkBox.setStyleName("checkbox-nonprot");
		add(checkBox, p);

		/* Icona e link */
		String titolo = pec.getTitolo();
		if (titolo == null || titolo.trim().length() == 0)
			titolo = "Nessun oggetto";
		StringBuilder sb = new StringBuilder(pec.getDataOraCreazione()).append(" - ").append(titolo);
		if (pec instanceof PecInDTO) {// PECIN
			iconaEmail = new Image(ConsolePECIcons._instance.bustinaChiusaEmail());
			iconaEmail.setTitle("Pec in ingresso");
		} else {// PECOUT
			if (pec instanceof PecOutDTO) {
				PecOutDTO p = (PecOutDTO) pec;
				if (p.isReinoltro()) {
					iconaEmail = new Image(ConsolePECIcons._instance.reinoltro());
					iconaEmail.setTitle("Reinoltro");
				} else {
					iconaEmail = new Image(ConsolePECIcons._instance.bustinaApertaEmail());
					iconaEmail.setTitle("Pec in uscita");
				}
			}
			sb.append(" (Stato: ").append(pec.getStatoLabel()).append(")");
		}
		
		linkImage.getElement().appendChild(iconaEmail.getElement());
		linkImage.setStyleName("ico verifica");
		
		add(linkImage, p);
		
		if (mostraDettaglioPEC != null) {
			linkDettaglio.setHref("javascript:;");
			linkDettaglio.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					SelezionePEC sel = new SelezionePEC();
					sel.checked = false;
					sel.clientID = pec.getClientID();
					sel.tipo = TipoRiferimentoPEC.getTipo(pec);
					mostraDettaglioPEC.exe(sel);
				}
			});
			linkDettaglio.setText(sb.toString());
			
			labelDettaglio.setClassName("documento-semplice");
			add(linkDettaglio, labelDettaglio);
			
			//linkDettaglio.setStyleName("documento-semplice");
			getElement().appendChild(labelDettaglio);
		} else {
			labelDettaglio.setInnerText(sb.toString());
			labelDettaglio.setClassName("documento-semplice");
			getElement().appendChild(labelDettaglio);
		}
		// linkDettaglio.setTargetHistoryToken(NameTokens.dettagliopecoutbozza);
		iconaEmail.setStyleName("ico-mail");

	}

	public Command<Void, MostraPEC> getMostraDettaglioPEC() {
		return mostraDettaglioPEC;
	}

	public void setMostraDettaglioPEC(Command<Void, MostraPEC> mostraDettaglioPEC) {
		this.mostraDettaglioPEC = mostraDettaglioPEC;
	}

	public Command<Void, SelezionePEC> getSelezionaPECCommand() {
		return selezionaPECCommand;
	}

	public void setSelezionaPECCommand(Command<Void, SelezionePEC> selezionaPECCommand) {
		this.selezionaPECCommand = selezionaPECCommand;
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

	public class MostraPEC {
		TipoRiferimentoPEC tipo;

		String clientID;

		public String getClientID() {
			return clientID;
		}

		public void setClientID(String clientID) {
			this.clientID = clientID;
		}

		public TipoRiferimentoPEC getTipo() {
			return tipo;
		}

		public void setTipo(TipoRiferimentoPEC tipo) {
			this.tipo = tipo;
		}
	}

	public class SelezionePEC extends MostraPEC {
		boolean checked;

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}
	}
}

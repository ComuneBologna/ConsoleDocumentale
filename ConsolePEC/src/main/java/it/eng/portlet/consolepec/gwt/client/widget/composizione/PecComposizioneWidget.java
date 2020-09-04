package it.eng.portlet.consolepec.gwt.client.widget.composizione;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Widget che disegna un riferimento PEC in un elenco di un dettaglio
 * 
 * @author pluttero
 * 
 */
public class PecComposizioneWidget extends ComplexPanel {

	// private static ElementoPECElencoWidgetUiBinder uiBinder = GWT.create(ElementoPECElencoWidgetUiBinder.class);
	//
	// interface ElementoPECElencoWidgetUiBinder extends UiBinder<Widget, ElementoPECElencoWidget> {
	// }
	
	CheckBox checkBox;
	
	DivElement divContainer;
	
	DivElement documento;
	DivElement azioni;
	

	HTMLPanel allegati;
	
	private boolean checkDisable = false;
	private Command<Void, MostraPEC> mostraDettaglioPEC;
	private Command<Void, SelezionePEC> selezionaPECCommand;

	private Command<Void, AllegatoDTO> mostraAllegatoCommand;
	private Command<Void, AllegatoDTO> downloadAllegatoCommand;
	private boolean capofila = false;
	
	private boolean mostraInformazioniDiProtocollazione = false;
	
	private SlideAnimation slideAnimation;
	
	public PecComposizioneWidget() {
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
		
		allegati = new HTMLPanel("");
		allegati.setVisible(false);
		allegati.setStyleName("attachment-container");
		add(allegati, divContainer);
		
		slideAnimation = new SlideAnimation(allegati);
		
		checkBox = new CheckBox();
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
		add(checkBox, documento);
		
		if(mostraInformazioniDiProtocollazione) {
			if(capofila){
				SpanElement capofilaSpan = Document.get().createSpanElement();
				capofilaSpan.setInnerHTML(ConsolePecConstants.CAPOFILA_LABEL);
				capofilaSpan.setClassName("capofila");
				documento.appendChild(capofilaSpan);
			}
			
			

			SpanElement protocollo = Document.get().createSpanElement();
			if(pec.getNumeroPG() != null){
				protocollo.setInnerText(pec.getNumeroPG() + "/" + pec.getAnnoPG());	
			} else {
				protocollo.setInnerText(ConsolePecConstants.NON_PROTOCOLLATO);
			}
			protocollo.setClassName("protocollo");
			documento.appendChild(protocollo);
			
			SpanElement span = Document.get().createSpanElement();
			span.setInnerText(" - ");
			documento.appendChild(span);
	
		}
				
		SpanElement data = Document.get().createSpanElement();
		data.setInnerHTML(pec.getDataOraCreazione());
		data.setClassName("data");
		documento.appendChild(data);
		
		if(mostraInformazioniDiProtocollazione && pec.getNumeroPGCapofila() != null) {
			SpanElement numPGAnnoPGCapofilaSpan = Document.get().createSpanElement();
			numPGAnnoPGCapofilaSpan.setInnerHTML(" - CAPOFILA " + pec.getNumeroPGCapofila() + "/" + pec.getAnnoPGCapofila());
			numPGAnnoPGCapofilaSpan.setClassName("data");
			documento.appendChild(numPGAnnoPGCapofilaSpan);
		}
		
		SpanElement dettaglioSpan = Document.get().createSpanElement();
		dettaglioSpan.setClassName("documento");
		documento.appendChild(dettaglioSpan);
		
		/* Icona e link */
		String titolo = pec.getTitolo();
		if (titolo == null || titolo.trim().length() == 0)
			titolo = "Nessun oggetto";
		StringBuilder sb = new StringBuilder(titolo);
		Image iconaEmail = null;
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
		
		AnchorElement iconaAnchor = Document.get().createAnchorElement();
		iconaAnchor.setClassName("ico verifica");
		add(iconaEmail, iconaAnchor);
		dettaglioSpan.appendChild(iconaAnchor);
		
		
		// dettaglio
		Anchor linkDettaglio = new Anchor();
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
			add(linkDettaglio, dettaglioSpan);
		}  else {
			
			SpanElement labelDettaglio = Document.get().createSpanElement();
			labelDettaglio.setInnerText(sb.toString());
			dettaglioSpan.appendChild(labelDettaglio);
		}
		iconaEmail.setStyleName("ico-mail");
		
		
		// inviato
		DivElement inviato = Document.get().createDivElement();
		StringBuilder inviatoSb = new StringBuilder(); 
		inviatoSb.append("Mittente \"" + pec.getMittente() + "\"");
		if(!pec.getDestinatari().isEmpty()) {
			inviatoSb.append(" - Destinatari "); 
			for(it.eng.portlet.consolepec.gwt.shared.model.Destinatario d : pec.getDestinatari()) inviatoSb.append("\"" + d.getDestinatario() + "\" ");
			for(it.eng.portlet.consolepec.gwt.shared.model.Destinatario d : pec.getDestinatariCC()) inviatoSb.append("\"" + d.getDestinatario() + "\" ");
		}
		inviato.setInnerText(inviatoSb.toString());
		inviato.getStyle().setFontSize(80, Unit.PCT);
		inviato.getStyle().setMarginLeft(27, Unit.PX);
		documento.appendChild(inviato);	
		
		Button vediAllegati = new Button(ConsolePecConstants.VEDI_ALLEGATI_COMPOSIZIONE_FASCICOLO);
		vediAllegati.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				slideAnimation.run(200);
				//allegati.setVisible(!allegati.isVisible());
			}
		});
		vediAllegati.setStyleName("btn black");
		if(pec.getAllegati().isEmpty()){
			vediAllegati.setEnabled(false);
		}
		add(vediAllegati, azioni);
		
		
		
		if(!pec.getAllegati().isEmpty()){
			for(final AllegatoDTO allegato :  pec.getAllegati()){
				HTMLPanel allegatoPanel = new HTMLPanel("");
				allegatoPanel.setStyleName("documenti-mail mail-attach");
				allegati.add(allegatoPanel);
				
				HTMLPanel spanAllegato = new HTMLPanel("");
				spanAllegato.setStyleName("documento");
				allegatoPanel.add(spanAllegato);
				
				
				Image iconaFirma = new Image(allegato.getIconaStato(ConsolePECIcons._instance));
				iconaFirma.setStyleName("ico-mail");
				
				Anchor mostraAllegatoAncor = new Anchor();
				mostraAllegatoAncor.setStyleName("ico verifica");
				mostraAllegatoAncor.setTitle("Visualizza dettagli");
				mostraAllegatoAncor.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						mostraAllegatoCommand.exe(allegato);
					}
				});
				mostraAllegatoAncor.getElement().appendChild(iconaFirma.getElement());
				spanAllegato.add(mostraAllegatoAncor );
				
				// dettaglio
				Anchor downloadAllegato = new Anchor(ConsolePecUtils.getLabelComposizioneFascicolo(allegato));
				downloadAllegato.setHref("javascript:;");
				downloadAllegato.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						downloadAllegatoCommand.exe(allegato);
					}
				});
				downloadAllegato.setTitle(allegato.getLabel());
				spanAllegato.add(downloadAllegato);
			}

		} else {
			
			// aggiungo un allegato vuoto per questioni di spazio
			HTMLPanel allegatoPanel = new HTMLPanel("");
			allegatoPanel.setStyleName("documenti-mail mail-attach");
			allegati.add(allegatoPanel);
			
			HTMLPanel spanAllegato = new HTMLPanel("");
			spanAllegato.setStyleName("documento");
			allegatoPanel.add(spanAllegato);
			
			Image iconaFirma = new Image(ConsolePECIcons._instance.nonfirmato());
			iconaFirma.setStyleName("ico-mail");
			iconaFirma.getElement().getStyle().setVisibility(Visibility.HIDDEN);
			spanAllegato.add(iconaFirma);
			
		}
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
	

	public void setMostraAllegatoCommand(Command<Void, AllegatoDTO> mostraAllegatoCommand) {
		this.mostraAllegatoCommand = mostraAllegatoCommand;
	}

	public void setDownloadAllegatoCommand(Command<Void, AllegatoDTO> downloadAllegatoCommand) {
		this.downloadAllegatoCommand = downloadAllegatoCommand;
	}

	public void setCapofila(boolean capofila) {
		this.capofila = capofila;
	}

	public void setCheckBoxEnabled(boolean enabled) {
		if (checkDisable)
			checkBox.setEnabled(false);
		else
			checkBox.setEnabled(enabled);
	}
	
	public void setCheckBoxVisible(boolean visible) {
		checkBox.setVisible(visible);
	}

	public void setMostraInformazioniDiProtocollazione(boolean mostraInformazioniDiProtocollazione) {
		this.mostraInformazioniDiProtocollazione = mostraInformazioniDiProtocollazione;
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

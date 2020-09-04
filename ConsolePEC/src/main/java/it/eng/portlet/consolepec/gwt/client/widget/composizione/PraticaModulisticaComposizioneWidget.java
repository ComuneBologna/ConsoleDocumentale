package it.eng.portlet.consolepec.gwt.client.widget.composizione;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
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
public class PraticaModulisticaComposizioneWidget extends ComplexPanel {

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
	private Command<Void, MostraPraticaModulistica> mostraDettaglioPraticaModulistica;
	private Command<Void, SelezionePraticaModulistica> selezionaPraticaModulisticaCommand;
	private Command<Void, AllegatoDTO> mostraAllegatoCommand;
	private Command<Void, AllegatoDTO> downloadAllegatoCommand;
	private boolean capofila = false;
	
	private boolean mostraInformazioniDiProtocollazione = false;
	
	private SlideAnimation slideAnimation;
	
	public PraticaModulisticaComposizioneWidget() {
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

	public void mostraDettaglio(final PraticaModulisticaDTO praticaModulistica) {

		/* Checkbox di selezione */
		CheckBox checkBox = new CheckBox();
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
		add(checkBox, documento);
		
		if(mostraInformazioniDiProtocollazione) {
			if(capofila){
				SpanElement capofilaSpan = Document.get().createSpanElement();
				capofilaSpan.setInnerHTML(ConsolePecConstants.CAPOFILA_LABEL);
				capofilaSpan.setClassName("capofila");
				documento.appendChild(capofilaSpan);
			}
			
			SpanElement protocollo = Document.get().createSpanElement();
			if(praticaModulistica.getNumeroPG() != null){
				protocollo.setInnerText(praticaModulistica.getNumeroPG() + "/" + praticaModulistica.getAnnoPG());	
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
		data.setInnerHTML(praticaModulistica.getDataOraCreazione());
		data.setClassName("data");
		documento.appendChild(data);
		
		
		SpanElement dettaglioSpan = Document.get().createSpanElement();
		dettaglioSpan.setClassName("documento");
		documento.appendChild(dettaglioSpan);
		
		
		
		/* Icona e link */
		String titolo = praticaModulistica.getTitolo();
		if (titolo == null || titolo.trim().length() == 0)
			titolo = "Nessun oggetto";
		StringBuilder sb = new StringBuilder(titolo);

		Image iconaEmail = new Image(ConsolePECIcons._instance.praticamodulistica());
		iconaEmail.setTitle("Pratica Modulistica");
		
		AnchorElement iconaAnchor = Document.get().createAnchorElement();
		iconaAnchor.setClassName("ico verifica");
		add(iconaEmail, iconaAnchor);
		dettaglioSpan.appendChild(iconaAnchor);
		
		
		
		// dettaglio
		Anchor linkDettaglio = new Anchor();
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
			add(linkDettaglio, dettaglioSpan);
		} else {
			SpanElement labelDettaglio = Document.get().createSpanElement();
			labelDettaglio.setInnerText(sb.toString());
			getElement().appendChild(dettaglioSpan);
		}
		// linkDettaglio.setTargetHistoryToken(NameTokens.dettagliopecoutbozza);
		iconaEmail.setStyleName("ico-mail");

		
		
		
		Button vediAllegati = new Button(ConsolePecConstants.VEDI_ALLEGATI_COMPOSIZIONE_FASCICOLO);
		vediAllegati.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				slideAnimation.run(200);
				//allegati.setVisible(!allegati.isVisible());
			}
		});
		vediAllegati.setStyleName("btn black");
		if(praticaModulistica.getAllegati().isEmpty()){
			vediAllegati.setEnabled(false);
		}
		add(vediAllegati, azioni);
		
		
		
		if(!praticaModulistica.getAllegati().isEmpty()){
			for(final AllegatoDTO allegato :  praticaModulistica.getAllegati()){
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
	
	public void setMostraAllegatoCommand(Command<Void, AllegatoDTO> mostraAllegatoCommand) {
		this.mostraAllegatoCommand = mostraAllegatoCommand;
	}

	public void setDownloadAllegatoCommand(Command<Void, AllegatoDTO> downloadAllegatoCommand) {
		this.downloadAllegatoCommand = downloadAllegatoCommand;
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
	
	public void setCapofila(boolean capofila) {
		this.capofila = capofila;
	}
	public void setMostraInformazioniDiProtocollazione(boolean mostraInformazioniDiProtocollazione) {
		this.mostraInformazioniDiProtocollazione = mostraInformazioniDiProtocollazione;
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

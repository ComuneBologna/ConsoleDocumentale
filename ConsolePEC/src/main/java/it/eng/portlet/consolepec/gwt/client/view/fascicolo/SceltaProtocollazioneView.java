package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmailOutLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaModulisticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.SceltaProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElencoVisitorAdapter;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class SceltaProtocollazioneView extends ViewImpl implements SceltaProtocollazionePresenter.MyView {

	private final Widget widget;
	
	@UiField(provided = true)
	MessageAlertWidget messageAlertWidget;
	
	@UiField
	Button confermaButton;
	@UiField
	Button annullaButton;
	
	@UiField
	HTMLPanel protocollazioni;
	

	private PecInPraticheDB pecInDb;
	private EventBus eventBus;
	
	private List<ElementoGruppoProtocollato> protocollazioniSelezionate = new ArrayList<ElementoGruppoProtocollato>();
	
	public interface Binder extends UiBinder<Widget, SceltaProtocollazioneView> {
		//
	}

	@Inject
	public SceltaProtocollazioneView(final Binder binder, final EventBus eventBus, final PecInPraticheDB pecInDb) {
		messageAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
		this.pecInDb = pecInDb;
		this.eventBus = eventBus;
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setAnnullaCommand(final Command annullaCommand) {
		this.annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				annullaCommand.execute();
			}
		});
	}

	@Override
	public void setConfermaCommand(final Command confermaCommand) {
		this.confermaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				confermaCommand.execute();
			}
		});
	}

	@Override
	public void mostraProtocollazioni(FascicoloDTO fascicolo) {
		
		protocollazioniSelezionate.clear();
		
		Window.scrollTo(0, 0);
		confermaButton.setEnabled(false);
		protocollazioni.clear();
		Element elem = protocollazioni.getElement();
		while (elem.hasChildNodes()) {
			elem.removeChild(elem.getFirstChild());
		}
		
		for(ElementoElenco elemento: fascicolo.getElenco()){
			
			elemento.accept(new ElementoElencoVisitorAdapter(){
				
				@Override
				public void visit(ElementoGruppoProtocollatoCapofila capofila) {
					
					/* creo li elemento capofila */
					UListElement ulCapofila = UListElement.as(DOM.createElement("ul"));
					ulCapofila.setClassName("contenitore-lista-gruppi");
					protocollazioni.getElement().appendChild(ulCapofila);

					LIElement curLi = LIElement.as(DOM.createElement("li"));
					curLi.setClassName("gruppo clearfix");
					ulCapofila.appendChild(curLi);
					
					final CheckBox checkBox = new CheckBox();
					checkBox.setStyleName("checkbox-nonprot");
					addClickHandler(checkBox, capofila);
					protocollazioni.add(checkBox, curLi);
					
					SpanElement capofilaSpan = Document.get().createSpanElement();
					capofilaSpan.setInnerHTML(ConsolePecConstants.CAPOFILA_LABEL);
					capofilaSpan.setClassName("capofila");
					curLi.appendChild(capofilaSpan);
					
					SpanElement protocollo = Document.get().createSpanElement();
					protocollo.setInnerText(capofila.getNumeroPG() + "/" + capofila.getAnnoPG());	
					protocollo.setClassName("protocollo");
					curLi.appendChild(protocollo);
					
					
					
					/* div corpo */
					HTMLPanel corpoDIV = new HTMLPanel(""); // .as(DOM.createElement("div"));
					corpoDIV.setStyleName("corpo");
					protocollazioni.add(corpoDIV, curLi);// curLi.appendChild(corpoDIV);
					/*
					 * creo div box e documenti, nel caso il gruppo abbia dei files e / o email
					 */
					HTMLPanel boxDIV = new HTMLPanel("");// .as(DOM.createElement("div"));
					boxDIV.setStyleName("last box-mail");
					corpoDIV.add(boxDIV);
					
					/*
					 * creo div subProt, nel caso in cui il gruppo abbia elementi subprot
					 */
					final UListElement ulSubProt = UListElement.as(DOM.createElement("ul"));
					ulSubProt.setClassName("lista-gruppi");
					corpoDIV.getElement().appendChild(ulSubProt);

					DisegnaVisitor visitor = new DisegnaVisitor(boxDIV);
					capofila.accept(visitor); // per ba01
					
					for(ElementoElenco ape: capofila.getElementi() ){
						ape.accept(visitor);
					}
					
					
					for (ElementoElenco elem : capofila.getElementi()) {
						elem.accept( new ElementoElencoVisitorAdapter(){
							 
							@Override
							public void visit(ElementoGruppoProtocollato subProt) {
								
								LIElement curLi = LIElement.as(DOM.createElement("li"));
								curLi.setClassName("gruppo clearfix");
								ulSubProt.appendChild(curLi);
								
								final CheckBox checkBox = new CheckBox();
								checkBox.setStyleName("checkbox-nonprot");
								addClickHandler(checkBox, subProt);
								protocollazioni.add(checkBox, curLi);
								
								SpanElement protocollo = Document.get().createSpanElement();
								protocollo.setInnerText(subProt.getNumeroPG() + "/" + subProt.getAnnoPG());	
								protocollo.setClassName("protocollo");
								curLi.appendChild(protocollo);
								
								/* div corpo */
								HTMLPanel corpoDIV = new HTMLPanel("");// DivElement.as(DOM.createElement("div"));
								corpoDIV.setStylePrimaryName("corpo");
								protocollazioni.add(corpoDIV, curLi);

								HTMLPanel boxDIV = new HTMLPanel("");
								boxDIV.setStyleName("last box-mail");
								corpoDIV.add(boxDIV);
								
								DisegnaVisitor visitor = new DisegnaVisitor(boxDIV);
								for(ElementoElenco ape: subProt.getElementi() ){
									ape.accept(visitor);
								}
								
							}
							
						} );
					
					}
					
				}
				
				
			});
		}
	}
	
	private void addClickHandler(final CheckBox checkBox, final ElementoGruppoProtocollato elementoGruppoProtocollato){
		checkBox.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(checkBox.getValue()){
					protocollazioniSelezionate.add(elementoGruppoProtocollato);
				} else {
					protocollazioniSelezionate.remove(elementoGruppoProtocollato);
				}
				
				// controllo le protocollazioni
				if(protocollazioniSelezionate.isEmpty()){
					confermaButton.setEnabled(false );
				} else {
					confermaButton.setEnabled(true);
				}
				
				
				
			}
		});
	}
		
	private class DisegnaVisitor extends ElementoElencoVisitorAdapter{
		
		private HTMLPanel panel;
		
		public DisegnaVisitor(HTMLPanel panel) {
			super();
			this.panel = panel;
		}
		
		
		private void creaElementoGrafico(ImageResource icona, String testo, boolean iconaPiccola){
			HTMLPanel p = new HTMLPanel("");
			p.setStyleName("documento-mail documento");
			
			Image image = new Image(icona);
			if(iconaPiccola){
				image.setStyleName("icona-semplice"); // logica presa dalla composizione fascicolo
			}
			p.add(image);
			
			InlineLabel label = new InlineLabel(testo);
			label.setStyleName("testo-semplice");
			p.add(label);
			
			panel.add(p);
		}


		@Override
		public void visit(ElementoAllegato allegato) {
			creaElementoGrafico(allegato.getIconaStato(ConsolePECIcons._instance), allegato.getNome(), true);
		}
		
		
		@Override
		public void visit(ElementoPECRiferimento pec) {
			if (pec.getTipo().equals(TipoRiferimentoPEC.IN) || pec.getTipo().equals(TipoRiferimentoPEC.EPROTO)) {
				pecInDb.getPecInByPath(pec.getRiferimento(), false, new PraticaEmaiInlLoaded() {

					@Override
					public void onPraticaLoaded(final PecInDTO pec) {

						creaElementoGrafico(ConsolePECIcons._instance.bustinaChiusaEmail(), ConsolePecUtils.getDescrizioneEmail(pec), false);
					}

					@Override
					public void onPraticaError(String error) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						eventBus.fireEvent(event);
					}
				});
			} else {
				pecInDb.getPecOutByPath(pec.getRiferimento(), false, new PraticaEmailOutLoaded() {

					@Override
					public void onPraticaLoaded(final PecOutDTO pecout) {
						ImageResource icona;
						if (pecout.isReinoltro()) {
							icona = ConsolePECIcons._instance.reinoltro();
						} else {
							icona = ConsolePECIcons._instance.bustinaApertaEmail();
						}
						
						creaElementoGrafico(icona, ConsolePecUtils.getDescrizioneEmail(pecout), false);
					}

					@Override
					public void onPraticaError(String error) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						eventBus.fireEvent(event);
					}
				});
			}
			
		}
		
		@Override
		public void visit(ElementoPraticaModulisticaRiferimento pm) {
			
			pecInDb.getPraticaModulisticaByPath(pm.getRiferimento(), false, new PraticaModulisticaLoaded() {
				
				@Override
				public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica) {
					creaElementoGrafico(ConsolePECIcons._instance.praticamodulistica(), ConsolePecUtils.getDescrizionePraticaModulistica(pratica), false);
				}
				
				@Override
				public void onPraticaModulisticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				
				}
			});
		}
		
		@Override
		public void visit(ElementoGruppoProtocollatoCapofila capofila) {
			// ba01
			if(capofila.getOggetto() != null){
				creaElementoGrafico(ConsolePECIcons._instance.fascicoloBa01(), ConsolePecUtils.getDescrizioneBA01(capofila.getOggetto()), false);
			}
			
		}
		
	}

	@Override
	public List<ElementoGruppoProtocollato> getProtocollazioniSelezionate() {
		return protocollazioniSelezionate;
	}
	
	
	
	
			

}

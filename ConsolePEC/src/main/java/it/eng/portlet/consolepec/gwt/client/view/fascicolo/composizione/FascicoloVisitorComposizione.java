package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmailOutLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaModulisticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.util.ComposizioneFascicoloComparator;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.DettaglioFascicoloGenericoView;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.AllegatoComposizioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.AllegatoComposizioneWidget.SelezioneAllegato;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PecComposizioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PecComposizioneWidget.MostraPEC;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PecComposizioneWidget.SelezionePEC;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PraticaModulisticaComposizioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PraticaModulisticaComposizioneWidget.MostraPraticaModulistica;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.PraticaModulisticaComposizioneWidget.SelezionePraticaModulistica;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.ProtocollazioneBA01ComposizioneWidget;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CapofilaFromBA01DTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoComunicazioneRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppo;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGrupppoNonProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.web.bindery.event.shared.EventBus;

public class FascicoloVisitorComposizione extends Composizione {

		
		
		private ElementoGruppo currentGruppo;
		private UListElement ulSubProt = null;
		private UListElement ulCapofila = null;
		private LIElement curLi = null;
		private HTMLPanel boxDIV = null;
		private HTMLPanel documentiDIV = null;


		private ComposizioneFascicoloComparator composizioneFascicoloComparator = new ComposizioneFascicoloComparator();

		public FascicoloVisitorComposizione(DettaglioFascicoloGenericoView dettaglioFascicoloGenericoViw, PecInPraticheDB pecInDb, SitemapMenu sitemapMenu, EventBus eventBus) {
			super(dettaglioFascicoloGenericoViw, pecInDb, sitemapMenu, eventBus);
		}

		@Override
		public String getTitolo() {
			return "P.G.";
		}
		
		@Override
		public void render() {
			this.dettaglioFascicoloGenericoViw.getEmailConAllegati().clear();
			if (pratica.getElenco().size() > 0) {
				ulCapofila = UListElement.as(DOM.createElement("ul"));
				ulCapofila.setClassName("contenitore-lista-gruppi");
				panel.getElement().appendChild(ulCapofila);

				for (ElementoElenco elem : elementiOrdinati(pratica.getElenco()))
					elem.accept(this);
				if (curLi != null) {
					curLi.setClassName("gruppo last clearfix");
				}
			}
		}

		@Override
		public void visit(ElementoGruppo grp) {
			currentGruppo = grp;
			for (ElementoElenco elem : grp.getElementi())
				elem.accept(this);
		}
		
		@Override
		public void visit(ElementoGrupppoNonProtocollato nonProt) {
			currentGruppo = nonProt;

			/* creo li elemento nonprot */
			curLi = LIElement.as(DOM.createElement("li"));
			ulCapofila.appendChild(curLi);
			curLi.setClassName("gruppo clearfix");
			
			documentiDIV = new HTMLPanel("");// DivElement.as(DOM.createElement("div"));
			documentiDIV.setStyleName("documenti-mail");

			ulCapofila.appendChild(curLi);

			SpanElement protocollo = Document.get().createSpanElement();
			protocollo.setInnerText(ConsolePecConstants.NON_PROTOCOLLATO);
			protocollo.setClassName("protocollo");
			curLi.appendChild(protocollo);
			
			HTMLPanel corpoDIV = new HTMLPanel(""); // .as(DOM.createElement("div"));
			corpoDIV.setStyleName("corpo");
			panel.add(corpoDIV, curLi);// curLi.add(corpoDIV);
		
			boxDIV = new HTMLPanel(""); 
			boxDIV.setStyleName("box-mail last");
			corpoDIV.add(boxDIV);

			for (ElementoElenco elem : elementiOrdinati(nonProt.getElementi())){
				
			
				elem.accept(this);
			}
				

		}

		@Override
		public void visit(ElementoGruppoProtocollato subProt) {
			curLi = LIElement.as(DOM.createElement("li"));
			curLi.setClassName("gruppo clearfix");
			ulSubProt.appendChild(curLi);
			currentGruppo = subProt;
			
			SpanElement protocollo = Document.get().createSpanElement();
			protocollo.setInnerText(subProt.getNumeroPG() + "/" + subProt.getAnnoPG());	
			protocollo.setClassName("protocollo");
			curLi.appendChild(protocollo);
			
			/* div corpo */
			HTMLPanel corpoDIV = new HTMLPanel("");// DivElement.as(DOM.createElement("div"));
			corpoDIV.setStylePrimaryName("corpo");
			panel.add(corpoDIV, curLi);

			boxDIV = new HTMLPanel("");
			boxDIV.setStyleName("box-mail last");
			corpoDIV.add(boxDIV);

			for (ElementoElenco elem : elementiOrdinati(subProt.getElementi())){
								
				elem.accept(this);
			}


		}

		/*
		 * vecchio ordinamento
		 * 
		 * 
		 * private Set<ElementoElenco> sortEmailTesta(Collection<ElementoElenco> insieme) { Set<ElementoElenco> res = new LinkedHashSet<FascicoloDTO.ElementoElenco>(); for (ElementoElenco elem :
		 * insieme) if (elem instanceof ElementoPECRiferimento) res.add(elem); for (ElementoElenco elem : insieme) if (!res.contains(elem)) res.add(elem);
		 * 
		 * return res; }
		 * 
		 * private Set<ElementoElenco> sortNonProtTesta(Collection<ElementoElenco> insieme) { Set<ElementoElenco> res = new LinkedHashSet<FascicoloDTO.ElementoElenco>(); for (ElementoElenco elem :
		 * insieme) if (elem instanceof ElementoGrupppoNonProtocollato) res.add(elem); for (ElementoElenco elem : insieme) if (!res.contains(elem)) res.add(elem);
		 * 
		 * return res; }
		 */

		private List<? extends ElementoElenco> elementiOrdinati(Collection<? extends ElementoElenco> c) {
			List<? extends ElementoElenco> elementi = new ArrayList<ElementoElenco>(c);

			Collections.sort(elementi, composizioneFascicoloComparator);

			return elementi;
		}

		@Override
		public void visit(ElementoGruppoProtocollatoCapofila capofila) {
			currentGruppo = capofila;
			/* creo li elemento capofila */
			curLi = LIElement.as(DOM.createElement("li"));
			ulCapofila.appendChild(curLi);
			curLi.setClassName("gruppo clearfix");
			
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
			panel.add(corpoDIV, curLi);// curLi.appendChild(corpoDIV);
			/*
			 * creo div box e documenti, nel caso il gruppo abbia dei files e / o email
			 */
			boxDIV = new HTMLPanel("");// .as(DOM.createElement("div"));
			boxDIV.setStyleName("box-mail last");
			corpoDIV.add(boxDIV);

			/*
			 * creo div subProt, nel caso in cui il gruppo abbia elementi subprot
			 */
			ulSubProt = UListElement.as(DOM.createElement("ul"));
			ulSubProt.setClassName("lista-gruppi");
			corpoDIV.getElement().appendChild(ulSubProt);

			LIElement curLiCapofila = curLi;
			HTMLPanel boxDIVCapofila = boxDIV;
			curLi = null;

			for (ElementoElenco elem : elementiOrdinati(capofila.getElementi())) {
				elem.accept(this);
				boxDIV = boxDIVCapofila;
			}

			if (capofila.getOggetto() != null) {
				//ElementoProtocollazioneBA01ElencoWidget elemento = new ElementoProtocollazioneBA01ElencoWidget();
				//elemento.mostraCapofila(new CapofilaFromBA01DTO(capofila.getOggetto(), capofila.getNumeroPG(), capofila.getAnnoPG(), false, capofila.getDataProtocollazione()));
				//elemento.setCheckBoxVisibility(true);
				ProtocollazioneBA01ComposizioneWidget elemento = new ProtocollazioneBA01ComposizioneWidget();
				elemento.mostraDettaglio(new CapofilaFromBA01DTO(capofila.getOggetto(), capofila.getNumeroPG(), capofila.getAnnoPG(), false, capofila.getDataProtocollazione()));
				boxDIV.add(elemento);
			}

			if (curLi != null) {// esiste almeno un subprot
				curLi.setClassName("gruppo last clearfix");
			}

			curLi = curLiCapofila;
		}

		@Override
		public void visit(final ElementoAllegato allegato) {
			
			AllegatoComposizioneWidget elementoAllegatoComposizioneWidget = new AllegatoComposizioneWidget();
			
			allegato.setProtocollato(!(currentGruppo instanceof ElementoGrupppoNonProtocollato));
			if(allegato.isProtocollato()){
				ElementoGruppoProtocollato gp = ((ElementoGruppoProtocollato) currentGruppo);
				elementoAllegatoComposizioneWidget.setNumPGAnnoPG(gp.getNumeroPG(), gp.getAnnoPG());
			}
			
			elementoAllegatoComposizioneWidget.setSelezioneCommand(new Command<Void, SelezioneAllegato>(){

				@Override
				public Void exe(SelezioneAllegato t) {
					if (t.isChecked()) {
						if (allegato.isProtocollato())
							dettaglioFascicoloGenericoViw.getAllegatiProtSelezionati().add(allegato);
						else
							dettaglioFascicoloGenericoViw.getAllegatiNonProtSelezionati().add(allegato);
					} else {
						if (allegato.isProtocollato())
							dettaglioFascicoloGenericoViw.getAllegatiProtSelezionati().remove(allegato);
						else
							dettaglioFascicoloGenericoViw.getAllegatiNonProtSelezionati().remove(allegato);
					}

					dettaglioFascicoloGenericoViw.impostaAbilitazioniPulsantiera();
					return null;
				}
				
			});
			
			elementoAllegatoComposizioneWidget.setMostraCommand(new Command<Void, AllegatoDTO>(){

				@Override
				public Void exe(AllegatoDTO t) {
					mostraDettaglioAllegatoCommand.exe(allegato.getClientID(), allegato);
					return null;
				}
				
			});
			elementoAllegatoComposizioneWidget.setDownloadCommand(new Command<Void, AllegatoDTO>(){

				@Override
				public Void exe(AllegatoDTO t) {
					downloadAllegatoCommand.exe(allegato);
					return null;
				}
				
			});
			elementoAllegatoComposizioneWidget.setCapofila(currentGruppo instanceof ElementoGruppoProtocollatoCapofila);
			
			
			boxDIV.add(elementoAllegatoComposizioneWidget);
			elementoAllegatoComposizioneWidget.mostraDettaglio(allegato);
						
			

		}

		@Override
		public void visit(final ElementoPECRiferimento pec) {
			final PecComposizioneWidget widget = new PecComposizioneWidget();
			widget.setSelezionaPECCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, PecComposizioneWidget.SelezionePEC>() {
				ElementoGruppo _currentGruppo = currentGruppo;

				@Override
				public Void exe(SelezionePEC t) {

					if (t.isChecked() && _currentGruppo instanceof ElementoGrupppoNonProtocollato)
						dettaglioFascicoloGenericoViw.getPecNonProtSelezionate().add(pec);
					else
						dettaglioFascicoloGenericoViw.getPecNonProtSelezionate().remove(pec);

					if (t.isChecked() && !(_currentGruppo instanceof ElementoGrupppoNonProtocollato))
						dettaglioFascicoloGenericoViw.getPecProtSelezionate().add(pec);
					else
						dettaglioFascicoloGenericoViw.getPecProtSelezionate().remove(pec);

					dettaglioFascicoloGenericoViw.impostaAbilitazioniPulsantiera();

					return null;
				}
			});
			widget.setCheckBoxEnabled(true); // sempre abilitato per gestire anche la chiusura dei procedimenti

			widget.setMostraDettaglioPEC(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, PecComposizioneWidget.MostraPEC>() {

				@Override
				public Void exe(MostraPEC t) {
					if (t.getTipo().equals(TipoRiferimentoPEC.IN) || t.getTipo().equals(TipoRiferimentoPEC.EPROTO))
						mostraDettaglioEmailCommand.exe(t.getClientID());
					else
						mostraDettaglioBozzaCommand.exe(t.getClientID());
					return null;
				}
			});
			
			widget.setMostraAllegatoCommand(new Command<Void, AllegatoDTO>(){

				@Override
				public Void exe(AllegatoDTO allegato) {
					mostraDettaglioAllegatoCommand.exe(pec.getRiferimento(), allegato);
					return null;
				}
				
			});
			widget.setDownloadAllegatoCommand(new Command<Void, AllegatoDTO>(){

				@Override
				public Void exe(AllegatoDTO allegato) {
					downloadAllegatoCommand.exe(allegato);
					return null;
				}
				
			});
			widget.setCapofila(currentGruppo instanceof ElementoGruppoProtocollatoCapofila);
			
			
			boxDIV.add(widget);
			if (pec.getTipo().equals(TipoRiferimentoPEC.IN) || pec.getTipo().equals(TipoRiferimentoPEC.EPROTO)) {
				pecInDb.getPecInByPath(pec.getRiferimento(), sitemapMenu.containsLink(pec.getRiferimento()), new PraticaEmaiInlLoaded() {

					@Override
					public void onPraticaLoaded(final PecInDTO pec) {

						widget.mostraDettaglio(pec);
						if (pec.getAllegati().size() > 0)
							dettaglioFascicoloGenericoViw.getEmailConAllegati().add(pec);
						if (dettaglioFascicoloGenericoViw.getEmailConAllegati().size() > 0 && pratica.isCaricaAllegato())
							dettaglioFascicoloGenericoViw.showHideImportaAllegatiMenuItem(true); // almeno un'email con allegato e la pratica non deve essere archiviata 
							//showHideMenuItem(newImportaAllegati, true);
						else
							dettaglioFascicoloGenericoViw.showHideImportaAllegatiMenuItem(false);
							//showHideMenuItem(newImportaAllegati, false);
						
					}

					@Override
					public void onPraticaError(String error) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						eventBus.fireEvent(event);
					}
				});
			} else {
				pecInDb.getPecOutByPath(pec.getRiferimento(), sitemapMenu.containsLink(pec.getRiferimento()), new PraticaEmailOutLoaded() {

					@Override
					public void onPraticaLoaded(final PecOutDTO pecout) {
						if (pecout.getStato().equals(StatoDTO.BOZZA)) {
							widget.setCheckBoxEnabled(false);
						} else if (pecout.getStato().equals(StatoDTO.PARZIALMENTECONSEGNATA) || pecout.getStato().equals(StatoDTO.CONSEGNATA))
							dettaglioFascicoloGenericoViw.getPecOutConRicevute().add(pec);
						widget.mostraDettaglio(pecout);
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
		public void visit(final ElementoPraticaModulisticaRiferimento pm) {
			final PraticaModulisticaComposizioneWidget widget = new PraticaModulisticaComposizioneWidget();
			widget.setSelezionaPraticaModulisticaCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, PraticaModulisticaComposizioneWidget.SelezionePraticaModulistica>() {
				ElementoGruppo _currentGruppo = currentGruppo;

				@Override
				public Void exe(SelezionePraticaModulistica t) {

					if (t.isChecked() && _currentGruppo instanceof ElementoGrupppoNonProtocollato)
						dettaglioFascicoloGenericoViw.getModulisticheNonProtSelezionate().add(pm);
					else
						dettaglioFascicoloGenericoViw.getModulisticheNonProtSelezionate().remove(pm);

					if (t.isChecked() && !(_currentGruppo instanceof ElementoGrupppoNonProtocollato))
						dettaglioFascicoloGenericoViw.getModulisticheProtSelezionate().add(pm);
					else
						dettaglioFascicoloGenericoViw.getModulisticheProtSelezionate().remove(pm);

					dettaglioFascicoloGenericoViw.impostaAbilitazioniPulsantiera();

					return null;
				}
			});
			widget.setCheckBoxEnabled(true);

			widget.setMostraDettaglioPraticaModulistica(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, PraticaModulisticaComposizioneWidget.MostraPraticaModulistica>() {

				@Override
				public Void exe(MostraPraticaModulistica t) {
					goToDettaglioModuloCommand.execute();
					return null;
				}
			});
			widget.setMostraAllegatoCommand(new Command<Void, AllegatoDTO>(){

				@Override
				public Void exe(AllegatoDTO allegato) {
					mostraDettaglioAllegatoCommand.exe(pm.getRiferimento(),  allegato);
					return null;
				}
				
			});
			widget.setDownloadAllegatoCommand(new Command<Void, AllegatoDTO>(){

				@Override
				public Void exe(AllegatoDTO allegato) {
					downloadAllegatoCommand.exe(allegato);
					return null;
				}
				
			});
			widget.setCapofila(currentGruppo instanceof ElementoGruppoProtocollatoCapofila);
			
			boxDIV.add(widget);

			pecInDb.getPraticaModulisticaByPath(pm.getRiferimento(), sitemapMenu.containsLink(pm.getRiferimento()), new PraticaModulisticaLoaded() {

				@Override
				public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica) {
					goToDettaglioModuloCommand.setPraticaModulisticaPath(pratica.getClientID());
					widget.mostraDettaglio(pratica);
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
		public void visit(ElementoComunicazioneRiferimento cr) {
			/*final ElementoComunicazioneElencoWidget widget = new ElementoComunicazioneElencoWidget();
			widget.setSelezionaComunicazioneCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, ElementoComunicazioneElencoWidget.SelezioneComunicazione>() {
				
				@Override
				public Void exe(SelezioneComunicazione t) {

					impostaAbilitazioniPulsantiera();

					return null;
				}
			});
			widget.setCheckBoxVisible(true);
			widget.setCheckBoxEnabled(true);

			widget.setMostraDettaglioComunicazione(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, ElementoComunicazioneElencoWidget.MostraComunicazione>() {

				@Override
				public Void exe(MostraComunicazione t) {
					mostraDettaglioComunicazioneCommand.execute();
					return null;
				}
			});
			boxDIV.add(widget);

			pecInDb.getComunicazioneByPath(cr.getRiferimento(), sitemapMenu.containsLink(cr.getRiferimento()), new PraticaComunicazioneLoaded() {
				
				@Override
				public void onPraticaLoaded(ComunicazioneDTO comunicazione) {
					mostraDettaglioComunicazioneCommand.setComunicazionePath(pratica.getClientID());
					widget.mostraDettaglio(comunicazione);
				}

				@Override
				public void onPraticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}
				
			});
				
				*/
		}
	}
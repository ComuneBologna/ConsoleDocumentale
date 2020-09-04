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
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoComunicazioneRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElencoVisitor;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppo;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGrupppoNonProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.PecDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

public class ConversazioneComposizione extends Composizione {

		private UListElement ulCorrente = null;
		private ElementoGruppo gruppoCorrente;
		
		private HTMLPanel pannelloRoot = null;
		
		private Map<ElementoElenco, Widget> widgetsCalcolati = new HashMap<FascicoloDTO.ElementoElenco, Widget>();
		private ConversazioneEntry conversazioniRoot = new ConversazioneEntry(null);
		
		public ConversazioneComposizione(DettaglioFascicoloGenericoView dettaglioFascicoloGenericoViw, PecInPraticheDB pecInDb, SitemapMenu sitemapMenu, EventBus eventBus) {
			super(dettaglioFascicoloGenericoViw, pecInDb, sitemapMenu, eventBus);
		}

		@Override
		public String getTitolo() {
			return "Conversazione";
		}
		
		@Override
		public void init(HTMLPanel panel, FascicoloDTO pratica, Button button) {
			super.init(panel, pratica, button);
			
		}
		
		@Override
		public void render() {
			this.dettaglioFascicoloGenericoViw.getEmailConAllegati().clear();
			this.widgetsCalcolati.clear();
			conversazioniRoot = new ConversazioneEntry(null);
			if (pratica.getElenco().size() > 0) {
				
				ulCorrente = UListElement.as(DOM.createElement("ul"));
				ulCorrente.setClassName("contenitore-lista-gruppi");
				panel.getElement().appendChild(ulCorrente);

				
				
				/*
				 * sono necessarie 3 fasi perchè le chiamate asincrone al server necessarie per recuperare i dati potrebbero sovrapporsi e i dati non verrebbero estratti correttamente:
				 * 1) Costruisco i widget(sono necessarie le informazioni sulla protocollazione) 
				 * 2) Ordino i widget in base alla conversazione
				 * 3) Disegno i widget
				 */
				
				// fase 1 
				CostruisciWidgetVisitor delegato = new CostruisciWidgetVisitor();
				for (ElementoElenco elem : pratica.getElenco())
					elem.accept(delegato);
				
				List<ElementoElenco> elementiOrdinatiPerConversazione = new ArrayList<ElementoElenco>(widgetsCalcolati.keySet());
				Collections.sort(elementiOrdinatiPerConversazione, new ComposizioneFascicoloComparator());
				
				
				// fase 2
				CreaConversazioniVisitor creaConversazioniVisitor = new CreaConversazioniVisitor(elementiOrdinatiPerConversazione.iterator());
				creaConversazioniVisitor.visitNext();
				
				
				
			}
		}

		@Override
		public void visit(ElementoGruppo grp) {}
		@Override
		public void visit(ElementoGruppoProtocollato subProt) {}
		@Override
		public void visit(ElementoGrupppoNonProtocollato nonProt) {}
		@Override
		public void visit(ElementoGruppoProtocollatoCapofila capofila) {
			
			final ProtocollazioneBA01ComposizioneWidget widget = (ProtocollazioneBA01ComposizioneWidget) widgetsCalcolati.get(capofila);
			
			widget.mostraDettaglio(new CapofilaFromBA01DTO(capofila.getOggetto(), capofila.getNumeroPG(), capofila.getAnnoPG(), false, capofila.getDataProtocollazione()));
			
			
			pannelloRoot.add(widget);
		}

		@Override
		public void visit(final ElementoAllegato allegato) {
			
			
			final AllegatoComposizioneWidget widget = (AllegatoComposizioneWidget) widgetsCalcolati.get(allegato);
			
			widget.mostraDettaglio(allegato);
			
			pannelloRoot.add(widget);
						
		}

		@Override
		public void visit(final ElementoPECRiferimento pec) {
			
			
			final PecComposizioneWidget widget = (PecComposizioneWidget) widgetsCalcolati.get(pec);
			
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
			
			
			pannelloRoot.add(widget);
			
			ConversazioneEntry entryCorrente = conversazioniRoot.successive.get(pec);
			
			if(!entryCorrente.successive.isEmpty()){
				
				
				for (ElementoElenco elem : entryCorrente.successive.keySet()) {
					HTMLPanel provvisorio = pannelloRoot;
					
					LIElement curLi = LIElement.as(DOM.createElement("li"));
					ulCorrente.appendChild(curLi);
					curLi.setClassName("gruppo clearfix");
					
					HTMLPanel documentiDIV = new HTMLPanel("");// DivElement.as(DOM.createElement("div"));
					documentiDIV.setStyleName("documenti-mail");

					HTMLPanel corpoDIV = new HTMLPanel(""); // .as(DOM.createElement("div"));
					corpoDIV.setStyleName("corpo");
					panel.add(corpoDIV, curLi);// curLi.add(corpoDIV);
				
					pannelloRoot = new HTMLPanel(""); 
					corpoDIV.add(pannelloRoot);
				
					
					UListElement ulPrecedente = ulCorrente;
					
					ulCorrente = UListElement.as(DOM.createElement("ul"));
					ulCorrente.setClassName("lista-gruppi");
					corpoDIV.getElement().appendChild(ulCorrente);
					
					ConversazioneEntry vecchiaRoot = conversazioniRoot;
					conversazioniRoot = entryCorrente;
					elem.accept(this);
					conversazioniRoot = vecchiaRoot;
					
					ulCorrente = ulPrecedente;

					
					pannelloRoot = provvisorio;
					
				}
		
				
				
				
			}
			

		}

		


		@Override
		public void visit(final ElementoPraticaModulisticaRiferimento pm) {
			
			
			final PraticaModulisticaComposizioneWidget widget = (PraticaModulisticaComposizioneWidget) widgetsCalcolati.get(pm);
			
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
			pannelloRoot.add(widget);
			
		}
		
		

		@Override
		public void visit(ElementoComunicazioneRiferimento cr) {
			
		}
		
		
		private class CostruisciWidgetVisitor implements ElementoElencoVisitor{
			
			
			@Override
			public void visit(final ElementoAllegato allegato) {

				AllegatoComposizioneWidget widget = new AllegatoComposizioneWidget();
				widget.setMostraInformazioniDiProtocollazione(true);
				
				allegato.setProtocollato(!(gruppoCorrente instanceof ElementoGrupppoNonProtocollato));
				if(allegato.isProtocollato()){
					ElementoGruppoProtocollato gp = ((ElementoGruppoProtocollato) gruppoCorrente);
					widget.setNumPGAnnoPG(gp.getNumeroPG(), gp.getAnnoPG());
					
					if(gp.getNumeroPGCapofila() != null && gp.getNumeroPGCapofila() != gp.getNumeroPG()) {
						widget.setNumPGAnnoPGCapofila(gp.getNumeroPGCapofila(), gp.getAnnoPGCapofila());
					}
				}
				
				widget.setSelezioneCommand(new Command<Void, SelezioneAllegato>(){

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
				
				widget.setMostraCommand(new Command<Void, AllegatoDTO>(){

					@Override
					public Void exe(AllegatoDTO t) {
						mostraDettaglioAllegatoCommand.exe(allegato.getClientID(), allegato);
						return null;
					}
					
				});
				widget.setDownloadCommand(new Command<Void, AllegatoDTO>(){

					@Override
					public Void exe(AllegatoDTO t) {
						downloadAllegatoCommand.exe(allegato);
						return null;
					}
					
				});
				widget.setCapofila(gruppoCorrente instanceof ElementoGruppoProtocollatoCapofila);
				widgetsCalcolati.put(allegato, widget);
			
			}

			@Override
			public void visit(final ElementoPECRiferimento pec) {
				final PecComposizioneWidget widget = new PecComposizioneWidget();
				widget.setMostraInformazioniDiProtocollazione(true);
				
				widget.setSelezionaPECCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, PecComposizioneWidget.SelezionePEC>() {
					ElementoGruppo _currentGruppo = gruppoCorrente;

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
				widget.setCapofila(gruppoCorrente instanceof ElementoGruppoProtocollatoCapofila);
				
				widgetsCalcolati.put(pec, widget);
				
				
				
				
			}

			@Override
			public void visit(ElementoGruppo grp) {
				gruppoCorrente = grp;
				for (ElementoElenco elem : grp.getElementi())
					elem.accept(this);
			}

			@Override
			public void visit(ElementoGruppoProtocollatoCapofila capofila) {
				gruppoCorrente = capofila;
				if (capofila.getOggetto() != null) {
					

					ProtocollazioneBA01ComposizioneWidget widget = new ProtocollazioneBA01ComposizioneWidget();
					widget.setMostraInformazioniDiProtocollazione(true);
					widgetsCalcolati.put(capofila, widget);
					
				}

				for (ElementoElenco elem : capofila.getElementi())
					elem.accept(this);
			}

			@Override
			public void visit(ElementoGruppoProtocollato grp) {
				gruppoCorrente = grp;
				for (ElementoElenco elem : grp.getElementi())
					elem.accept(this);
			}

			@Override
			public void visit(ElementoGrupppoNonProtocollato grp) {
				gruppoCorrente = grp;
				for (ElementoElenco elem : grp.getElementi())
					elem.accept(this);
			}

			@Override
			public void visit(final ElementoPraticaModulisticaRiferimento pm) {
				final PraticaModulisticaComposizioneWidget widget = new PraticaModulisticaComposizioneWidget();
				widget.setMostraInformazioniDiProtocollazione(true);
				
				widget.setSelezionaPraticaModulisticaCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, PraticaModulisticaComposizioneWidget.SelezionePraticaModulistica>() {
					ElementoGruppo _currentGruppo = gruppoCorrente;

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
						mostraDettaglioAllegatoCommand.exe(pm.getRiferimento(), allegato);
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
				widget.setCapofila(gruppoCorrente instanceof ElementoGruppoProtocollatoCapofila);
				
				
				widgetsCalcolati.put(pm, widget);
			}

			@Override
			public void visit(ElementoComunicazioneRiferimento elementoComunicazioneRiferimento) {}
			
		}
		
		
		private class CreaConversazioniVisitor implements ElementoElencoVisitor{
			
			private Iterator<ElementoElenco> iterator;
			
			private Map<PecDTO, ElementoPECRiferimento> stackConversazioni = new HashMap<PecDTO, ElementoPECRiferimento>();
			
			public CreaConversazioniVisitor(Iterator<ElementoElenco> iterator) {
				super();
				this.iterator = iterator;
			}

			@Override
			public void visit(ElementoAllegato allegato) {
				conversazioniRoot.successive.put(allegato, new ConversazioneEntry(allegato));
				visitNext();
			}

			@Override
			public void visit(final ElementoPECRiferimento pec) {
				
				
				if (pec.getTipo().equals(TipoRiferimentoPEC.IN) || pec.getTipo().equals(TipoRiferimentoPEC.EPROTO)) {
					pecInDb.getPecInByPath(pec.getRiferimento(), sitemapMenu.containsLink(pec.getRiferimento()), new PraticaEmaiInlLoaded() {

						@Override
						public void onPraticaLoaded(final PecInDTO pecIn) {
							
							calcolaConversazione(pec, pecIn);
							visitNext();
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
							
							calcolaConversazione(pec, pecout);
							visitNext();
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
			
			private void calcolaConversazione(ElementoPECRiferimento pec, PecDTO pecDTO){
				if(pecDTO.getReplyTo() != null){
					stackConversazioni.put(pecDTO, pec);
					conversazioniRoot.successive.put(pec, new ConversazioneEntry(pec)); // lo metto anche nelle conversazioni per gestire il caso di mail sganciata, ma non sgancio la risposta
				} else {
					processStackConversazioni(pec, pecDTO, null);
							
				}
			}
			
			
			private void processStackConversazioni(ElementoPECRiferimento pec,PecDTO pecDTO, ConversazioneEntry conversazioneEntry){
				
				if(conversazioneEntry == null){ 
					conversazioneEntry = new ConversazioneEntry(pec); // root
					conversazioniRoot.successive.put(pec, conversazioneEntry);
				}
				
				Iterator<Entry<PecDTO, ElementoPECRiferimento>> eIterator = stackConversazioni.entrySet().iterator();
				
				while(eIterator.hasNext()){
					Entry<PecDTO, ElementoPECRiferimento> entry = eIterator.next();
					
					PecDTO pecSuccessivaDTO = entry.getKey();
					ElementoPECRiferimento pecSuccesiva = entry.getValue();
					
					if(pecSuccessivaDTO.getReplyTo().equals(pecDTO.getNumeroRepertorio())){
						//eIterator.remove();
						conversazioniRoot.successive.remove(pecSuccesiva);
						
						ConversazioneEntry entryFiglia = new ConversazioneEntry(pecSuccesiva);
						
						conversazioneEntry.successive.put(pecSuccesiva, entryFiglia);
						
						processStackConversazioni(pecSuccesiva, pecSuccessivaDTO, entryFiglia);
						
					}
					
				}
				
				
				
			}

			@Override
			public void visit(ElementoGruppo gruppo) {}
			@Override
			public void visit(ElementoGruppoProtocollatoCapofila capofila) {
				conversazioniRoot.successive.put(capofila, new ConversazioneEntry(capofila));
				visitNext();
				
			}
			@Override
			public void visit(ElementoGruppoProtocollato subProt) {}
			@Override
			public void visit(ElementoGrupppoNonProtocollato nonProt) {}

			@Override
			public void visit(ElementoPraticaModulisticaRiferimento pm) {
				conversazioniRoot.successive.put(pm, new ConversazioneEntry(pm));
				visitNext();
			}

			@Override
			public void visit(ElementoComunicazioneRiferimento elementoComunicazioneRiferimento) {}
			
			
			private void visitNext(){
				if(iterator.hasNext()) {
					iterator.next().accept(this);
				} else {
					// fase 3 -> disegno la conversazione
					
					
					List<ElementoElenco> elementiConversazione = new ArrayList<ElementoElenco>(conversazioniRoot.successive.keySet()); 
					Collections.sort(elementiConversazione, new ComposizioneFascicoloComparator());
					
										
					for (ElementoElenco elem : elementiConversazione) {
						LIElement curLi = LIElement.as(DOM.createElement("li"));
						ulCorrente.appendChild(curLi);
						curLi.setClassName("gruppo clearfix");
						
						HTMLPanel documentiDIV = new HTMLPanel("");// DivElement.as(DOM.createElement("div"));
						documentiDIV.setStyleName("documenti-mail");

						HTMLPanel corpoDIV = new HTMLPanel(""); // .as(DOM.createElement("div"));
						corpoDIV.setStyleName("corpo box-mail last");
						panel.add(corpoDIV, curLi);// curLi.add(corpoDIV);
					
						pannelloRoot = new HTMLPanel(""); 
						//pannelloRoot.setStyleName("box-mail last");
						corpoDIV.add(pannelloRoot);
						
						UListElement ulPrecedente = ulCorrente;
						
						ulCorrente = UListElement.as(DOM.createElement("ul"));
						ulCorrente.setClassName("lista-gruppi");
						corpoDIV.getElement().appendChild(ulCorrente);
						
						
						elem.accept(ConversazioneComposizione.this);
						
						ulCorrente = ulPrecedente;

					}
						
				}
			}

		}
		
		private class ConversazioneEntry {
			
			ElementoElenco elemento;
			
			Map<ElementoElenco, ConversazioneEntry> successive = new LinkedHashMap<ElementoElenco, ConversazioneEntry>();

			public ConversazioneEntry(ElementoElenco elemento) {
				super();
				this.elemento = elemento;
			}

			
			@Override
			public String toString() {// a scopo di debug
				StringBuilder bl = new StringBuilder(); 
				for(Iterator<ConversazioneEntry> it = successive.values().iterator(); it.hasNext();)  bl.append(it.next().toString() + (it.hasNext() ? "," : "")); 
				return (elemento != null ? elemento : "" )+ "[" + bl.toString()   + "]";
			}
			
		}
		
		
}
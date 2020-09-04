package it.eng.portlet.consolepec.gwt.client.view.modulistica;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.modulistica.DettaglioPraticaModulisticaPresenter;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.DettaglioAllegatiWidget;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.NodoModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.NodoModulisticaDTO.TipoNodoModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.SezioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TabellaModuloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TabellaModuloDTO.RigaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ValoreModuloDTO;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class DettaglioPraticaModulisticaView extends ViewImpl implements DettaglioPraticaModulisticaPresenter.MyView {

	private final Widget widget;

	@UiField
	HTMLPanel valoriPanel;
	
	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	
	@UiField
	Button archiviaButton;
	@UiField
	Button eliminaButton;
	@UiField
	Button riportaInGestioneButton;
	@UiField
	Button chiudiButton;
	@UiField
	Button creaFascicoloButton;
	@UiField
	Button riassegnaButton;
	@UiField
	Button aggFascicoloButton;
	@UiField
	DettaglioAllegatiWidget dettaglioAllegatiWidget;
	@UiField
	DownloadAllegatoWidget downloadWidget;
	
	private final PecInPraticheDB praticheDB;
	private final SitemapMenu sitemapMenu;
	private final EventBus eventBus;

	private com.google.gwt.user.client.Command dettaglioFascicoloCommand;
	private Command chiudiDettaglioCommand;
	
	public interface Binder extends UiBinder<Widget, DettaglioPraticaModulisticaView> {
	}

	@Inject
	public DettaglioPraticaModulisticaView(final PecInPraticheDB praticheDB, final SitemapMenu sitemapMenu,  final Binder binder, final EventBus eventBus) {
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
		this.praticheDB = praticheDB;
		this.sitemapMenu = sitemapMenu;
		this.eventBus = eventBus;
		
		chiudiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				chiudiDettaglioCommand.execute();
			}
		});
		
	}

	
	@Override
	public Widget asWidget() {
		return widget;
	}


	
	@Override
	public void setChiudiDettaglioCommand(final Command chiudiDettaglioCommand) {
		this.chiudiDettaglioCommand = chiudiDettaglioCommand;
	}
	
	private class ColonnaGenerica extends TextColumn<RigaDTO>{
		private int index;
		
		
		public ColonnaGenerica(int index) {
			super();
			this.index = index;
		}

		@Override
		public String getValue(RigaDTO riga) {
			if(riga!= null && riga.getColonne() != null  && riga.getColonne().size() > index){
				return riga.getColonne().get(index).getDescrizione(); 
			}
			return "";
		}
	}

	@Override
	public void mostraModulo(PraticaModulisticaDTO pratica) {
		valoriPanel.clear();
		valoriPanel.getElement().removeAllChildren();
		
		aggiungiValoriAlPannello(pratica.getValori(), valoriPanel);
		
		
		// DATI PRATICA
		DisclosurePanel disclosurePanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Dati Modulo");
		disclosurePanel.setAnimationEnabled(true);
		disclosurePanel.setOpen(false);
		HTMLPanel pannelloDatiPratica = new HTMLPanel("");
		disclosurePanel.add(pannelloDatiPratica);
		valoriPanel.getElement().appendChild(DOM.createElement("HR"));
		valoriPanel.add(disclosurePanel);

		String idDocumentale = pratica.getNumeroRepertorio() != null && !"".equals(pratica.getNumeroRepertorio()) ? pratica.getNumeroRepertorio() : "-";
		aggiungiValoreGenerico(pannelloDatiPratica, "Id Documentale", idDocumentale);
		String assegnatario = pratica.getAssegnatario() != null && !"".equals(pratica.getAssegnatario()) ? pratica.getAssegnatario() : "-";
		aggiungiValoreGenerico(pannelloDatiPratica, "In Gestione a", assegnatario);
		
		
		
		if (pratica.getIdClientFascicolo() != null) {
			final Anchor link = new Anchor("Fascicolo collegato");
			link.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dettaglioFascicoloCommand.execute();
				}
			});
			
			HTMLPanel panel = new HTMLPanel("");
			panel.setStyleName("cell acapo");

			SpanElement label = SpanElement.as(DOM.createSpan());
			label.setClassName("label");
			label.setInnerText("Fascicolo Collegato");
			panel.getElement().appendChild(label);
			
			panel.add(link);

			valoriPanel.add(panel);
			
			
			
			praticheDB.getFascicoloByPath(pratica.getIdClientFascicolo(), sitemapMenu.containsLink(pratica.getIdClientFascicolo()), new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO fascicolo) {
					if (fascicolo != null) {
						link.setText(fascicolo.getTitolo());
					}
				}

				@Override
				public void onPraticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}
			});

		}

		aggiornaElencoAllegati(pratica);
		
		archiviaButton.setEnabled(pratica.isArchiviaAbilitato());
		riportaInGestioneButton.setEnabled(pratica.isRiportaInGestioneAbilitato());
		eliminaButton.setEnabled(pratica.isEliminaAbilitato());
		creaFascicoloButton.setEnabled(pratica.isCreaFascicoloAbilitato());
		riassegnaButton.setEnabled(pratica.isRiassegnaAbilitato());
		aggFascicoloButton.setEnabled(pratica.isAggiungiFascicoloAbilitato());
		
		Window.scrollTo(0, 0);
	}
	
	private void aggiungiValoreGenerico(HTMLPanel mainPanel, String l, String value) {
		HTMLPanel panel = new HTMLPanel("");
		panel.setStyleName("cell acapo");

		SpanElement label = SpanElement.as(DOM.createSpan());
		label.setClassName("label");
		label.setInnerText(l);
		panel.getElement().appendChild(label);

		panel.add(new Label(value));

		mainPanel.add(panel);
		
	}


	private void aggiungiValoriAlPannello(List<NodoModulisticaDTO> nodi, HTMLPanel addPanel){
		boolean first = true;
		
		for (NodoModulisticaDTO nodo : nodi) {

			if(nodo.getTipoNodo() == TipoNodoModulisticaDTO.SEZIONE){
				SezioneDTO sezione = (SezioneDTO) nodo;
				DisclosurePanel disclosurePanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), sezione.getTitolo());
				disclosurePanel.setAnimationEnabled(true);
				HTMLPanel pannelloSezioni = new HTMLPanel("");
				disclosurePanel.add(pannelloSezioni);
				aggiungiValoriAlPannello(sezione.getNodi(), pannelloSezioni);
				if(first) {
					disclosurePanel.setOpen(true);
					first = false;
				} else {
					addPanel.getElement().appendChild(DOM.createElement("HR"));
					disclosurePanel.setOpen(false);
				}
				addPanel.add(disclosurePanel);
				
			}
			if(nodo.getTipoNodo() == TipoNodoModulisticaDTO.VALORE_MODULO){
				ValoreModuloDTO v = (ValoreModuloDTO) nodo;
				if (v.isVisibile()) {
					switch (v.getTipoValoreModulo()) {
						case Tabella: {
							TabellaModuloDTO tabellaModel = v.getTabella();
							if (tabellaModel != null && tabellaModel.getRighe() != null && tabellaModel.getRighe().isEmpty() == false) {
	
								int numRighe = tabellaModel.getRighe().size() - 1;
								DataGridWidget<RigaDTO> tabella = new DataGridWidget<RigaDTO>(numRighe, null, null);
								tabella.setStyleName("tabella-modulo");
								tabella.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
								RigaDTO header = tabellaModel.getRighe().get(0);
								if (header.getColonne() != null && header.getColonne().isEmpty() == false) {
									for (int i = 0; i < header.getColonne().size(); i++) {
										if(header.getColonne().get(i).isVisibile())  {
											tabella.addColumn(new ColonnaGenerica(i), header.getColonne().get(i).getDescrizione());
										}
									}
								}
	
								List<RigaDTO> righe = tabellaModel.getRighe().size() > 1 ? tabellaModel.getRighe().subList(1, tabellaModel.getRighe().size()) : new ArrayList<RigaDTO>();
								tabella.setRowData(righe);
	
								HTMLPanel panel = new HTMLPanel("");
								panel.setStyleName("cell acapo");
	
								SpanElement label = SpanElement.as(DOM.createSpan());
								label.setClassName("label");
								label.setInnerText(v.getEtichetta());
								panel.getElement().appendChild(label);
								panel.add(tabella);
	
								addPanel.add(panel);
								    
								    
							}
	
							break;
						}
						case Valore: {
							HTMLPanel panel = new HTMLPanel("");
							panel.setStyleName("cell acapo");
	
							SpanElement label = SpanElement.as(DOM.createSpan());
							label.setClassName("label");
							label.setInnerText(v.getEtichetta());
							panel.getElement().appendChild(label);
	
							String valore = v.getDescrizione() != null && !"".equals(v.getDescrizione()) ? v.getDescrizione() : "-";
							panel.add(new Label(valore));
	
							addPanel.add(panel);
							break;
						}
					}
				}
			}
		}
	}
	
	
	
	
	private void aggiornaElencoAllegati(PraticaModulisticaDTO pm) {
		dettaglioAllegatiWidget.mostraAllegati(pm.getAllegati(), false);
	}
	@Override
	public void setEliminaCommand(final com.google.gwt.user.client.Command eliminaCommand) {

		this.eliminaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eliminaCommand.execute();
			}
		});
	}

	@Override
	public void setArchiviaCommand(final com.google.gwt.user.client.Command archiviaCommand) {
		this.archiviaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				archiviaCommand.execute();
			}
		});
	}
	@Override
	public void setRiportaInGestioneCommand(final com.google.gwt.user.client.Command command) {
		this.riportaInGestioneButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}	
	
	@Override
	public void setCreaFascicoloCommand(final com.google.gwt.user.client.Command mostraCreaFascicoloFormCommand) {
		this.creaFascicoloButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mostraCreaFascicoloFormCommand.execute();
			}
		});

	}
	
	@Override
	public void setRiassegnaCommand(final com.google.gwt.user.client.Command riassegna) {
		this.riassegnaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				riassegna.execute();
			}
		});

	}


	@Override
	public void setAggiungiPraticaAFascicoloCommand(final Command aggFascCommand) {
		this.aggFascicoloButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				aggFascCommand.execute();
			}
		});
	}
	
	@Override
	public void setDettaglioFascicoloCommand(com.google.gwt.user.client.Command dettaglioFascicoloCommand) {
		this.dettaglioFascicoloCommand = dettaglioFascicoloCommand;
		
	}
	
	@Override
	public void setDownloadAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand) {
		this.dettaglioAllegatiWidget.setDownloadAllegatoCommand(downloadAllegatoCommand);
	}
	
	@Override
	public void setMostraDettaglioAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> mostraDettaglioAllegatoCommand) {
		this.dettaglioAllegatiWidget.setMostraDettaglioAllegatoCommand(mostraDettaglioAllegatoCommand);
	}


	@Override
	public void sendDownload(SafeUri uri) {
		this.downloadWidget.sendDownload(uri);
	}

}

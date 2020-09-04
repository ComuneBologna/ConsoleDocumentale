package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione;

import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.IndietroSceltaCapofilaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.IndietroSceltaCapofilaFascicoloEvent.IndietroSceltaCapofilaFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.IStampa;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.RiversamentoCartaceoDaProtocollazioneCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.GestioneProcedimentiDaProtocollazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.MostraEsitoProtocollazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.MostraEsitoProtocollazioneEvent.MostraEsitoProtocollazioneHandler;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.GroupSuggestBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton.YesNoRadioButtonCommand;
import it.eng.portlet.consolepec.gwt.shared.dto.Element;
import it.eng.portlet.consolepec.gwt.shared.dto.Group;
import it.eng.portlet.consolepec.gwt.shared.dto.Row;
import it.eng.portlet.consolepec.gwt.shared.dto.VistorElement;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.procedimenti.OperazioniProcedimento;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.Button;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class EsitoProtocollazionePresenter extends Presenter<EsitoProtocollazionePresenter.MyView, EsitoProtocollazionePresenter.MyProxy> implements MostraEsitoProtocollazioneHandler, IndietroSceltaCapofilaFascicoloHandler,IStampa {

	public static Map<String, EtichettaPosizioneValore> elementLabel = new HashMap<String, EtichettaPosizioneValore>();
	com.google.gwt.i18n.client.DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-ddZZZ");
	com.google.gwt.i18n.client.DateTimeFormat dateFormatView = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);

	static {
		elementLabel.put("i1_data_arrivo", new EtichettaPosizioneValore("Data", 3));
		elementLabel.put("i1_codice_provenienza", new EtichettaPosizioneValore("Codice provenienza", 4));
		elementLabel.put("i1_riferimento_provenienza", new EtichettaPosizioneValore("Rif. Provenienza", 5));
		elementLabel.put("i1_provenienza", new EtichettaPosizioneValore("Provenienza", 6));
		elementLabel.put("i1_cf_provenienza", new EtichettaPosizioneValore("C.F./P.IVA Provenienza", 7));
		elementLabel.put("i1_codice_destinatario", new EtichettaPosizioneValore("Codice Destinatario", 8));
		elementLabel.put("i1_cf_destinatario", new EtichettaPosizioneValore("C.F./P.IVA Destinatario", 9));
		elementLabel.put("i1_destinatario", new EtichettaPosizioneValore("Destinatario", 10));
		elementLabel.put("i1_oggetto", new EtichettaPosizioneValore("Oggetto", 11));
		elementLabel.put("i1_tipologia_documento", new EtichettaPosizioneValore("Tipo Documento", 12));
		elementLabel.put("i1_codice_titolo", new EtichettaPosizioneValore("Titolo", 13));
		elementLabel.put("i1_codice_rubrica", new EtichettaPosizioneValore("Rubrica", 14));
		elementLabel.put("i1_codice_sezione", new EtichettaPosizioneValore("Sezione", 15));
		elementLabel.put("i1_numero_allegati", new EtichettaPosizioneValore("Numero Allegati", 16));
		elementLabel.put("i1_fascicolo_riservato", new EtichettaPosizioneValore("Fascicolo Riservato", 17));
	}

	public interface MyView extends View {

		public Button getChiudiButton();

		public void setRiepilogo(String divsHTML);

		public Button getConfermaButton();

		public YesNoRadioButton getSceltaChiusura();

		public YesNoRadioButton getSceltaAvvio();
		
		public void setRiversamentoCartaceoCommand(com.google.gwt.user.client.Command command);
		
		public DownloadAllegatoWidget getDownloadWidget();

	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<EsitoProtocollazionePresenter> {
	}

	private String clientId;
	private Map<String, Map<String, Element>> map;
	private String pg;
	private String pgCapofila;
	private String tipologiaDocumento;
	private String titolo;
	private String rubrica;
	private String sezione;
	private PecInPraticheDB praticheDb;
	private Set<String> praticheProtocollate = new HashSet<String>();
	private DispatchAsync dispatcher;
	private PlaceManager placeManager;

	@Inject
	public EsitoProtocollazionePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB praticheDb, final DispatchAsync dispatcher, final PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.praticheDb = praticheDb;
		this.dispatcher = dispatcher;
		this.placeManager = placeManager;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {

		super.onBind();

		getView().getConfermaButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				OperazioniProcedimento operazione = isCapofila() ? OperazioniProcedimento.AVVIO : OperazioniProcedimento.CHIUSURA;
				GestioneProcedimentiDaProtocollazioneEvent gestioneProcedimentiDaProtocollazioneEvent = new GestioneProcedimentiDaProtocollazioneEvent(operazione, pg.split("/")[0], pg.split("/")[1], pgCapofila.split("/")[0], pgCapofila.split("/")[1], tipologiaDocumento, titolo, rubrica,
						sezione, clientId, map);

				gestioneProcedimentiDaProtocollazioneEvent.setIdPraticheSelezionateProtocollate(praticheProtocollate);
				getEventBus().fireEvent(gestioneProcedimentiDaProtocollazioneEvent);

			}
		});

		getView().getChiudiButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				praticheDb.getFascicoloByPath(clientId, false, new PraticaFascicoloLoaded() {

					@Override
					public void onPraticaLoaded(FascicoloDTO fascicolo) {
						Place place = new Place();
						place.setToken(NameTokens.dettagliofascicolo);
						place.addParam(NameTokensParams.idPratica, clientId);
						getEventBus().fireEvent(new GoToPlaceEvent(place));
					}

					@Override
					public void onPraticaError(String error) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(error);
						getEventBus().fireEvent(event);
					}
				});
			}
		});
		
		YesNoRadioButtonCommand radioCommand = new YesNoRadioButtonCommand(){
			@Override
			public void execute(Boolean value) {
				getView().getConfermaButton().setEnabled(value);				
			}
		};
		getView().getSceltaAvvio().setYesNoRadioButtonCommand(radioCommand);
		getView().getSceltaChiusura().setYesNoRadioButtonCommand(radioCommand);
		
		getView().setRiversamentoCartaceoCommand(new RiversamentoCartaceoDaProtocollazioneCommand(this));
	}

	@Override
	protected void onHide() {
		super.onHide();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		abilitazioniPulsanti();
	}

	@Override
	@ProxyEvent
	public void onMostraEsitoProtocollazione(MostraEsitoProtocollazioneEvent event) {
		clientId = event.getClientID();
		map = event.getMapForm();
		pg = event.getPG();
		pgCapofila = event.getPgCapofila();
		praticheProtocollate = event.getPraticheProtocollate();
		revealInParent();
	}

	private void initTitolazione() {
		Map<String, Element> elementiRec1 = map.get("reci1");
		for (Element element : elementiRec1.values()) {
			Row row = (Row) element;
			if (row.getName().equalsIgnoreCase("i1_codice_titolo")) {
				titolo = row.getValue();
			} else if (row.getName().equalsIgnoreCase("i1_codice_rubrica")) {
				rubrica = row.getValue();
			} else if (row.getName().equalsIgnoreCase("i1_codice_sezione")) {
				sezione = row.getValue();
			} else if (row.getName().equalsIgnoreCase("i1_tipologia_documento")) {
				tipologiaDocumento = row.getValue();
			}
		}

	}
	
	/*
	 * Nascondo sia la richiesta di avvio che quella di chiusura dei procedimenti  e disabilito il pulsante di conferma. 
	 * Poi recupero il fascicolo per controllare le relative abilitazioni all'avvio e alla chiusura dei procedimenti 
	 * e quindi mostro il pannello con la domanda di avvio/chiusura adatto.
	 */
	private void abilitazioniPulsanti(){
		
		initTitolazione();
		
		TreeSet<EtichettaPosizioneValore> epvs = getEsitoProtocollazioneResult(map);
		StringBuffer divsHTML = new StringBuffer();
		EtichettaPosizioneValore epvPG = new EtichettaPosizioneValore("PG", 0);
		epvPG.setValore(pg);
		epvs.add(epvPG);
		if (!pgCapofila.equalsIgnoreCase("/")) {
			EtichettaPosizioneValore epvPGCapofila = new EtichettaPosizioneValore("PG Capofila", 1);
			epvPGCapofila.setValore(pgCapofila);
			epvs.add(epvPGCapofila);
		}
		for (EtichettaPosizioneValore epv : epvs) {
			divsHTML.append(epv.creaDivEtichettaValore());
		}
		getView().setRiepilogo(divsHTML.toString());
		
		
		getView().getSceltaChiusura().setVisible(false);
		getView().getSceltaAvvio().setVisible(false);
		getView().getConfermaButton().setEnabled(false);
		
		ShowAppLoadingEvent.fire(EsitoProtocollazionePresenter.this, true);
		praticheDb.getFascicoloByPath(clientId, false, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {
				ShowAppLoadingEvent.fire(EsitoProtocollazionePresenter.this, false);
				if(isCapofila()){
					if(fascicolo.isAvviaProcedimento()){
						getView().getSceltaAvvio().selectYes();
						//if(getView().getSceltaAvvio().getElement().hasAttribute("disabled"))
						//	getView().getSceltaAvvio().getElement().removeAttribute("disabled");
						getView().getSceltaAvvio().setReadOnly(false);
						getView().getConfermaButton().setEnabled(true);
					} else {
						getView().getSceltaAvvio().selectNo();
						//getView().getSceltaAvvio().getElement().setAttribute("disabled", "disabled");
						getView().getSceltaAvvio().setReadOnly(true);
						getView().getConfermaButton().setEnabled(false);
					}
					getView().getSceltaAvvio().setVisible(true);
				} else {
					if(fascicolo.isChiudiProcedimento()){
						getView().getSceltaChiusura().selectYes();
						//if(getView().getSceltaChiusura().getElement().hasAttribute("disabled"))
//							getView().getSceltaChiusura().getElement().removeAttribute("disabled");
						getView().getSceltaChiusura().setReadOnly(false);
						getView().getConfermaButton().setEnabled(true);
					} else {
						getView().getSceltaChiusura().selectNo();
						getView().getSceltaChiusura().setReadOnly(true);
						getView().getConfermaButton().setEnabled(false);
					}
					getView().getSceltaChiusura().setVisible(true);
				}
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(EsitoProtocollazionePresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(error);
				getEventBus().fireEvent(event);
			}
		});
	}
	

	private TreeSet<EtichettaPosizioneValore> getEsitoProtocollazioneResult(Map<String, Map<String, Element>> datiCampiProtocollazione) {

		final TreeSet<EtichettaPosizioneValore> etichettePosizioniValore = new TreeSet<EtichettaPosizioneValore>();

		for (String elemento : datiCampiProtocollazione.keySet()) {

			// elemento di livello 1
			Map<String, Element> internalMap = datiCampiProtocollazione.get(elemento);

			if (internalMap.size() == 0)
				continue;

			for (String elementoInterno : internalMap.keySet()) {
				Element element = internalMap.get(elementoInterno);

				element.accept(new VistorElement() {

					private static final long serialVersionUID = -5868597288660021208L;

					@Override
					public void visit(Row row) {
						String name = row.getName().equalsIgnoreCase("/") ? "" : row.getName();
						EtichettaPosizioneValore etichettaPosizioneValore = elementLabel.get(name);

						String valore = Strings.isNullOrEmpty(row.getValue()) ? null : row.getValue();

						if (name.equalsIgnoreCase("i1_codice_titolo")) {
							valore = valore + " - " + GroupSuggestBoxProtocollazione.getTitoloNameByIdDisplayName(titolo);
						} else if (name.equalsIgnoreCase("i1_codice_rubrica")) {
							valore = valore + " - " + GroupSuggestBoxProtocollazione.getRubricaNameByIdTitolo(valore, titolo);
						} else if (name.equalsIgnoreCase("i1_codice_sezione")) {
							valore = valore + " - " + GroupSuggestBoxProtocollazione.getSezioneNameByIdRubrica(valore, titolo, rubrica);
						} else if (name.equalsIgnoreCase("i1_tipologia_documento")) {
							valore = valore + " - " + GroupSuggestBoxProtocollazione.getTipoDocumentoNameByIdDisplayName(tipologiaDocumento);
						} else if (name.equalsIgnoreCase("i1_fascicolo_riservato") && !Strings.isNullOrEmpty(valore)) {
							valore = valore.equalsIgnoreCase("s") ? "Si" : "No";
						} else if (name.equalsIgnoreCase("i1_data_arrivo")) {
							Date parse = dateFormat.parse(row.getValue());
							String format = dateFormatView.format(parse);
							valore = format;
						}

						if (etichettaPosizioneValore != null && valore != null) {
							etichettaPosizioneValore.setValore(valore);
							etichettePosizioniValore.add(etichettaPosizioneValore);
						}
					}

					@Override
					public void visit(Group gruppo) {

						if (gruppo.getElements().size() > 0) {
							for (it.eng.portlet.consolepec.gwt.shared.dto.Element el : gruppo.getElements()) {
								el.accept(this);
							}
						}
					}

					@Override
					public void visit(Element e) {
					}
				});
			}

		}

		return etichettePosizioniValore;
	}

	static class EtichettaPosizioneValore implements Comparable<EtichettaPosizioneValore> {

		private String etichetta;
		private Integer posizione;
		private String valore;

		public EtichettaPosizioneValore(String etichetta, Integer posizione) {
			this.etichetta = etichetta;
			this.posizione = posizione;
		}

		public String getEtichetta() {
			return etichetta;
		}

		public void setEtichetta(String etichetta) {
			this.etichetta = etichetta;
		}

		public Integer getPosizione() {
			return posizione;
		}

		public void setPosizione(Integer posizione) {
			this.posizione = posizione;
		}

		public String getValore() {
			return valore;
		}

		public void setValore(String valore) {
			this.valore = valore;
		}

		public String creaDivEtichettaValore() {
			return " <div class='cell'><span class='label'>" + this.etichetta + ":</span>" + "<div class='abstract'><span>" + this.valore.toUpperCase() + " </span></div></div>";
		}

		@Override
		public int compareTo(EtichettaPosizioneValore o) {
			return this.getPosizione().compareTo(o.getPosizione());
		}

		@Override
		public int hashCode() {

			final int prime = 31;
			int result = 1;
			result = prime * result + ((etichetta == null) ? 0 : etichetta.hashCode());
			result = prime * result + ((posizione == null) ? 0 : posizione.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {

			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EtichettaPosizioneValore other = (EtichettaPosizioneValore) obj;
			if (etichetta == null) {
				if (other.etichetta != null)
					return false;
			} else if (!etichetta.equals(other.etichetta))
				return false;
			if (posizione == null) {
				if (other.posizione != null)
					return false;
			} else if (!posizione.equals(other.posizione))
				return false;
			return true;
		}

	}

	@Override
	public void onIndietroSceltaCapofilaFascicolo(IndietroSceltaCapofilaFascicoloEvent event) {
		revealInParent();
	}
	
	private boolean isCapofila() {
		return pg.equals(pgCapofila);
	}
	
	@Override
	public DispatchAsync getDispatchAsync() {
		return dispatcher;
	}

	@Override
	public PlaceManager getPlaceManager() {
		return placeManager;
	}

	@Override
	public PecInPraticheDB getPecInPraticheDB() {
		return praticheDb;
	}

	@Override
	public EventBus _getEventBus() {
		return getEventBus();
	}
	
	public String getFascicoloPath(){
		return clientId;
	}

	public String getAnnoPG(){
		return pg.split("/")[1];
	}
	
	public String getNumeroPG(){
		return pg.split("/")[0];
	}

	@Override
	public void downloadStampa(SafeUri uri) {
		getView().getDownloadWidget().sendDownload(uri);
	}

}

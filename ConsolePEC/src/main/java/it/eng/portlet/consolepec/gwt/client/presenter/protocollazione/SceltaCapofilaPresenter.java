package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione;

import it.eng.portlet.consolepec.gwt.client.command.ConsolePecCommandBinder;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazionePecInEvent;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazionePecOutEvent;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazionePraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazioneSceltaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.IndietroSceltaCapofilaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.modulistica.event.RilasciaInCaricoPraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.RilasciaInCaricoPecInEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione.AbstractDatiProtocollazioneCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione.ProtocollaDaSceltaFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione.ProtocollaFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione.ProtocollaPecInCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione.ProtocollaPecOutCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione.ProtocollaPraticaModulisticaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEmailInEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEmailInEvent.MostraSceltaCapofilaEmailInHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEmailOutEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEmailOutEvent.MostraSceltaCapofilaEmailOutHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaPraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaPraticaModulisticaEvent.MostraSceltaCapofilaPraticaModulisticaHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaProtocollazioneFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaProtocollazioneFascicoloEvent.MostraSceltaCapofilaProtocollazioneFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaSceltaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaSceltaFascicoloEvent.MostraSceltaCapofilaSceltaFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.RicercaCapofilaAction;
import it.eng.portlet.consolepec.gwt.shared.action.RicercaCapofilaResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.dto.DatiPg;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class SceltaCapofilaPresenter extends Presenter<SceltaCapofilaPresenter.MyView, SceltaCapofilaPresenter.MyProxy> implements MostraSceltaCapofilaEmailInHandler, MostraSceltaCapofilaProtocollazioneFascicoloHandler, MostraSceltaCapofilaEmailOutHandler,
		MostraSceltaCapofilaSceltaFascicoloHandler, MostraSceltaCapofilaPraticaModulisticaHandler, ConsolePecCommandBinder {

	public interface MyView extends View {

		void mostraSceltaCapofilaView(TreeMap<DatiPg, Set<ElementoPECRiferimento>> mapPec, TreeMap<DatiPg, Set<AllegatoDTO>> allegati, TreeMap<DatiPg, Set<CapofilaFromBA01DTO>> capofilaBa01);

		void setAvantiCommand(Command command);

		void setIndietroCommand(Command command);

		void setAnnullaCommand(Command command);

		void setCercaCommand(Command cercaCommand);

		String getAnnoPgCapofila();

		String getNumeroPgCapofila();

		void reset();

		DatiPg getDatiPg();

		void clear();

		void resetRicercaCapofila();

	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<SceltaCapofilaPresenter> {
	}

	private final PecInPraticheDB praticheDB;
	private final EventBus eventBus;
	private FascicoloDTO fascicolo;
	private final SitemapMenu sitemapMenu;
	private TreeMap<DatiPg, Set<AllegatoDTO>> allegati = new TreeMap<DatiPg, Set<AllegatoDTO>>();
	private TreeMap<DatiPg, Set<CapofilaFromBA01DTO>> ricercaCapofilaFromBa01 = new TreeMap<DatiPg, Set<CapofilaFromBA01DTO>>();
	private TreeMap<DatiPg, Set<ElementoPECRiferimento>> mapPec = new TreeMap<DatiPg, Set<ElementoPECRiferimento>>();
	private final DispatchAsync dispatchAsync;
	private DatiPg datiPgRef;
	private AbstractDatiProtocollazioneCommand mostraProtocollazioneCommand;
	private Command indietroCommand;
	private Command annullaCommand;
	private CreaFascicoloDTO creaFascicoloDTO;
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	
	private String idFascicolo;
	private String idEmailOut;
	private String idEmailIn;
	private String idPraticaModulistica;

	@Inject
	public SceltaCapofilaPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, 
			final PecInPraticheDB pecInDb, final SitemapMenu sitemapMenu, final DispatchAsync dispatchAsync, 
			ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.praticheDB = pecInDb;
		this.eventBus = eventBus;
		this.sitemapMenu = sitemapMenu;
		this.dispatchAsync = dispatchAsync;
		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void revealInParent() {

		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setAnnullaCommand(new AnnullaCommand());
		getView().setAvantiCommand(new AvantiCommand());
		getView().setCercaCommand(new CercaCommand());
		getView().setIndietroCommand(new IndietroCommand());
	}

	@Override
	protected void onHide() {
		super.onHide();
		getView().reset();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		reloadMap();

		getView().mostraSceltaCapofilaView(mapPec, allegati, ricercaCapofilaFromBa01);
		getView().clear();
		getView().resetRicercaCapofila();
		Window.scrollTo(0, 0);
	}

	private void reloadMap() {
		FascicoloVisitor visitor = new FascicoloVisitor(fascicolo);
		allegati.clear();
		mapPec.clear();
		ricercaCapofilaFromBa01.clear();
		allegati.putAll(visitor.getMapAllegati());
		mapPec.putAll(visitor.getMapPec());
		ricercaCapofilaFromBa01.putAll(visitor.getRicercaCapofilaFromBa01());
	}

	/**
	 * Gestisce la provenienza dal dettaglio di una pec out
	 */
	@Override
	@ProxyEvent
	public void onMostraSceltaCapofilaEmailOut(final MostraSceltaCapofilaEmailOutEvent event) {
		loadFascicolo(event.getIdFascicolo(), new Command() { // carico il fascicolo per la prepolazione del capofila
			
			@Override
			public void execute() {
				
				idFascicolo = event.getIdFascicolo();
				idEmailOut = event.getIdPecOut();
				
				ProtocollaPecOutCommand protocollaPecOutCommand = new ProtocollaPecOutCommand(SceltaCapofilaPresenter.this, configurazioniHandler, profilazioneUtenteHandler);
				protocollaPecOutCommand.setDatiPg(getView().getDatiPg());
				protocollaPecOutCommand.setIdEmailOut(event.getIdPecOut());
				protocollaPecOutCommand.setIdFascicolo(event.getIdFascicolo());
				mostraProtocollazioneCommand = protocollaPecOutCommand;
				indietroCommand = new IndietroPecOtuCommand(event.isInteroperabile());
				annullaCommand = new AnnullaDefaultCommand();
				revealInParent();

			}
		});
		
	}

	/**
	 * Gestisce la provenienza dal dettaglio di un fascicolo
	 */
	@Override
	@ProxyEvent
	public void onMostraSceltaCapofilaProtocollazioneFascicolo(final MostraSceltaCapofilaProtocollazioneFascicoloEvent event) {
		loadFascicolo(event.getIdFascicolo(), new Command() { // carico il fascicolo per la prepolazione del capofila
			
			@Override
			public void execute() {
				
				idFascicolo = event.getIdFascicolo();
				
				ProtocollaFascicoloCommand protocollaFascicoloCommand = new ProtocollaFascicoloCommand(SceltaCapofilaPresenter.this, configurazioniHandler, profilazioneUtenteHandler);
				protocollaFascicoloCommand.setDatiPg(getView().getDatiPg());
				protocollaFascicoloCommand.setIdFascicolo(event.getIdFascicolo());
				protocollaFascicoloCommand.setListaAllegati(new ArrayList<>(event.getAllegati()));
				protocollaFascicoloCommand.setListaPec((new ArrayList<>(event.getListEmail())));
				mostraProtocollazioneCommand = protocollaFascicoloCommand;
				annullaCommand = new AnnullaDefaultCommand();
				indietroCommand = new IndietroFascicoloCommand(event.getIdFascicolo());
				revealInParent();

			}
		});
		

	}

	/**
	 * Gestisce la provenienza dal form di creazione di un fascicolo a partire da mail in
	 */
	@Override
	@ProxyEvent
	public void onMostraSceltaCapofilaEmailIn(MostraSceltaCapofilaEmailInEvent event) {
		
		creaFascicoloDTO = event.getCreaFascicoloDTO();
		
		ProtocollaPecInCommand protocollaPecInCommand = new ProtocollaPecInCommand(this, profilazioneUtenteHandler);
		protocollaPecInCommand.setCreaFascicoloDTO(event.getCreaFascicoloDTO());
		protocollaPecInCommand.setDatiPg(getView().getDatiPg());
		mostraProtocollazioneCommand = protocollaPecInCommand;
		indietroCommand = new IndietroPecInCommand();
		annullaCommand = new AnnullaPecInCommand();
		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onMostraSceltaCapofilaPraticaModulistica(MostraSceltaCapofilaPraticaModulisticaEvent event) {
		
		creaFascicoloDTO = event.getCreaFascicoloDTO();
		
		ProtocollaPraticaModulisticaCommand protocollaPraticaModulisticaCommand = new ProtocollaPraticaModulisticaCommand(this);
		protocollaPraticaModulisticaCommand.setCreaFascicoloDTO(event.getCreaFascicoloDTO());
		protocollaPraticaModulisticaCommand.setDatiPg(getView().getDatiPg());
		mostraProtocollazioneCommand = protocollaPraticaModulisticaCommand;
		indietroCommand = new IndietroPraticaModulisticaCommand();
		annullaCommand = new AnnullaPraticaModulisticaCommand();
		revealInParent();
	}

	/**
	 * Gestisce la provenienza dal form scelta di un fascicolo da associare ad una mail in
	 */
	@Override
	@ProxyEvent
	public void onMostraSceltaCapofilaSceltaFascicolo(final MostraSceltaCapofilaSceltaFascicoloEvent event) {
		
		loadFascicolo(event.getIdFascicolo(), new Command() { // carico il fascicolo per la prepolazione del capofila
			
			@Override
			public void execute() {
				
				
				
				ProtocollaDaSceltaFascicoloCommand protocollaDaSceltaFascicoloCommand = new ProtocollaDaSceltaFascicoloCommand(SceltaCapofilaPresenter.this, configurazioniHandler, profilazioneUtenteHandler);
				protocollaDaSceltaFascicoloCommand.setDatiPg(getView().getDatiPg());
				protocollaDaSceltaFascicoloCommand.setIdEmailIn(event.getIdEmailIn());
				protocollaDaSceltaFascicoloCommand.setIdPraticaModulistica(event.getIdPraticaModulistica());
				protocollaDaSceltaFascicoloCommand.setIdFascicolo(event.getIdFascicolo());
				mostraProtocollazioneCommand = protocollaDaSceltaFascicoloCommand;
				indietroCommand = new IndietroSceltaFascicoloCommand(event.getIdFascicolo(), event.getIdEmailIn(), event.getIdPraticaModulistica());
				annullaCommand = new AnnullaSceltaFascicoloCommand(event.getIdEmailIn(), event.getIdPraticaModulistica());
				revealInParent();

			}
		});
		
	
	}

	/* CLASSI INTERNE */

	class FascicoloVisitor implements ElementoElencoVisitor {
		private HashMap<DatiPg, Set<AllegatoDTO>> mapAllegati = new HashMap<DatiPg, Set<AllegatoDTO>>();
		private HashMap<DatiPg, Set<ElementoPECRiferimento>> mapPec = new HashMap<DatiPg, Set<ElementoPECRiferimento>>();
		private HashMap<DatiPg, Set<CapofilaFromBA01DTO>> ricercaCapofilaFromBa01 = new HashMap<DatiPg, Set<CapofilaFromBA01DTO>>();

		DatiPg datiPgCorrente;

		public FascicoloVisitor() {
			start(null);
		}

		public FascicoloVisitor(FascicoloDTO fascicoloDTO) {
			start(fascicoloDTO);
		}

		public void start(FascicoloDTO fascicoloDto) {
			mapAllegati = new HashMap<DatiPg, Set<AllegatoDTO>>();
			mapPec = new HashMap<DatiPg, Set<ElementoPECRiferimento>>();
			ricercaCapofilaFromBa01 = new HashMap<DatiPg, Set<CapofilaFromBA01DTO>>();
			if (fascicoloDto != null)
				for (ElementoElenco e : fascicoloDto.getElenco())
					e.accept(this);
		}

		@Override
		public void visit(ElementoAllegato allegato) {
			Set<AllegatoDTO> allegati = mapAllegati.get(datiPgCorrente);
			if (allegati == null) {
				allegati = new HashSet<AllegatoDTO>();
			}
			allegati.add(allegato);
			mapAllegati.put(datiPgCorrente, allegati);
		}

		@Override
		public void visit(ElementoPECRiferimento pec) {

			final Set<ElementoPECRiferimento> listPec;
			if (mapPec.get(datiPgCorrente) == null) {
				listPec = new HashSet<ElementoPECRiferimento>();
			} else {
				listPec = mapPec.get(datiPgCorrente);
			}

			listPec.add(pec);
			mapPec.put(datiPgCorrente, listPec);

		}

		@Override
		public void visit(ElementoPraticaModulisticaRiferimento elementoPraticaModulisticaRiferimento) {
			//
		}
		
		@Override
		public void visit(ElementoComunicazioneRiferimento elementoComunicazioneRiferimento) {
			// NOP
		}
		@Override
		public void visit(ElementoGruppo gruppo) {

			// TODO Auto-generated method stub

		}

		@Override
		public void visit(ElementoGruppoProtocollatoCapofila capofila) {

			datiPgCorrente = new DatiPg(capofila.getNumeroPG(), capofila.getAnnoPG(), capofila.getIdTitolo(), capofila.getIdRubrica(), capofila.getIdSezione());

			mapAllegati.put(datiPgCorrente, new HashSet<AllegatoDTO>());
			mapPec.put(datiPgCorrente, new HashSet<ElementoPECRiferimento>());
			ricercaCapofilaFromBa01.put(datiPgCorrente, new HashSet<CapofilaFromBA01DTO>());
			if (capofila.getOggetto() != null) {
				Set<CapofilaFromBA01DTO> set = new HashSet<CapofilaFromBA01DTO>();
				set.add(new CapofilaFromBA01DTO(capofila.getOggetto(), capofila.getNumeroPG(), capofila.getAnnoPG(), false, capofila.getDataProtocollazione()));
				ricercaCapofilaFromBa01.put(datiPgCorrente, set);
			}
			for (ElementoElenco e : capofila.getElementi()) {
				e.accept(this);
			}
		}

		@Override
		public void visit(ElementoGruppoProtocollato subProt) {
			// TODO Auto-generated method stub
		}

		@Override
		public void visit(ElementoGrupppoNonProtocollato nonProt) {
			//
		}

		public HashMap<DatiPg, Set<AllegatoDTO>> getMapAllegati() {
			return mapAllegati;
		}

		public void setMapAllegati(HashMap<DatiPg, Set<AllegatoDTO>> mapAllegati) {
			this.mapAllegati = mapAllegati;
		}

		public HashMap<DatiPg, Set<ElementoPECRiferimento>> getMapPec() {
			return mapPec;
		}

		public void setMapPec(HashMap<DatiPg, Set<ElementoPECRiferimento>> mapPec) {
			this.mapPec = mapPec;
		}

		public HashMap<DatiPg, Set<CapofilaFromBA01DTO>> getRicercaCapofilaFromBa01() {
			return ricercaCapofilaFromBa01;
		}

		public void setRicercaCapofilaFromBa01(HashMap<DatiPg, Set<CapofilaFromBA01DTO>> ricercaCapofilaFromBa01) {
			this.ricercaCapofilaFromBa01 = ricercaCapofilaFromBa01;
		}

		public DatiPg getDatiPgCorrente() {
			return datiPgCorrente;
		}

		public void setDatiPgCorrente(DatiPg datiPgCorrente) {
			this.datiPgCorrente = datiPgCorrente;
		}

		

	}

	/* COMMAND */

	/**
	 * Command per la ricerca del capofila da BA01
	 * */
	public class CercaCommand implements Command {

		@Override
		public void execute() {
			ricercaCapofilaBa01(getView().getNumeroPgCapofila(), getView().getAnnoPgCapofila());
		}

		private void ricercaCapofilaBa01(String numeroPgCapofila, String annoPgCapofila) {

			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			eventBus.fireEvent(event);

			int annoPg = checkAnnoPgCapofila(annoPgCapofila);

			numeroPgCapofila = checkNumeroPgCapofila(numeroPgCapofila);

			if (annoPg > 0 && numeroPgCapofila != null) {

				ShowAppLoadingEvent.fire(SceltaCapofilaPresenter.this, true);

				RicercaCapofilaAction ricercaCapofilaAction = new RicercaCapofilaAction();
				ricercaCapofilaAction.setAnnoPg(annoPg);
				ricercaCapofilaAction.setNumeroPg(numeroPgCapofila);
				dispatchAsync.execute(ricercaCapofilaAction, new AsyncCallback<RicercaCapofilaResult>() {

					@Override
					public void onFailure(Throwable caught) {
						ShowAppLoadingEvent.fire(SceltaCapofilaPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						eventBus.fireEvent(event);
					}

					@Override
					public void onSuccess(RicercaCapofilaResult result) {
						ShowAppLoadingEvent.fire(SceltaCapofilaPresenter.this, false);
						if (result.isError() || result.isWarninig()) {
							ShowMessageEvent event = new ShowMessageEvent();
							if (result.isWarninig())
								event.setWarningMessage(result.getMessageWarninig());
							else
								event.setErrorMessage(result.getMessageError());
							eventBus.fireEvent(event);
						} else {

							DatiPg datiPg = new DatiPg(result.getNumeroPg(), "" + result.getAnnoPg(), "" + result.getTitolo(), "" + result.getRubrica(), "" + result.getSezione());
							datiPg.setCapofilaFromBa01(true);
							CapofilaFromBA01DTO capofila = new CapofilaFromBA01DTO(result.getOggetto(), result.getNumeroPg(), "" + result.getAnnoPg(), true, result.getDataProtocollazione());

							Set<CapofilaFromBA01DTO> set = new HashSet<CapofilaFromBA01DTO>();
							set.add(capofila);

							// se il pg non è mai stato caricato
							if (mapPec.get(datiPg) == null) {
								if (datiPg.isCapofilaFromBa01()) {
									// rimuovo il precedente
									if (datiPgRef != null) {
										mapPec.remove(datiPgRef);
										ricercaCapofilaFromBa01.remove(datiPgRef);
									}
									datiPgRef = datiPg;
								}
								// aggiungo alla lista
								ricercaCapofilaFromBa01.put(datiPg, set);
								mapPec.put(datiPg, new HashSet<FascicoloDTO.ElementoPECRiferimento>());
								getView().mostraSceltaCapofilaView(mapPec, allegati, ricercaCapofilaFromBa01);
//								mostraProtocollazioneCommand.setDatiPg(datiPg);	// XXX FM MCC
							}
						}

					}
				});
			}

		}

		private String checkNumeroPgCapofila(String numeroPgCapofila) {
			int num;
			if (numeroPgCapofila == null || numeroPgCapofila.isEmpty()) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setWarningMessage("Numero PG Capofila è obbligatorio");
				eventBus.fireEvent(event);
				return null;
			}
			try {
				if (numeroPgCapofila.length() > 7) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setWarningMessage("Numero PG Capofila deve essere al massimo di sette caratteri");
					eventBus.fireEvent(event);
					return null;
				}
				num = Integer.parseInt(numeroPgCapofila);
				if (num <= 0) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setWarningMessage("Numero PG Capofila deve essere maggiore di zero");
					eventBus.fireEvent(event);
					return null;
				}
			} catch (NumberFormatException e) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setWarningMessage("Numero PG Capofila non numerico");
				eventBus.fireEvent(event);
				return null;
			}
			return "" + num;
		}

		private int checkAnnoPgCapofila(String annoPgCapofila) {
			int annoCapofila = 0;
			if (annoPgCapofila != null && !annoPgCapofila.isEmpty()) {
				if (annoPgCapofila.length() == 4) {
					// annoPgCapofila deve essere di quattro caratteri
					try {
						annoCapofila = (int) NumberFormat.getFormat("0000").parse(annoPgCapofila);
						if (annoCapofila <= 0) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setWarningMessage("Anno PG Capofila deve essere maggiore di zero");
							eventBus.fireEvent(event);
						}
					} catch (NumberFormatException nfe) {
						// annoPgCapofila deve essere intero
						ShowMessageEvent event = new ShowMessageEvent();
						event.setWarningMessage("Anno PG Capofila non numerico");
						eventBus.fireEvent(event);
					}
				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setWarningMessage("Anno PG Capofila deve essere di quattro numeri");
					eventBus.fireEvent(event);
				}
			} else {
				// annoPgCapofila è obbligatorio
				ShowMessageEvent event = new ShowMessageEvent();
				event.setWarningMessage("Anno PG Capofila è obbligatorio");
				eventBus.fireEvent(event);
			}
			return annoCapofila;
		}
	}

	/**
	 * Annulla la protocollazione e torna alla maschera iniziale
	 * */
	public class AnnullaCommand implements Command {

		@Override
		public void execute() {
			annullaCommand.execute();
		}
	}

	public class AnnullaPecInCommand implements Command {

		@Override
		public void execute() {
			allegati = new TreeMap<DatiPg, Set<AllegatoDTO>>();
			ricercaCapofilaFromBa01 = new TreeMap<DatiPg, Set<CapofilaFromBA01DTO>>();
			mapPec = new TreeMap<DatiPg, Set<ElementoPECRiferimento>>();
			RilasciaInCaricoPecInEvent event = new RilasciaInCaricoPecInEvent(creaFascicoloDTO.getClientID());
			eventBus.fireEvent(event);
		}
	}

	
	public class AnnullaSceltaFascicoloCommand implements Command {
		
		private String idEmailIn;
		private String idPraticaModulistica;
		
		public AnnullaSceltaFascicoloCommand(String idEmailIn, String idPraticaModulistica) {
			super();
			this.idEmailIn = idEmailIn;
			this.idPraticaModulistica = idPraticaModulistica;
		}


		@Override
		public void execute() {
			allegati = new TreeMap<DatiPg, Set<AllegatoDTO>>();
			ricercaCapofilaFromBa01 = new TreeMap<DatiPg, Set<CapofilaFromBA01DTO>>();
			mapPec = new TreeMap<DatiPg, Set<ElementoPECRiferimento>>();
			Event<?> event;
			if(idEmailIn != null){
				event = new RilasciaInCaricoPecInEvent(idEmailIn);
			} else {
				event = new RilasciaInCaricoPraticaModulisticaEvent(idPraticaModulistica);
			}
			eventBus.fireEvent(event);
		}
	}

	public class AnnullaPraticaModulisticaCommand implements Command {

		@Override
		public void execute() {
			allegati = new TreeMap<DatiPg, Set<AllegatoDTO>>();
			ricercaCapofilaFromBa01 = new TreeMap<DatiPg, Set<CapofilaFromBA01DTO>>();
			mapPec = new TreeMap<DatiPg, Set<ElementoPECRiferimento>>();
			RilasciaInCaricoPraticaModulisticaEvent event = new RilasciaInCaricoPraticaModulisticaEvent(creaFascicoloDTO.getClientID());
			eventBus.fireEvent(event);
		}
	}

	private class AnnullaDefaultCommand implements Command {

		@Override
		public void execute() {
			allegati = new TreeMap<DatiPg, Set<AllegatoDTO>>();
			ricercaCapofilaFromBa01 = new TreeMap<DatiPg, Set<CapofilaFromBA01DTO>>();
			mapPec = new TreeMap<DatiPg, Set<ElementoPECRiferimento>>();
			getEventBus().fireEvent(new BackFromPlaceEvent());
		}

	}

	/**
	 * Mostra il form di protocollazione
	 * */
	public class AvantiCommand implements Command {

		@Override
		public void execute() {
			DatiPg datiPg = getView().getDatiPg();
			mostraProtocollazioneCommand.setDatiPg(datiPg);
			mostraProtocollazioneCommand.execute();
		}
	}

	public class IndietroCommand implements Command {

		@Override
		public void execute() {
			indietroCommand.execute();
		}
	}


	/* COMMAND INDIETRO */

	private class IndietroPecOtuCommand implements Command {
		private boolean interoperabile;

		public IndietroPecOtuCommand(boolean interoperabile) {
			super();
			this.interoperabile = interoperabile;
		}

		@Override
		public void execute() {
			if (interoperabile) {
				Place place = new Place();
				place.setToken(NameTokens.dettagliopecout);
				place.addParam(NameTokensParams.idPratica, idEmailOut);
				getEventBus().fireEvent(new GoToPlaceEvent(place));
			} else {
				ConfermaProtocollazionePecOutEvent event = new ConfermaProtocollazionePecOutEvent();
				event.setIdFascicolo(idFascicolo);
				event.setIdEmailOut(idEmailOut);
				getEventBus().fireEvent(event);
			}
		}
	}

	private class IndietroPecInCommand implements Command {

		@Override
		public void execute() {
			ConfermaProtocollazionePecInEvent event = new ConfermaProtocollazionePecInEvent();
			event.setCreaFascicoloDTO(creaFascicoloDTO);
			event.setIdEmailIn(creaFascicoloDTO.getClientID());
			getEventBus().fireEvent(event);
		}
	}

	private class IndietroPraticaModulisticaCommand implements Command {

		@Override
		public void execute() {
			ConfermaProtocollazionePraticaModulisticaEvent event = new ConfermaProtocollazionePraticaModulisticaEvent();
			event.setCreaFascicoloDTO(creaFascicoloDTO);
			event.setIdPraticaModulistica(creaFascicoloDTO.getClientID());
			getEventBus().fireEvent(event);
		}
	}

	private class IndietroSceltaFascicoloCommand implements Command {
		
		private String idFascicolo;
		private String idEmailIn;
		private String idPraticaModulistica;
		
		public IndietroSceltaFascicoloCommand(String idFascicolo, String idEmailIn, String idPraticaModulistica) {
			super();
			this.idFascicolo = idFascicolo;
			this.idEmailIn = idEmailIn;
			this.idPraticaModulistica = idPraticaModulistica;
		}



		@Override
		public void execute() {
			ConfermaProtocollazioneSceltaFascicoloEvent event = new ConfermaProtocollazioneSceltaFascicoloEvent();
			event.setIdFascicolo(idFascicolo);
			event.setIdPraticaModulistica(idPraticaModulistica);
			event.setIdEmail(idEmailIn);
			getEventBus().fireEvent(event);
		}
	}

	private class IndietroFascicoloCommand implements Command {
		private String idFascicolo;
		public IndietroFascicoloCommand(String idFascicolo) {
			this.idFascicolo = idFascicolo;
		}

		@Override
		public void execute() {
			IndietroSceltaCapofilaFascicoloEvent event = new IndietroSceltaCapofilaFascicoloEvent();
			event.setIdFascicolo(idFascicolo);
			getEventBus().fireEvent(event);
		}
	}
	
	private void loadFascicolo(String idFascicolo, final Command command){
		if (idFascicolo != null) {
			praticheDB.getFascicoloByPath(idFascicolo, sitemapMenu.containsLink(idFascicolo), new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO f) {

					fascicolo = f;
					command.execute();
				}

				@Override
				public void onPraticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}
			});
		} else {
			command.execute();
		}
	}
	

	@Override
	public EventBus _getEventBus() {
		return getEventBus();
	}

	@Override
	public DispatchAsync getDispatchAsync() {
		return dispatchAsync;
	}

	@Override
	public PlaceManager getPlaceManager() {
		return null;
	}

	@Override
	public PecInPraticheDB getPecInPraticheDB() {
		return praticheDB;
	}

	public FascicoloDTO getFascicoloDTO() {
		return fascicolo;
	}

}

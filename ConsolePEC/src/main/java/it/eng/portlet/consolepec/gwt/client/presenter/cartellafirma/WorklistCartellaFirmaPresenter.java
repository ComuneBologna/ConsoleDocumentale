package it.eng.portlet.consolepec.gwt.client.presenter.cartellafirma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Counter;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.UpdateSiteMapEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.GoToWorklistCartellaFirmaEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.GoToWorklistCartellaFirmaEvent.GoToWorklistCartellaFirmaHandler;
import it.eng.portlet.consolepec.gwt.client.event.dettaglioallegato.DettaglioAllegatoFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.dettaglioallegato.DettaglioAllegatoFineEvent.DettaglioAllegatoFineHandler;
import it.eng.portlet.consolepec.gwt.client.event.dettaglioallegato.DettaglioAllegatoInizioEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.WorklistHandler.WorklistHandlerCallback;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaRicercaApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaWizardApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaWizardApiClient.OperazioneWizardTaskFirma;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.cartellafirma.FormRicercaCartellaFirma;
import it.eng.portlet.consolepec.gwt.client.worklist.RigaEspansaCartellaFirmaStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistCartellaFirmaStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistCartellaFirmaStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistCartellaFirmaStrategy.RicercaCallback;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistCartellaFirmaStrategyImpl;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.CercaDocumentoFirmaVistoAction;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoUtenteTaskFirmaDTO;

/**
 *
 * @author biagiot
 *
 */
public class WorklistCartellaFirmaPresenter extends Presenter<WorklistCartellaFirmaPresenter.MyView, WorklistCartellaFirmaPresenter.MyProxy> implements GoToWorklistCartellaFirmaHandler, DettaglioAllegatoFineHandler {

	public interface MyView extends View {
		void initForm(AnagraficaWorklist worklistConfiguration, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler, Command cercaCommand);

		void initWorklist(WorklistCartellaFirmaStrategy strategy, //
				it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> accettaCommand, //
				it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> firmaCommand, //
				it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> diniegaCommand, //
				it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> ritiraCommand, //
				it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> rispondiCommand, //
				it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> evadiCommand, //
				Command selezioneDocumentoCommand);

		void impostaTitolo(String titolo);

		FormRicercaCartellaFirma getFormRicerca();

		void impostaAbilitazioniPulsantiera();

		void sendDownload(SafeUri uri);

		void espandiRiga(DocumentoFirmaVistoDTO documento);

		void nascondiRiga(DocumentoFirmaVistoDTO documento);

		void showFormErrors(List<String> errors);

		void setTipoRicerca(TipoUtenteTaskFirmaDTO tipoRicerca);
	}

	@ProxyCodeSplit
	@NameToken({ NameTokens.worklistcartellafirma })
	public interface MyProxy extends ProxyPlace<WorklistCartellaFirmaPresenter> {/**/}

	private final SitemapMenu sitemapMenu;
	private final EventBus eventBus;
	private final CartellaFirmaRicercaApiClient cartellaFirmaRicercaApiClient;
	private final CartellaFirmaWizardApiClient cartellaFirmaWizardApiClient;
	private final ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private final ConfigurazioniHandler configurazioniHandler;
	private AnagraficaWorklist worklistGenericaConfiguration;
	private String idWorklist;
	private WorklistCartellaFirmaStrategy strategy;
	private boolean firstReveal = true;
	private CercaFormCommand ricercaCommand;

	@Inject
	public WorklistCartellaFirmaPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final SitemapMenu sitemapMenu, final CartellaFirmaRicercaApiClient cartellaFirmaApiClient,
			final CartellaFirmaWizardApiClient cartellaFirmaWizardApiClient, final ProfilazioneUtenteHandler profilazioneUtenteHandler, final ConfigurazioniHandler configurazioniHandler) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.sitemapMenu = sitemapMenu;
		this.cartellaFirmaRicercaApiClient = cartellaFirmaApiClient;
		this.cartellaFirmaWizardApiClient = cartellaFirmaWizardApiClient;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		revealInParent();
		Window.scrollTo(0, 0);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	public void prepareFromRequest(final com.gwtplatform.mvp.shared.proxy.PlaceRequest request) {
		super.prepareFromRequest(request);

		profilazioneUtenteHandler.getWorklist(false, new WorklistHandlerCallback() {

			@Override
			public void onSuccess(Map<AnagraficaWorklist, Counter> worklist) {
				init(request);
			}

			@Override
			public void onFailure(String error) {
				throw new IllegalStateException(error);
			}
		});
	}

	private void init(com.gwtplatform.mvp.shared.proxy.PlaceRequest request) {
		idWorklist = request.getParameter(NameTokensParams.identificativoWorklist, null);
		worklistGenericaConfiguration = profilazioneUtenteHandler.getWorklist(idWorklist);

		if (worklistGenericaConfiguration != null) {

			if (firstReveal) {
				strategy = new WorklistCartellaFirmaStrategyImpl(Window.getClientWidth());
				strategy.addEspandiRigaEventListener(new EspandiRigaCartellaFirmaEvent());
				strategy.setEventBus(eventBus);
				strategy.setRicercaEventListener(new RicercaEventListener());
				strategy.setRigaEspansaStrategy(new RigaEspansaCartellaFirmaStrategy());
				strategy.setDownloadAllegatoCommand(new DownloadAllegatoCommand());
				strategy.setDettaglioAllegatoCommand(new MostraDettaglioAllegatoCommand());
				strategy.setDettaglioFascicoloCommand(new GoToDettaglioFascicolo());

				ricercaCommand = new CercaFormCommand(strategy, eventBus);
			}

			getView().impostaTitolo(worklistGenericaConfiguration.getTitoloWorklist());
			getView().initForm(worklistGenericaConfiguration, configurazioniHandler, profilazioneUtenteHandler, ricercaCommand);

			if (firstReveal) {
				firstReveal = false;
				getView().initWorklist(strategy, new AccettaDocumentoCommand(), new FirmaDocumentoCommand(), new DiniegaDocumentoCommand(), new RitiraDocumentiCommand(),
						new RispondiDocumentiCommand(), new EvadiCommand(), new SelezionaDocumentoCommand());
			}

			sitemapMenu.setActiveVoce(worklistGenericaConfiguration.getTitoloWorklist());
			strategy.resetSelezioni();
		}
	}

	@Override
	@ProxyEvent
	public void onGoToWorklistCartellaFirma(GoToWorklistCartellaFirmaEvent event) {
		if (ricercaCommand != null)
			ricercaCommand.refreshDataGrid();
		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onEndDettaglioAllegato(DettaglioAllegatoFineEvent event) {
		if (this.equals(event.getOpeningRequestor())) {
			ShowAppLoadingEvent.fire(WorklistCartellaFirmaPresenter.this, false);
			revealInParent();
		}
	}

	/**
	 *
	 * COMMAND
	 *
	 */
	private class CercaFormCommand implements Command {

		private WorklistCartellaFirmaStrategy strategy;
		private EventBus eventBus;

		public CercaFormCommand(WorklistCartellaFirmaStrategy strategy, EventBus eventBus) {
			this.strategy = strategy;
			this.eventBus = eventBus;
		}

		@Override
		public void execute() {
			if (getView().getFormRicerca().isSalvaFiltri()) {
				profilazioneUtenteHandler.aggiornaPreferenzeUtente(null, null, getView().getFormRicerca().getPreferenzeFiltriRicerca(), null);
			}
			strategy.restartSearchDatiGrid();
			eventBus.fireEvent(new UpdateSiteMapEvent());
		}

		public void refreshDataGrid() {
			strategy.restartSearchDatiGrid();
			eventBus.fireEvent(new UpdateSiteMapEvent());
		}
	}

	private class SelezionaDocumentoCommand implements Command {
		@Override
		public void execute() {
			getView().impostaAbilitazioniPulsantiera();
		}
	}

	private class FirmaDocumentoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> {
		@Override
		public Void exe(Set<DocumentoFirmaVistoDTO> documenti) {
			cartellaFirmaWizardApiClient.startWizardTaskFirma(documenti, OperazioneWizardTaskFirma.FIRMA);
			return null;
		}
	}

	private class AccettaDocumentoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> {
		@Override
		public Void exe(Set<DocumentoFirmaVistoDTO> documenti) {
			cartellaFirmaWizardApiClient.startWizardTaskFirma(documenti, OperazioneWizardTaskFirma.VISTO);
			return null;
		}
	}

	private class DiniegaDocumentoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> {
		@Override
		public Void exe(Set<DocumentoFirmaVistoDTO> documenti) {
			cartellaFirmaWizardApiClient.startWizardTaskFirma(documenti, OperazioneWizardTaskFirma.DINIEGO);
			return null;
		}
	}

	private class RispondiDocumentiCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> {
		@Override
		public Void exe(Set<DocumentoFirmaVistoDTO> documenti) {
			cartellaFirmaWizardApiClient.startWizardTaskFirma(documenti, OperazioneWizardTaskFirma.RISPOSTA_PARERE);
			return null;
		}
	}

	private class RitiraDocumentiCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> {
		@Override
		public Void exe(Set<DocumentoFirmaVistoDTO> documenti) {
			Map<String, List<AllegatoDTO>> mapPraticaAllegati = new HashMap<String, List<AllegatoDTO>>();
			for (DocumentoFirmaVistoDTO documento : documenti) {
				String key = documento.getClientIdFascicolo();
				if (mapPraticaAllegati.containsKey(key)) {
					mapPraticaAllegati.get(key).add(documento.getAllegato());
				} else {
					List<AllegatoDTO> temp = new ArrayList<AllegatoDTO>();
					temp.add(documento.getAllegato());
					mapPraticaAllegati.put(key, temp);
				}
			}
			cartellaFirmaWizardApiClient.ritiraDocumenti(mapPraticaAllegati, false);
			return null;
		}
	}

	private class EvadiCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> {
		@Override
		public Void exe(Set<DocumentoFirmaVistoDTO> t) {
			cartellaFirmaWizardApiClient.evadi(t);
			return null;
		}

	}

	private class DownloadAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {
		@Override
		public Void exe(AllegatoDTO t) {
			SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(t.getClientID(), t);
			getView().sendDownload(uri);
			return null;
		}

	}

	private class MostraDettaglioAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {
		@Override
		public Void exe(AllegatoDTO t) {
			DettaglioAllegatoInizioEvent.fire(WorklistCartellaFirmaPresenter.this, t, t.getClientID(), WorklistCartellaFirmaPresenter.this);
			return null;
		}
	}

	private class GoToDettaglioFascicolo implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, DocumentoFirmaVistoDTO> {
		@Override
		public Void exe(DocumentoFirmaVistoDTO t) {
			goToDettaglioFascicolo(t);
			return null;
		}
	}

	private class RicercaEventListener implements WorklistCartellaFirmaStrategy.RicercaEventListener {

		@Override
		public void onStartRicerca(int start, int length, ColonnaWorklist campoOrdinamento, boolean asc, RicercaCallback callback) {
			CercaDocumentoFirmaVistoAction action = new CercaDocumentoFirmaVistoAction();
			action.setFine(start + length);
			action.setInizio(start);
			action.setCampoOrdinamento(campoOrdinamento);
			action.setOrdinamentoAsc(asc);
			List<String> errors = getView().getFormRicerca().getFiltroDiRicerca(action);

			if (errors.size() > 0) {
				getView().showFormErrors(errors);
			} else {
				TipoUtenteTaskFirmaDTO tipoRicerca = action.isRicercaDaDestinatario() ? TipoUtenteTaskFirmaDTO.DESTINATARIO : TipoUtenteTaskFirmaDTO.PROPONENTE;
				getView().setTipoRicerca(tipoRicerca);

				cartellaFirmaRicercaApiClient.cercaDocumentiFirmaVisto(action, callback);
			}
		}
	}

	private class EspandiRigaCartellaFirmaEvent implements EspandiRigaEventListener {

		@Override
		public void onEspandiRiga(DocumentoFirmaVistoDTO documentoFirmaVisto, boolean isEspansa) {
			int clientWidth = Window.getClientWidth();
			if (clientWidth > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
				if (!isEspansa)
					getView().espandiRiga(documentoFirmaVisto);
				else
					getView().nascondiRiga(documentoFirmaVisto);
			} else {
				goToDettaglioFascicolo(documentoFirmaVisto);
			}
		}
	}

	private void goToDettaglioFascicolo(DocumentoFirmaVistoDTO dto) {
		Place place = new Place();
		place.setToken(dto.getTipologiaPratica().getDettaglioNameToken());
		place.addParam(NameTokensParams.showActions, Boolean.toString(false));
		place.addParam(NameTokensParams.idPratica, dto.getClientIdFascicolo());
		getEventBus().fireEvent(new GoToPlaceEvent(place));
	}
}

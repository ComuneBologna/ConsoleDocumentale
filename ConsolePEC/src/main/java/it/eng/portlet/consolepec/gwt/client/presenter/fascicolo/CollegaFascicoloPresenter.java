package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.collegamenti.Permessi;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.AzioneCollegamento;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.ChiudiCollegaFascicoloConRicercaEvent;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.ChiudiCollegaFascicoloDirettoEvent;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.MostraCollegaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.MostraCollegaFascicoloEvent.MostraCollegaFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.MostraCollegaFascicoloEvent.TipoMostraCollegaFascicolo;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoli;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoliResult;
import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;
import it.eng.portlet.consolepec.gwt.shared.dto.CondivisioneDto;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO.TipoPresaInCarico;

public class CollegaFascicoloPresenter extends Presenter<CollegaFascicoloPresenter.MyView, CollegaFascicoloPresenter.MyProxy> implements MostraCollegaFascicoloHandler {

	private String fascicoloFrom;
	private String fascicoloTo;
	private TipoMostraCollegaFascicolo tipoMostraCollegaFascicolo;
	private final PecInPraticheDB praticheDB;
	private DispatchAsync dispatcher;

	public interface MyView extends View {

		public void setAnnullaCommand(Command command);

		public void setCollegaFascicoloCommand(Command command);

		public void popolaPermessiCollegamentoFascicolo(String titolo, ImageResource icona, HashMap<String, Boolean> operazioni, Permessi permessi, Command onSelezioneCommand);

		public Permessi getPermessi();

		public void attivaPulsanteConferma(boolean enabled);

		void impostaTitolo(String titolo);

		public void attivaPulsanteElimina(boolean enabled);

		public void setEliminaCommand(Command eliminaCommand);

		void configuraSceltaRuolo(List<String> ruoliAbiltiati, it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> selectCommand);

		void clear();

	}

	private String ruoloSelezionato;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private ConfigurazioniHandler configurazioniHandler;

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<CollegaFascicoloPresenter> {/**/}

	@Inject
	public CollegaFascicoloPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final PecInPraticheDB praticheDB,
			ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		super(eventBus, view, proxy);
		this.praticheDB = praticheDB;
		this.dispatcher = dispatcher;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		getView().clear();
		ruoloSelezionato = null;
		caricaOperazioni();
	}

	private void caricaOperazioni() {
		ShowAppLoadingEvent.fire(CollegaFascicoloPresenter.this, true);
		// prendo il fascicolo originale al quale voglio collegare un altro fascicolo
		praticheDB.getFascicoloByPath(fascicoloFrom, false, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(final FascicoloDTO fascicoloProvenienzaDTO) {
				// prendo il fascicolo nuovo
				praticheDB.getFascicoloByPath(fascicoloTo, false, new PraticaFascicoloLoaded() {

					@Override
					public void onPraticaLoaded(final FascicoloDTO fascicoloDestinazioneDTO) {
						ShowAppLoadingEvent.fire(CollegaFascicoloPresenter.this, false);

						List<String> ruoliCond = new ArrayList<String>();

						for (CondivisioneDto cond : fascicoloDestinazioneDTO.getCondivisioni()) {

							if (profilazioneUtenteHandler.getDatiUtente().getAnagraficheRuoli().contains(cond.getRuolo())) {
								ruoliCond.add(cond.getRuolo().getEtichetta());
							}
						}

						if (!ruoliCond.isEmpty()) {
							if (ruoliCond.size() > 1) {
								getView().configuraSceltaRuolo(ruoliCond, new SelectRuoloCommand());

							} else if (!ruoliCond.get(0).equals(fascicoloProvenienzaDTO.getAssegnatario())) {
								ruoloSelezionato = configurazioniHandler.getAnagraficaRuoloByEtichetta(ruoliCond.get(0)) != null
										? configurazioniHandler.getAnagraficaRuoloByEtichetta(ruoliCond.get(0)).getRuolo() : null;

							}
						}

						HashMap<String, Boolean> mappaCompletaOperazioni = new HashMap<String, Boolean>();
						List<String> operazioniAbilitatePrecedentemente = new ArrayList<String>();
						for (CollegamentoDto collegamento : fascicoloProvenienzaDTO.getCollegamenti()) {
							if (fascicoloTo.equalsIgnoreCase(collegamento.getClientId())) {
								for (String operazione : collegamento.getOperazioni()) {
									operazioniAbilitatePrecedentemente.add(operazione);
								}
							}
						}
						for (String operazione : fascicoloProvenienzaDTO.getOperazioni()) {
							// aggiungo, settate a false, solo le operazioni che non sono già state abilitate per questo collegamento
							mappaCompletaOperazioni.put(operazione, operazioniAbilitatePrecedentemente.contains(operazione));
						}
						ImageResource icona = ConsolePECIcons._instance.fascicolo();
						String titolo = fascicoloDestinazioneDTO.getTitolo() + " (" + fascicoloDestinazioneDTO.getNumeroRepertorio() + ")";

						Permessi permessi = caricaPermessi(fascicoloProvenienzaDTO, fascicoloDestinazioneDTO);

						getView().popolaPermessiCollegamentoFascicolo(titolo, icona, mappaCompletaOperazioni, permessi, new Command() {

							@Override
							public void execute() {
								// non ci sono operazioni aggiuntive: il pulsante conferma va sempre abilitato
								// getView().attivaPulsanteConferma(!getView().getOperazioniSelezionate().isEmpty());
							}
						});
						if (operazioniAbilitatePrecedentemente.isEmpty()) {
							getView().impostaTitolo(ConsolePecConstants.TITOLO_ABILITA_COLLEGAMENTO);
							getView().attivaPulsanteElimina(false);
						} else {
							getView().impostaTitolo(ConsolePecConstants.TITOLO_MODIFICA_COLLEGAMENTO);
							getView().attivaPulsanteElimina(true);
						}
						if (fascicoloDestinazioneDTO.getTipoPresaInCarico().equals(TipoPresaInCarico.ALTRO_UTENTE)) {
							getView().attivaPulsanteConferma(false);
							ShowMessageEvent event = new ShowMessageEvent();
							event.setWarningMessage("Il fascicolo numero " + fascicoloDestinazioneDTO.getNumeroRepertorio() + " è attualmente in carico ad un altro utente");
							getEventBus().fireEvent(event);
						} else {
							getView().attivaPulsanteConferma(true);
						}

						getView().setEliminaCommand(new com.google.gwt.user.client.Command() {

							@Override
							public void execute() {
								ShowMessageEvent event = new ShowMessageEvent();
								event.setMessageDropped(true);
								getEventBus().fireEvent(event);
								CollegamentoFascicoli action = new CollegamentoFascicoli(CollegamentoFascicoli.LINK_DELETE, fascicoloTo, fascicoloFrom, getView().getPermessi());
								ShowAppLoadingEvent.fire(CollegaFascicoloPresenter.this, true);
								dispatcher.execute(action, new AsyncCallback<CollegamentoFascicoliResult>() {

									@Override
									public void onFailure(Throwable arg0) {
										ShowAppLoadingEvent.fire(CollegaFascicoloPresenter.this, false);
										ShowMessageEvent event = new ShowMessageEvent();
										event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
										getEventBus().fireEvent(event);
									}

									@Override
									public void onSuccess(CollegamentoFascicoliResult result) {
										ShowAppLoadingEvent.fire(CollegaFascicoloPresenter.this, false);
										if (result.isError()) {
											ShowMessageEvent event = new ShowMessageEvent();
											event.setErrorMessage(result.getErrorMsg());
											getEventBus().fireEvent(event);
										} else {
											praticheDB.remove(fascicoloFrom);
											praticheDB.remove(fascicoloTo);
											if (tipoMostraCollegaFascicolo.equals(TipoMostraCollegaFascicolo.CON_RICERCA)) {
												getEventBus().fireEvent(new ChiudiCollegaFascicoloConRicercaEvent(AzioneCollegamento.DELETE));
											} else {
												getEventBus().fireEvent(new ChiudiCollegaFascicoloDirettoEvent());
											}
										}
									}
								});
							}
						});

					}

					@Override
					public void onPraticaError(String error) {
						ShowAppLoadingEvent.fire(CollegaFascicoloPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}
				});
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(CollegaFascicoloPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}
		});
	}

	private static Permessi caricaPermessi(FascicoloDTO fascicoloSorgenteDTO, FascicoloDTO fascicoloRemotoDTO) {
		Permessi permessi = new Permessi();
		for (CollegamentoDto cs : fascicoloSorgenteDTO.getCollegamenti()) {
			if (cs.getClientId().equalsIgnoreCase(fascicoloRemotoDTO.getClientID())) {
				permessi.setRemotoAccessibileInLettura(cs.isAccessibileInLettura());
			}
		}
		for (CollegamentoDto cr : fascicoloRemotoDTO.getCollegamenti()) {
			if (cr.getClientId().equalsIgnoreCase(fascicoloSorgenteDTO.getClientID())) {
				permessi.setSorgenteAccessibileInLettura(cr.isAccessibileInLettura());
			}
		}
		return permessi;
	}

	@Override
	public void onHide() {
		super.onHide();
		getView().clear();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setAnnullaCommand(new com.google.gwt.user.client.Command() {

			@Override
			public void execute() {
				if (tipoMostraCollegaFascicolo.equals(TipoMostraCollegaFascicolo.CON_RICERCA)) {
					getEventBus().fireEvent(new ChiudiCollegaFascicoloConRicercaEvent(AzioneCollegamento.ANNULLA));
				} else {
					getEventBus().fireEvent(new ChiudiCollegaFascicoloDirettoEvent());
				}
			}
		});
		getView().setCollegaFascicoloCommand(new com.google.gwt.user.client.Command() {

			@Override
			public void execute() {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setMessageDropped(true);
				getEventBus().fireEvent(event);

				if (!getView().getPermessi().isRemotoAccessibileInLettura() && !getView().getPermessi().isSorgenteAccessibileInLettura()) {
					ShowAppLoadingEvent.fire(CollegaFascicoloPresenter.this, false);
					ShowMessageEvent event1 = new ShowMessageEvent();
					event1.setErrorMessage("Non è possibile inibire l'accesso in lettura in entrambi i versi");
					getEventBus().fireEvent(event1);

				} else {
					CollegamentoFascicoli action = null;

					if (ruoloSelezionato != null) {
						action = new CollegamentoFascicoli(CollegamentoFascicoli.LINK_MERGE, fascicoloTo, fascicoloFrom, getView().getPermessi(), ruoloSelezionato);

					} else {
						action = new CollegamentoFascicoli(CollegamentoFascicoli.LINK_MERGE, fascicoloTo, fascicoloFrom, getView().getPermessi());
					}

					ShowAppLoadingEvent.fire(CollegaFascicoloPresenter.this, true);
					dispatcher.execute(action, new AsyncCallback<CollegamentoFascicoliResult>() {

						@Override
						public void onFailure(Throwable arg0) {
							ShowAppLoadingEvent.fire(CollegaFascicoloPresenter.this, false);
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
							getEventBus().fireEvent(event);
						}

						@Override
						public void onSuccess(CollegamentoFascicoliResult result) {
							ShowAppLoadingEvent.fire(CollegaFascicoloPresenter.this, false);
							if (result.isError()) {
								ShowMessageEvent event = new ShowMessageEvent();
								event.setErrorMessage(result.getErrorMsg());
								getEventBus().fireEvent(event);
							} else {
								praticheDB.remove(fascicoloFrom);
								praticheDB.remove(fascicoloTo);
								if (tipoMostraCollegaFascicolo.equals(TipoMostraCollegaFascicolo.CON_RICERCA)) {
									getEventBus().fireEvent(new ChiudiCollegaFascicoloConRicercaEvent(AzioneCollegamento.MERGE));
								} else {
									getEventBus().fireEvent(new ChiudiCollegaFascicoloDirettoEvent());
								}
							}
						}
					});
				}
			}
		});
	}

	@Override
	@ProxyEvent
	public void onMostraCollegaFascicolo(MostraCollegaFascicoloEvent event) {
		fascicoloFrom = event.getFascicoloOriginarioPath();
		fascicoloTo = event.getFascicoloDaCollegarePath();
		tipoMostraCollegaFascicolo = event.getTipoMostraCollegaFascicolo();
		praticheDB.remove(fascicoloFrom);
		praticheDB.remove(fascicoloTo);
		revealInParent();
	}

	public class SelectRuoloCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> {

		@Override
		public Void exe(String t) {
			ruoloSelezionato = configurazioniHandler.getAnagraficaRuoloByEtichetta(t) != null ? configurazioniHandler.getAnagraficaRuoloByEtichetta(t).getRuolo() : null;
			return null;
		}
	}

}

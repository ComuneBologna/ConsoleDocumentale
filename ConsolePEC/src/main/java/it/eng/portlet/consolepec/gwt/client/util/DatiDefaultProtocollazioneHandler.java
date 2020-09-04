package it.eng.portlet.consolepec.gwt.client.util;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaModulisticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;

public class DatiDefaultProtocollazioneHandler {

	private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);

	@Inject
	public DatiDefaultProtocollazioneHandler(final PecInPraticheDB pecInPraticheDB, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler) {
		this.pecInPraticheDB = pecInPraticheDB;
		this.eventBus = eventBus;
		this.configurazioniHandler = configurazioniHandler;
	}
	
	private ConfigurazioniHandler configurazioniHandler;
	private PecInPraticheDB pecInPraticheDB;
	private PraticaDTO emailInDTO;
	private PraticaDTO emailOutDTO;
	private PraticaDTO modulisticaDTO;
	private PraticaDTO fascicoloDTO;
	private EventBus eventBus;

	public PecInPraticheDB getPecInPraticheDB() {
		return pecInPraticheDB;
	}

	public void setPecInPraticheDB(PecInPraticheDB pecInPraticheDB) {
		this.pecInPraticheDB = pecInPraticheDB;
	}

	public PraticaDTO getEmailInDTO() {
		return emailInDTO;
	}

	public void setEmailInDTO(PraticaDTO emailInDTO) {
		this.emailInDTO = emailInDTO;
	}

	public PraticaDTO getEmailOutDTO() {
		return emailOutDTO;
	}

	public void setEmailOutDTO(PraticaDTO emailOutDTO) {
		this.emailOutDTO = emailOutDTO;
	}

	public PraticaDTO getModulisticaDTO() {
		return modulisticaDTO;
	}

	public void setModulisticaDTO(PraticaDTO modulisticaDTO) {
		this.modulisticaDTO = modulisticaDTO;
	}

	public PraticaDTO getFascicoloDTO() {
		return fascicoloDTO;
	}

	public void setFascicoloDTO(PraticaDTO fascicoloDTO) {
		this.fascicoloDTO = fascicoloDTO;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void retrieveDatiDefaultProtocollazione(final DatiDefaultProtocollazione datiDefaultProtocollazione, final PresenterWidget<?> presenter,
			final RecuperoDatiObserver recuperoDatiObserver) {

		recuperoDatiDefaultProtocollazione(datiDefaultProtocollazione, presenter, recuperoDatiObserver);

		recuperoDatiDefaultProtocollazionePecIn(datiDefaultProtocollazione, presenter, recuperoDatiObserver);

		recuperoDatiDefaultProtocollazioneModulo(datiDefaultProtocollazione, presenter, recuperoDatiObserver);

		recuperoDatiProtocollazionePecOut(datiDefaultProtocollazione, presenter, recuperoDatiObserver);

		recuperoDatiDefaultProtocollazioneNuovaMail(datiDefaultProtocollazione, presenter, recuperoDatiObserver);

		recuperoDatiDefaultProtocollazioneNuovoModulo(datiDefaultProtocollazione, presenter, recuperoDatiObserver);
	}

	private void recuperoDatiProtocollazionePecOut(final DatiDefaultProtocollazione datiDefaultProtocollazione, final PresenterWidget<?> presenter,
			final RecuperoDatiObserver recuperoDatiObserver) {
		if (fascicoloDTO != null && emailInDTO == null && emailOutDTO != null && modulisticaDTO == null) {
			pecInPraticheDB.getFascicoloByPath(fascicoloDTO.getClientID(), false, new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO fascicolo) {
					ShowAppLoadingEvent.fire(presenter, false);
					AnagraficaFascicolo anagraficaFascicolo = configurazioniHandler.getAnagraficaFascicolo(fascicolo.getTipologiaPratica().getNomeTipologia());
					datiDefaultProtocollazione.setTitolazione(anagraficaFascicolo.getTitolazione());
					datiDefaultProtocollazione.setOggettoProtocollazione(fascicolo.getTitolo());
					datiDefaultProtocollazione.setDatiProcedimento(anagraficaFascicolo.getDatiProcedimento());
					recuperoDatiObserver.onComplete();
				}

				@Override
				public void onPraticaError(String error) {
					ShowAppLoadingEvent.fire(presenter, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);

				}
			});
		}
	}

	private void recuperoDatiDefaultProtocollazione(final DatiDefaultProtocollazione datiDefaultProtocollazione, final PresenterWidget<?> presenter,
			final RecuperoDatiObserver recuperoDatiObserver) {
		if (fascicoloDTO != null && emailInDTO == null && emailOutDTO == null && modulisticaDTO == null) {
			pecInPraticheDB.getFascicoloByPath(fascicoloDTO.getClientID(), false, new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO fascicolo) {
					ShowAppLoadingEvent.fire(presenter, false);
					AnagraficaFascicolo anagraficaFascicolo = configurazioniHandler.getAnagraficaFascicolo(fascicolo.getTipologiaPratica().getNomeTipologia());
					
					datiDefaultProtocollazione.setTitolazione(anagraficaFascicolo.getTitolazione());
					datiDefaultProtocollazione.setOggettoProtocollazione(fascicolo.getTitolo());
					datiDefaultProtocollazione.setDatiProcedimento(anagraficaFascicolo.getDatiProcedimento());
					recuperoDatiObserver.onComplete();
				}

				@Override
				public void onPraticaError(String error) {
					ShowAppLoadingEvent.fire(presenter, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);

				}
			});
		}

	}

	private void recuperoDatiDefaultProtocollazioneNuovaMail(final DatiDefaultProtocollazione datiDefaultProtocollazione,
			final PresenterWidget<?> presenter, final RecuperoDatiObserver recuperoDatiObserver) {
		if (fascicoloDTO == null && emailInDTO != null && emailOutDTO == null && modulisticaDTO == null) {

			pecInPraticheDB.getPecInByPath(emailInDTO.getClientID(), false, new PraticaEmaiInlLoaded() {

				@Override
				public void onPraticaLoaded(PecInDTO pec) {
					ShowAppLoadingEvent.fire(presenter, false);
					datiDefaultProtocollazione.setDataDiProtocollazione(dateFormat.parse(pec.getDataOraArrivo()));
					recuperoDatiObserver.onComplete();
				}

				@Override
				public void onPraticaError(String error) {
					ShowAppLoadingEvent.fire(presenter, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}
			});
		}

	}

	// protocollazione pec in ingresso
	private void recuperoDatiDefaultProtocollazionePecIn(final DatiDefaultProtocollazione datiDefaultProtocollazione,
			final PresenterWidget<?> presenter, final RecuperoDatiObserver recuperoDatiObserver) {
		if (fascicoloDTO != null && emailInDTO != null && emailOutDTO == null && modulisticaDTO == null) {
			pecInPraticheDB.getFascicoloByPath(fascicoloDTO.getClientID(), false, new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO fascicolo) {
					
					AnagraficaFascicolo anagraficaFascicolo = configurazioniHandler.getAnagraficaFascicolo(fascicolo.getTipologiaPratica().getNomeTipologia());
					
					datiDefaultProtocollazione.setTitolazione(anagraficaFascicolo.getTitolazione());
					datiDefaultProtocollazione.setOggettoProtocollazione(fascicolo.getTitolo());
					datiDefaultProtocollazione.setDatiProcedimento(anagraficaFascicolo.getDatiProcedimento());
					pecInPraticheDB.getPecInByPath(emailInDTO.getClientID(), false, new PraticaEmaiInlLoaded() {

						@Override
						public void onPraticaLoaded(PecInDTO pec) {
							ShowAppLoadingEvent.fire(presenter, false);
							datiDefaultProtocollazione.setDataDiProtocollazione(dateFormat.parse(pec.getDataOraArrivo()));
							recuperoDatiObserver.onComplete();
						}

						@Override
						public void onPraticaError(String error) {
							ShowAppLoadingEvent.fire(presenter, false);
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
							eventBus.fireEvent(event);
						}
					});
				}

				@Override
				public void onPraticaError(String error) {
					ShowAppLoadingEvent.fire(presenter, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);

				}
			});
		}
	}

	// protocollazione modulo
	private void recuperoDatiDefaultProtocollazioneModulo(final DatiDefaultProtocollazione datiDefaultProtocollazione,
			final PresenterWidget<?> presenter, final RecuperoDatiObserver recuperoDatiObserver) {
		if (fascicoloDTO != null && emailInDTO == null && emailOutDTO == null && modulisticaDTO != null) {
			pecInPraticheDB.getFascicoloByPath(fascicoloDTO.getClientID(), false, new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO fascicolo) {
					
					AnagraficaFascicolo anagraficaFascicolo = configurazioniHandler.getAnagraficaFascicolo(fascicolo.getTipologiaPratica().getNomeTipologia());
					
					datiDefaultProtocollazione.setTitolazione(anagraficaFascicolo.getTitolazione());
					datiDefaultProtocollazione.setDatiProcedimento(anagraficaFascicolo.getDatiProcedimento());
					pecInPraticheDB.getPraticaModulisticaByPath(modulisticaDTO.getClientID(), false, new PraticaModulisticaLoaded() {

						@Override
						public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica) {
							ShowAppLoadingEvent.fire(presenter, false);
							datiDefaultProtocollazione.setDataDiProtocollazione(dateFormat.parse(pratica.getDataOraCreazione()));
							datiDefaultProtocollazione.setCodiceFiscaleProvenienza(pratica.getCodiceFiscaleProvenienza());
							datiDefaultProtocollazione.setProvenienza(pratica.getProvenienza());
							datiDefaultProtocollazione.setOggettoProtocollazione(pratica.getTitoloProtocollazione());
							recuperoDatiObserver.onComplete();

						}

						@Override
						public void onPraticaModulisticaError(String error) {
							ShowAppLoadingEvent.fire(presenter, false);
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
							eventBus.fireEvent(event);
						}
					});
				}

				@Override
				public void onPraticaError(String error) {
					ShowAppLoadingEvent.fire(presenter, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);

				}
			});
		}
	}

	// protocollazione modulo
	private void recuperoDatiDefaultProtocollazioneNuovoModulo(final DatiDefaultProtocollazione datiDefaultProtocollazione,
			final PresenterWidget<?> presenter, final RecuperoDatiObserver recuperoDatiObserver) {
		if (fascicoloDTO == null && emailInDTO == null && emailOutDTO == null && modulisticaDTO != null) {

			pecInPraticheDB.getPraticaModulisticaByPath(modulisticaDTO.getClientID(), false, new PraticaModulisticaLoaded() {

				@Override
				public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica) {
					ShowAppLoadingEvent.fire(presenter, false);
					datiDefaultProtocollazione.setDataDiProtocollazione(dateFormat.parse(pratica.getDataOraCreazione()));
					datiDefaultProtocollazione.setCodiceFiscaleProvenienza(pratica.getCodiceFiscaleProvenienza());
					datiDefaultProtocollazione.setProvenienza(pratica.getProvenienza());
					datiDefaultProtocollazione.setOggettoProtocollazione(pratica.getTitoloProtocollazione());
					recuperoDatiObserver.onComplete();

				}

				@Override
				public void onPraticaModulisticaError(String error) {
					ShowAppLoadingEvent.fire(presenter, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}
			});
		}
	}

	public interface RecuperoDatiObserver {
		public void onComplete();
	}

	public void clear() {
		emailInDTO = emailOutDTO = modulisticaDTO = fascicoloDTO = null;
	}

}

package it.eng.portlet.consolepec.gwt.client.tasks.gestionefascicolo.operazioni.richiedifirma.impl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.richiedifirma.RichiediFirmaVistoFineEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.richiedifirma.RichiediFirmaVistoFineEvent.RichiediFirmaVistoFineHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.richiedifirma.RichiediFirmaVistoInizioEvent;
import it.eng.portlet.consolepec.gwt.client.tasks.gestionefascicolo.operazioni.richiedifirma.RichiediFirmaTaskApiClient;
import it.eng.portlet.consolepec.gwt.client.util.TaskFirmaUtils;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.richiedifirma.RichiediFirmaVistoAction;
import it.eng.portlet.consolepec.gwt.shared.action.richiedifirma.RichiediFirmaVistoActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioGruppoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioUtenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.richiedifirma.AllegatoRichiediFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.richiedifirma.RichiediFirmaVistoDTO;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

/**
 *
 * @author biagiot
 *
 */
public class RichiediFirmaTaskApiClientImpl implements RichiediFirmaTaskApiClient, HasHandlers, RichiediFirmaVistoFineHandler {

	private final DispatchAsync dispatcher;
	private final PecInPraticheDB pecInPraticheDB;
	private final EventBus eventBus;
	private final PlaceManager placeManager;
	private final SitemapMenu sitemapMenu;

	private FascicoloDTO fascicoloDTO;
	private Set<AllegatoDTO> allegati;


	@Inject
	public RichiediFirmaTaskApiClientImpl(final DispatchAsync dispatcher, final PecInPraticheDB pecInPraticheDB, final EventBus eventBus, final PlaceManager placeManager, final SitemapMenu sitemapMenu) {
		this.dispatcher = dispatcher;
		this.pecInPraticheDB = pecInPraticheDB;
		eventBus.addHandler(RichiediFirmaVistoFineEvent.getType(), this);
		this.eventBus = eventBus;
		this.placeManager = placeManager;
		this.sitemapMenu = sitemapMenu;
	}

	@Override
	public void goToRichiediVistoFirma(String fascicoloPath, final Set<AllegatoDTO> allegatiSelezionati) {

		ShowAppLoadingEvent.fire(RichiediFirmaTaskApiClientImpl.this, true);

		this.pecInPraticheDB.getFascicoloByPath(fascicoloPath, true, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {
				fascicoloDTO = fascicolo;
				allegati = allegatiSelezionati;

				boolean allegatoProtocollato = false;
				for (AllegatoDTO allegato : allegatiSelezionati) {
					if (allegato.isProtocollato()) {
						allegatoProtocollato = true;
					 	break;
					}
				}

				RichiediFirmaVistoInizioEvent.fire(RichiediFirmaTaskApiClientImpl.this, RichiediFirmaTaskApiClientImpl.this, fascicolo.getAssegnatario(), allegatoProtocollato);
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(RichiediFirmaTaskApiClientImpl.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}
		});
	}

	@Override
	public boolean isTaskInvocabile(FascicoloDTO pratica, Set<AllegatoDTO> allegati) {

		if (pratica.isPropostaFirmaAbilitato() && allegati.size() > 0) {

			for (AllegatoDTO allegato : allegati) {
				if (Boolean.TRUE.equals(allegato.isLock()))
					return false;
				}

			return true;
		}

		return false;
	}

	@Override
	public boolean isTaskAnnullabile(final FascicoloDTO pratica, final Set<AllegatoDTO> allegati) {
		if (!pratica.isRitiroPropostaFirmaEnabled() || allegati.size() == 0)
			return false;

		for (AllegatoDTO allegato : allegati) {
			TaskFirmaDTO taskFirma = TaskFirmaUtils.getTaskFirmaAttivo(pratica, allegato);
			if (taskFirma == null)
				return false;
		}

		return true;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEventFromSource(event, this);
	}

	@Override
	@ProxyEvent
	public void onRichiediFirmaVistoFine(RichiediFirmaVistoFineEvent event) {

		if (this.equals(event.getOpeningRequestor())) {

			if (event.isAnnulla()) {
				gotoFascicolo();

			} else {
				RichiediFirmaVistoDTO richiediFirmaVistoDTO = event.getRichiediFirmaVisto();
				richiediFirmaVistoDTO.setClientIdFascicolo(fascicoloDTO.getClientID());

				if (!fascicoloDTO.getAssegnatario().equalsIgnoreCase(richiediFirmaVistoDTO.getProponente().getNomeGruppo())) {
					richiediFirmaVistoDTO.getProponente().setNomeGruppo(fascicoloDTO.getAssegnatario());
				}

				Set<AllegatoRichiediFirmaDTO> allegatiRichiediFirma = new HashSet<AllegatoRichiediFirmaDTO>();
				for (AllegatoDTO allegato : allegati) {
					AllegatoRichiediFirmaDTO allegatoRichiediFirmaDTO = new AllegatoRichiediFirmaDTO(allegato.getNome(), allegato.getVersioneCorrente());
					allegatiRichiediFirma.add(allegatoRichiediFirmaDTO);
				}
				richiediFirmaVistoDTO.getAllegati().addAll(allegatiRichiediFirma);

				richiediFirmaVisto(richiediFirmaVistoDTO);
			}
		}
	}

	private void gotoFascicolo() {
		this.fascicoloDTO = null;
		this.allegati = null;
		placeManager.revealCurrentPlace();
	}

	@Override
	public void richiediFirmaVisto(RichiediFirmaVistoDTO richiediFirmaVistoDTO) {

		ShowAppLoadingEvent.fire(RichiediFirmaTaskApiClientImpl.this, true);
		RichiediFirmaVistoAction action = new RichiediFirmaVistoAction(richiediFirmaVistoDTO);

		dispatcher.execute(action, new AsyncCallback<RichiediFirmaVistoActionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(RichiediFirmaTaskApiClientImpl.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}

			@Override
			public void onSuccess(RichiediFirmaVistoActionResult result) {
				if (result.getError()) {
					ShowAppLoadingEvent.fire(RichiediFirmaTaskApiClientImpl.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);

				} else {
					PraticaDTO fascicolo = result.getPratica();
					pecInPraticheDB.insertOrUpdate(fascicolo.getClientID(), fascicolo, sitemapMenu.containsLink(fascicolo.getClientID()));
					ShowAppLoadingEvent.fire(RichiediFirmaTaskApiClientImpl.this, false);
					gotoFascicolo();
				}
			}
		});
	}

	@Override
	public DestinatarioDTO getDestinatario(Utente utente) {
		DestinatarioUtenteDTO dest = new DestinatarioUtenteDTO();
		dest.setUserId(utente.getUsername());
		dest.setNome(utente.getNome());
		dest.setCognome(utente.getCognome());
		dest.setNomeCompleto(utente.getNome() + " " + utente.getCognome());
		dest.setSettore(utente.getDipartimento());
		dest.setMatricola(utente.getMatricola());
		return dest;
	}


	@Override
	public DestinatarioDTO getDestinatario(AnagraficaRuolo ruolo) {
		DestinatarioGruppoDTO dest = new DestinatarioGruppoDTO();
		dest.setNomeGruppoDisplay(ruolo.getEtichetta());
		dest.setNomeGruppoConsole(ruolo.getRuolo());
		return dest;
	}
}

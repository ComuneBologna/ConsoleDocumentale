package it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.impl;

import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaRicercaApiClient;
import it.eng.portlet.consolepec.gwt.client.widget.cartellafirma.FormRicercaCartellaFirma;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistCartellaFirmaStrategy.RicercaCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.CercaDocumentoFirmaVistoAction;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.CercaDocumentoFirmaVistoActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoTaskFirmaDTO;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;

/**
 *
 * @author biagiot
 *
 */
public class CartellaFirmaRicercaApiClientImpl implements CartellaFirmaRicercaApiClient, HasHandlers {

	private final DispatchAsync dispatcher;
	private final EventBus eventBus;

	@Inject
	public CartellaFirmaRicercaApiClientImpl(DispatchAsync dispatcher, EventBus eventBus) {
		this.dispatcher = dispatcher;
		this.eventBus = eventBus;
	}

	@Override
	public void cercaDocumentiFirmaVisto(CercaDocumentoFirmaVistoAction filter, final RicercaCallback callback) {
		filter.setCount(false);

		ShowAppLoadingEvent.fire(CartellaFirmaRicercaApiClientImpl.this, true);
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);

		dispatcher.execute(filter, new AsyncCallback<CercaDocumentoFirmaVistoActionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(CartellaFirmaRicercaApiClientImpl.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
				callback.setNoResult();
			}

			@Override
			public void onSuccess(CercaDocumentoFirmaVistoActionResult result) {

				if (result.isError()) {
					ShowAppLoadingEvent.fire(CartellaFirmaRicercaApiClientImpl.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMessage());
					eventBus.fireEvent(event);

				} else {
					callback.setRisultati(result.getDocumentiFirmaVisto());
					ShowAppLoadingEvent.fire(CartellaFirmaRicercaApiClientImpl.this, false);
				}

			}
		});

		if (filter.getInizio() == 0) {
			filter.setCount(true);

			dispatcher.execute(filter, new AsyncCallback<CercaDocumentoFirmaVistoActionResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(CartellaFirmaRicercaApiClientImpl.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
					callback.setNoResult();
				}

				@Override
				public void onSuccess(CercaDocumentoFirmaVistoActionResult result) {

					if (result.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMessage());
						eventBus.fireEvent(event);

					} else {
						callback.setCount(result.getMaxResult(), result.isEstimate());
					}

				}
			});
		}
	}

	@Override
	public List<String> creaFiltroDiRicerca (FormRicercaCartellaFirma formRicercaCartellaFirma, CercaDocumentoFirmaVistoAction action) {

		List<String> errors = new ArrayList<String>();

		if (formRicercaCartellaFirma.getTipoStatoRichiesta() == null) {
			errors.add("Stato proposta obbligatorio");

		} else {
			action.setRicercaDaDestinatario(formRicercaCartellaFirma.isDestinatario());
			action.setOggetto(formRicercaCartellaFirma.getOggetto());
			action.setMittenteOriginale(formRicercaCartellaFirma.getMittenteOriginale());
			action.setDataDa(formRicercaCartellaFirma.getDataFrom());
			action.setDataA(formRicercaCartellaFirma.getDataTo());
			action.setDataScadenzaDa(formRicercaCartellaFirma.getDataScadenzaFrom());
			action.setDataScadenzaA(formRicercaCartellaFirma.getDataScadenzaTo());
			action.setTipoProposta(formRicercaCartellaFirma.getTipoProposta());
			action.setTipoStato(formRicercaCartellaFirma.getTipoStatoRichiesta());

			if (formRicercaCartellaFirma.getProponente() != null)
				action.getProponenti().add(formRicercaCartellaFirma.getProponente());

			if (formRicercaCartellaFirma.isAvanzatePanel()) {
				action.setTipologiaPratica(formRicercaCartellaFirma.getTipologiaPratica());
				action.setIdDocumentaleFascicolo(formRicercaCartellaFirma.getIdDocumentale());
				action.setTitoloFascicolo(formRicercaCartellaFirma.getTitoloFascicolo());

				if (action.getStato() != null) {

					if (action.getTipoProposta() != null && !StatoTaskFirmaDTO.fromTipoStatoETipoProposta(action.getTipoStato(), action.getTipoProposta()).contains(action.getStato())) {
						errors.add("Stato di dettaglio non compatibile con il tipo di proposta scelto");
					}

					if(!action.getStato().getTipologiaStato().equals(action.getTipoStato())) {
						errors.add("Stato di dettaglio non compatibile con lo stato scelto");
					}
				}

				if (action.getStatoDestinatario() != null && action.getTipoProposta() != null && !action.getStatoDestinatario().getTipiProposta().contains(action.getTipoProposta())) {
					errors.add("Stato destinatario incompatibile con il tipo proposta scelto");
				}

				action.setStato(formRicercaCartellaFirma.getStatoDiDettaglio());
				action.setStatoDestinatario(formRicercaCartellaFirma.getStatoDestinatario());
			}
		}

		return errors;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEventFromSource(event, this);
	}
}

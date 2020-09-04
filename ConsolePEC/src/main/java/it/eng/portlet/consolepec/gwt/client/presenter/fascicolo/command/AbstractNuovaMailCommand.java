package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaRisposta;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaRispostaResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractNuovaMailCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public AbstractNuovaMailCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	public abstract boolean isInteroperabile();
	
	@Override
	public void _execute() {
		// invoco la creazione della pratica bozza
		CreaRisposta risposta = new CreaRisposta(getPresenter().getFascicoloPath());
		ShowAppLoadingEvent.fire(getPresenter(), true);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);
		
		risposta.setInteroperabile(isInteroperabile());
		
		getPresenter().getDispatchAsync().execute(risposta, new AsyncCallback<CreaRispostaResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(CreaRispostaResult result) {
				if (!result.getError()) {
					// inserisco il fascicolo(aggiornato) e la nuova bozza,
					// sul db locale

					getPresenter().getPecInPraticheDB().insertOrUpdate(result.getFascicoloDTO().getClientID(), result.getFascicoloDTO(), getPresenter().getSitemapMenu().containsLink(result.getFascicoloDTO().getClientID()));
					getPresenter().getPecInPraticheDB().insertOrUpdate(result.getPecOutDTO().getClientID(), result.getPecOutDTO(), getPresenter().getSitemapMenu().containsLink(result.getPecOutDTO().getClientID()));
					// richiedo il display del dettaglio
					Place place = new Place();
					place.setToken(NameTokens.dettagliopecout);
					place.addParam(NameTokensParams.idPratica, result.getPecOutDTO().getClientID());
					
					if(isInteroperabile()) place.addParam(NameTokensParams.interoperabile, "true");
					
					getPresenter()._getEventBus().fireEvent(new GoToPlaceEvent(place));
					ShowAppLoadingEvent.fire(getPresenter(), false);
					
				} else {
					ShowAppLoadingEvent.fire(getPresenter(), false);

					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getMessError());
					getPresenter()._getEventBus().fireEvent(event);
				}
			}
		});
	}

}

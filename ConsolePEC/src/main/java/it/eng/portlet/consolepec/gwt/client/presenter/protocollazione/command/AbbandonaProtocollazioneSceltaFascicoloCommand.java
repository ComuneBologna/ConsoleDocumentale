package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.AggiungiPraticaAFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.pec.AggiungiPraticaAFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AbbandonaProtocollazioneSceltaFascicoloCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public AbbandonaProtocollazioneSceltaFascicoloCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		AggiungiPraticaAFascicolo action = null;

		final String idFascicolo = getPresenter().getIdFascicolo();
		final String idEmailIn = getPresenter().getIdEmailIn();
		final String idPraticaModulistica = getPresenter().getIdPraticaModulistica();

		if (idEmailIn != null) {
			action = new AggiungiPraticaAFascicolo(idFascicolo, idEmailIn);
		} else {
			action = new AggiungiPraticaAFascicolo(idFascicolo, idPraticaModulistica);
		}

		ShowAppLoadingEvent.fire(getPresenter(), true);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);
		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<AggiungiPraticaAFascicoloResult>() {
			@Override
			public void onSuccess(AggiungiPraticaAFascicoloResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (!result.getIsError()) {
					getPresenter().getPecInPraticheDB().remove(idFascicolo);
					if (idPraticaModulistica != null) {
						getPresenter().getPecInPraticheDB().remove(idPraticaModulistica);
						mostraDettaglioFascicolo(idFascicolo);

					} else {
						getPresenter().getPecInPraticheDB().remove(idEmailIn);
						mostraDettaglioFascicolo(idFascicolo);
					}
				} else {
					writeErrorMessage(new RuntimeException(result.getMessError()));
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				writeErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			}

		});
	}

	private void writeErrorMessage(RuntimeException runtimeException) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(runtimeException);
		getPresenter()._getEventBus().fireEvent(event);

	}

	private void writeErrorMessage(String error) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(error);
		getPresenter()._getEventBus().fireEvent(event);

	}

	private void mostraDettaglioFascicolo(String idFascicolo) {
		getPresenter().getPecInPraticheDB().getFascicoloByPath(idFascicolo, true, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {

				Place place = new Place();
				place.setToken(NameTokens.dettagliofascicolo);
				place.addParam(NameTokensParams.idPratica, fascicolo.getClientID());
				getPresenter()._getEventBus().fireEvent(new GoToPlaceEvent(place));
			}

			@Override
			public void onPraticaError(String error) {
				writeErrorMessage(new RuntimeException(error));
			}
		});
	}
}

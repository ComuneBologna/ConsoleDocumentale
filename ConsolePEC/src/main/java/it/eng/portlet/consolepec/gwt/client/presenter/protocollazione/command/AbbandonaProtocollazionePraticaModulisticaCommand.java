package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloActionResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AbbandonaProtocollazionePraticaModulisticaCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public AbbandonaProtocollazionePraticaModulisticaCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		ShowAppLoadingEvent.fire(getPresenter(), true);

		CreaFascicoloAction action = getPresenter().getCreaFascicoloDTO().getAction();

		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<CreaFascicoloActionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(CreaFascicoloActionResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);

				if (result.getError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getMessageError());
					getPresenter()._getEventBus().fireEvent(event);

				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setMessageDropped(true);
					getPresenter()._getEventBus().fireEvent(event);

					getPresenter().getPecInPraticheDB().remove(getPresenter().getIdPraticaModulistica());

					Place place = new Place();
					place.setToken(NameTokens.dettagliofascicolo);
					place.addParam(NameTokensParams.idPratica, result.getFascicoloDTO().getClientID());
					getPresenter()._getEventBus().fireEvent(new GoToPlaceEvent(place));
				}
			}
		});
	}

}
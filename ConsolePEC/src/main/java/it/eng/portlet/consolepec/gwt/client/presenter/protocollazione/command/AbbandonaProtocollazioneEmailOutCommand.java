package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmailOutLoaded;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InviaMailAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InviaMailActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AbbandonaProtocollazioneEmailOutCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public AbbandonaProtocollazioneEmailOutCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		PraticaEmailOutLoaded callback = new PraticaEmailOutLoaded() {
			@Override
			public void onPraticaLoaded(PecOutDTO pec) {
				if (!pec.isInviata()) {
					inviaMail();
				} else {
					Place place = new Place();
					place.setToken(NameTokens.dettagliopecout);
					place.addParam(NameTokensParams.idPratica, getPresenter().getIdEmailIn());
					getPresenter()._getEventBus().fireEvent(new GoToPlaceEvent(place));
				}
			}

			@Override
			public void onPraticaError(String error) {
				writeErrorMessage(new RuntimeException(error));
			}

		};
		getPresenter().getPecInPraticheDB().getPecOutByPath(getPresenter().getIdEmailOut(), getPresenter().getSitemapMenu().containsLink(getPresenter().getIdEmailOut()), callback);
	}

	private void inviaMail() {
		InviaMailAction action = new InviaMailAction(getPresenter().getIdEmailOut());

		ShowAppLoadingEvent.fire(getPresenter(), true);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);
		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<InviaMailActionResult>() {

			@Override
			public void onSuccess(InviaMailActionResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (!result.getError()) {
					getPresenter().getPecInPraticheDB().insertOrUpdate(getPresenter().getIdEmailOut(), result.getPecOutDTO(), true);
					Place place = new Place();
					place.setToken(NameTokens.dettagliopecout);
					place.addParam(NameTokensParams.idPratica, getPresenter().getIdEmailOut());
					getPresenter()._getEventBus().fireEvent(new GoToPlaceEvent(place));
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

}
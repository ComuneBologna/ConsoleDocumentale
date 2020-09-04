package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;

public class IndietroProtocollazioneEmailOutCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public IndietroProtocollazioneEmailOutCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		Place place = new Place();
		place.setToken(NameTokens.dettagliopecout);
		place.addParam(NameTokensParams.idPratica, getPresenter().getIdEmailOut());
		getPresenter()._getEventBus().fireEvent(new GoToPlaceEvent(place));

	}

}

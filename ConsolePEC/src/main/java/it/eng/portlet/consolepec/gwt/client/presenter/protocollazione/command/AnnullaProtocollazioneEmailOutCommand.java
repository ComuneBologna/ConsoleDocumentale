package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;

public class AnnullaProtocollazioneEmailOutCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public AnnullaProtocollazioneEmailOutCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		getPresenter()._getEventBus().fireEvent(new BackFromPlaceEvent(getPresenter().getIdEmailIn()));

	}

}

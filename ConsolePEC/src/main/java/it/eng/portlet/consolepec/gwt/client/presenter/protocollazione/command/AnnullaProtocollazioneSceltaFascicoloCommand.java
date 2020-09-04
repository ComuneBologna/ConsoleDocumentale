package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;

public class AnnullaProtocollazioneSceltaFascicoloCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public AnnullaProtocollazioneSceltaFascicoloCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		getPresenter()._getEventBus().fireEvent(new BackFromPlaceEvent());

	}

}

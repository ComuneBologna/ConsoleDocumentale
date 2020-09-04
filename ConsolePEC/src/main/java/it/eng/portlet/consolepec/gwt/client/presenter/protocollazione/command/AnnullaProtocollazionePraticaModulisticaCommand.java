package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;

public class AnnullaProtocollazionePraticaModulisticaCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public AnnullaProtocollazionePraticaModulisticaCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		getPresenter()._getEventBus().fireEvent(new BackFromPlaceEvent());
	}

}

package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.MostraSceltaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;

public class IndietroProtocollazioneSceltaFascicoloCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public IndietroProtocollazioneSceltaFascicoloCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		MostraSceltaFascicoloEvent event = new MostraSceltaFascicoloEvent();
		getPresenter()._getEventBus().fireEvent(event);

	}

}

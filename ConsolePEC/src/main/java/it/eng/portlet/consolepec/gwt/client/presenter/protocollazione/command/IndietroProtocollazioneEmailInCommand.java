package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.MostraDatiPraticaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;

public class IndietroProtocollazioneEmailInCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public IndietroProtocollazioneEmailInCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		MostraDatiPraticaEvent event = new MostraDatiPraticaEvent(getPresenter().getIdEmailIn());
		event.setSvuotaCampi(false);
		getPresenter()._getEventBus().fireEvent(event);

	}

}

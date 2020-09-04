package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEmailOutEvent;

public class ConfermaProtocollazioneEmailOutCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public ConfermaProtocollazioneEmailOutCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		MostraSceltaCapofilaEmailOutEvent event = new MostraSceltaCapofilaEmailOutEvent();
		event.setIdFascicolo(getPresenter().getIdFascicolo());
		event.setIdPecOut(getPresenter().getIdEmailOut());
		getPresenter()._getEventBus().fireEvent(event);

	}

}

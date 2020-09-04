package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEmailInEvent;

public class ConfermaProtocollazioneEmailInCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public ConfermaProtocollazioneEmailInCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		MostraSceltaCapofilaEmailInEvent eventWizard = new MostraSceltaCapofilaEmailInEvent();
		eventWizard.setCreaFascicoloDTO(getPresenter().getCreaFascicoloDTO());
		getPresenter()._getEventBus().fireEvent(eventWizard);

	}

}

package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.RilasciaInCaricoPecInEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;

public class AnnullaProtocollazioneEmailInCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public AnnullaProtocollazioneEmailInCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		RilasciaInCaricoPecInEvent event = new RilasciaInCaricoPecInEvent(getPresenter().getIdEmailIn());
		getPresenter()._getEventBus().fireEvent(event);

	}

}

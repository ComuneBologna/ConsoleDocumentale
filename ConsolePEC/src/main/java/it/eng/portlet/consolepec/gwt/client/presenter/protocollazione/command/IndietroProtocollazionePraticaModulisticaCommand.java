package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.MostraDatiPraticaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;

public class IndietroProtocollazionePraticaModulisticaCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public IndietroProtocollazionePraticaModulisticaCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		MostraDatiPraticaEvent event = new MostraDatiPraticaEvent(getPresenter().getIdPraticaModulistica());
		event.setSvuotaCampi(false);
		getPresenter()._getEventBus().fireEvent(event);

	}

}

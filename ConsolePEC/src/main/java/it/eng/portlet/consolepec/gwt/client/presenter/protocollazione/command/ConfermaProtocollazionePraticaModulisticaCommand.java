package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaPraticaModulisticaEvent;

public class ConfermaProtocollazionePraticaModulisticaCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public ConfermaProtocollazionePraticaModulisticaCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		MostraSceltaCapofilaPraticaModulisticaEvent eventWizard = new MostraSceltaCapofilaPraticaModulisticaEvent();
		eventWizard.setCreaFascicoloDTO(getPresenter().getCreaFascicoloDTO());
		getPresenter()._getEventBus().fireEvent(eventWizard);
	}

}

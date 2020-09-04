package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaSceltaFascicoloEvent;

public class ConfermaProtocollazioneSceltaFascicoloCommand extends AbstractConsolePecCommand<ConfermaSceltaProtocollazionePresenter> {

	public ConfermaProtocollazioneSceltaFascicoloCommand(ConfermaSceltaProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		MostraSceltaCapofilaSceltaFascicoloEvent event = new MostraSceltaCapofilaSceltaFascicoloEvent();
		event.setIdFascicolo(getPresenter().getIdFascicolo());
		event.setIdEmailIn(getPresenter().getIdEmailIn());
		event.setIdPraticaModulistica(getPresenter().getIdPraticaModulistica());
		getPresenter()._getEventBus().fireEvent(event);
	}

}

package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.MostraSceltaProtocollazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;

public class AggiornaPGCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	
	public AggiornaPGCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	
	@Override
	public void _execute() {
		
		getPresenter()._getEventBus().fireEvent(new MostraSceltaProtocollazioneEvent(getPresenter().getFascicoloPath()));
	}

	
}

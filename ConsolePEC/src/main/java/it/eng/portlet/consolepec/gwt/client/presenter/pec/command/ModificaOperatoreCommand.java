package it.eng.portlet.consolepec.gwt.client.presenter.pec.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.MostraSceltaOperatoreEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.DettaglioPecInPresenter;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class ModificaOperatoreCommand extends AbstractConsolePecCommand<DettaglioPecInPresenter> {
	
	public ModificaOperatoreCommand(DettaglioPecInPresenter presenter) {
		super(presenter);
	}
	
	@Override
	public void _execute() {
		PraticaDTO pratica = getPresenter().getPecInDTO(); 
		getPresenter()._getEventBus().fireEvent(new MostraSceltaOperatoreEvent(pratica));
	}

	

}

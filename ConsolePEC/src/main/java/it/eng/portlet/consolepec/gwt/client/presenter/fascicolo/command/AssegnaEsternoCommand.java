package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaEsternoFromDettaglioFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;

public class AssegnaEsternoCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public AssegnaEsternoCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	@Override
	public void _execute() {
		getPresenter()._getEventBus().fireEvent(new GoToAssegnaEsternoFromDettaglioFascicoloEvent(getPresenter().getFascicoloPath()));
	}
}

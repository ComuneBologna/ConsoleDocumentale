package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromDettaglioFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;

public class MostraGruppiCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public MostraGruppiCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	public void _execute() {
		GoToAssegnaFromDettaglioFascicoloEvent event = new GoToAssegnaFromDettaglioFascicoloEvent(getPresenter().getFascicoloPath());
		getPresenter()._getEventBus().fireEvent(event);
	}
}

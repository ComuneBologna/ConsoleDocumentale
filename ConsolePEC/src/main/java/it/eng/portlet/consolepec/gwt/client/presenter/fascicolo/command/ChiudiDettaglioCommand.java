package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;

public class ChiudiDettaglioCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public ChiudiDettaglioCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	@Override
	public void _execute() {
		getPresenter().getPecInPraticheDB().remove(getPresenter().getFascicoloPath());
		getPresenter()._getEventBus().fireEvent(new BackFromPlaceEvent(getPresenter().getFascicoloPath()));
	}
}

package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.SpostaAllegatiInizioEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;

public class SpostaAllegatiCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {

	public SpostaAllegatiCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		getPresenter()._getEventBus().fireEvent(new SpostaAllegatiInizioEvent(getPresenter().getFascicoloPath(), getPresenter().getView().getAllegatiSelezionati()));
	}

}

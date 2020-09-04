package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.SpostaProtocollazioniInizioEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;

public class SpostaProtocollazioniCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {

	public SpostaProtocollazioniCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {

		getPresenter()._getEventBus().fireEvent(
				new SpostaProtocollazioniInizioEvent(getPresenter().getFascicoloPath(), getPresenter().getView().getAllegatiProtSelezionati(), getPresenter().getView().getPraticheProtSelezionate()));
	}

}

package it.eng.portlet.consolepec.gwt.client.presenter.pec.command;

import it.eng.portlet.consolepec.gwt.client.presenter.pec.DettaglioPecInPresenter;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ElettoraleAction.OperazioneElettorale;

public class ImportaElettoraleCommand extends AbstractElettoraleCommand<DettaglioPecInPresenter> {

	public ImportaElettoraleCommand(DettaglioPecInPresenter presenter) {
		super(presenter);
	}

	@Override
	public OperazioneElettorale getOperazioneElettorale() {
		return OperazioneElettorale.IMPORTA;
	}
}

package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiAllegatiEmailEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.ImportaAllegatiEmailPresenter;

public class AnnullaImportaAllegatiCommand extends AbstractConsolePecCommand<ImportaAllegatiEmailPresenter> {

	public AnnullaImportaAllegatiCommand(ImportaAllegatiEmailPresenter importaAllegatiEmailPresenter) {
		super(importaAllegatiEmailPresenter);
	}

	@Override
	protected void _execute() {
		getPresenter().getView().restSelezioni();
		getPresenter()._getEventBus().fireEvent(new ChiudiAllegatiEmailEvent());

	}

}

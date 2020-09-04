package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;

public class ModificaAbilitazioniAssegnaEsternoCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public ModificaAbilitazioniAssegnaEsternoCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	public void _execute() {
		getPresenter()._getEventBus().fireEvent(new GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloEvent(getPresenter().getFascicoloPath()));
	}
}
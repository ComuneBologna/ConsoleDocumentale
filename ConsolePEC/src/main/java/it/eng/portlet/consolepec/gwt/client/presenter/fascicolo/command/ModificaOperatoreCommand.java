package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.MostraSceltaOperatoreEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class ModificaOperatoreCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	
	public ModificaOperatoreCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	private PraticaDTO pratica;

	@Override
	public void _execute() {
		getPresenter()._getEventBus().fireEvent(new MostraSceltaOperatoreEvent(pratica));
	}

	public void setPratica(PraticaDTO pratica) {
		this.pratica = pratica;
	}

}

package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.MostraModificaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;

/**
 * @author GiacomoFM
 * @since 17/lug/2017
 */
public class ModificaFascicoloCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {

	public ModificaFascicoloCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		getPresenter()._getEventBus().fireEvent(new MostraModificaFascicoloEvent(getPresenter().getFascicoloPath()));
	}
	
}

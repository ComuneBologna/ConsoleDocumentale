package it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.urbanistica.MostraListaPraticaProcediEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.ListaPraticaProcediPresenter;

/**
 * @author GiacomoFM
 * @since 08/nov/2017
 */
public class ListaPraticaProcediCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	
	public ListaPraticaProcediCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		getPresenter()._getEventBus().fireEvent(new MostraListaPraticaProcediEvent(getPresenter().getFascicoloPath(),ListaPraticaProcediPresenter.class.getName()));
	}

}

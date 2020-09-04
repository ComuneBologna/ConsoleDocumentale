package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.MostraAllegatiEmailEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

import java.util.List;

public class ImportaAllegatiModuloCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public ImportaAllegatiModuloCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	@Override
	public void _execute() {
		List<PecInDTO> lista = getPresenter().getView().getEmailConAllegati();
		MostraAllegatiEmailEvent event = new MostraAllegatiEmailEvent(lista, getPresenter().getFascicoloPath());
		getPresenter()._getEventBus().fireEvent(event);
	}

}

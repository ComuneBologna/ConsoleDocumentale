package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.firmadigitale.FirmaDigitaleInizioEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import java.util.Set;

public class FirmaAllegatoCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public FirmaAllegatoCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	public void _execute() {
		Set<AllegatoDTO> allegati = getPresenter().getView().getAllegatiSelezionati();
		FirmaDigitaleInizioEvent event = new FirmaDigitaleInizioEvent(getPresenter(), allegati);
		getPresenter()._getEventBus().fireEvent(event);
	}
}

package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;

public class NuovaMailInteroperabileCommand extends AbstractNuovaMailCommand {
	public NuovaMailInteroperabileCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	@Override
	public boolean isInteroperabile() {
		return true;
	}

}

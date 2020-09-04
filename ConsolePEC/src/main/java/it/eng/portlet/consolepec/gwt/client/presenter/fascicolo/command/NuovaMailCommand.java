package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;

public class NuovaMailCommand extends AbstractNuovaMailCommand {
	
	public NuovaMailCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	@Override
	public boolean isInteroperabile() {
		return false;
	}
}

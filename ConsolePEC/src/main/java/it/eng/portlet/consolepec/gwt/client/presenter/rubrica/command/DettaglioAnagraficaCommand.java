package it.eng.portlet.consolepec.gwt.client.presenter.rubrica.command;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraDettaglioAnagraficaEvent;

import com.google.gwt.user.client.Command;
import com.gwtplatform.mvp.client.Presenter;

/**
 * @author GiacomoFM
 * @since 15/set/2017
 */
public class DettaglioAnagraficaCommand implements Command {

	private Anagrafica anagrafica;

	private Presenter<?,?> presenter;

	public DettaglioAnagraficaCommand(Presenter<?,?> presenter) {
		this.presenter = presenter;
	}

	public void setAnagrafica(Anagrafica anagrafica) {
		this.anagrafica = anagrafica;
	}

	@Override
	public void execute() {
		if (this.presenter == null) throw new IllegalStateException("Presenter cannot be null in the command");
		presenter.fireEvent(new MostraDettaglioAnagraficaEvent(anagrafica,presenter));
	}

}

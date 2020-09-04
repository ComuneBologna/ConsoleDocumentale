package it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.command;

import it.eng.cobo.consolepec.commons.urbanistica.AllegatoProcedi;
import it.eng.portlet.consolepec.gwt.client.presenter.event.urbanistica.MostraDettaglioAllegatoProcediEvent;
import lombok.AllArgsConstructor;

import com.google.gwt.user.client.Command;
import com.gwtplatform.mvp.client.Presenter;

@AllArgsConstructor
public class DettaglioAllegatoProcediCommand implements Command {

	private Presenter<?, ?> presenter;
	private String idPraticaProcedi;
	private AllegatoProcedi allegatoProcedi;

	@Override
	public void execute() {
		if (this.presenter == null) throw new IllegalStateException("Presenter cannot be null in the command");
		presenter.fireEvent(new MostraDettaglioAllegatoProcediEvent(idPraticaProcedi, allegatoProcedi));
	}

}

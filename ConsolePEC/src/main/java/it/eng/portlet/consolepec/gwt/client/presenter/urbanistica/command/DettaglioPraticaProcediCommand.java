package it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.command;

import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import lombok.Setter;

import com.google.gwt.user.client.Command;
import com.gwtplatform.mvp.client.Presenter;

/**
 * @author GiacomoFM
 * @since 07/nov/2017
 */
public class DettaglioPraticaProcediCommand implements Command {

	@Setter private String id;
	@Setter private String className;

	private Presenter<?, ?> presenter;

	public DettaglioPraticaProcediCommand(Presenter<?, ?> presenter) {
		super();
		this.presenter = presenter;
	}

	@Override
	public void execute() {
		if (this.presenter == null) throw new IllegalStateException("Presenter cannot be null in the command");
		Place place = new Place();
		place.setToken(NameTokens.praticaprocedi);
		place.addParam(NameTokensParams.idPratica, id);
		place.addParam(NameTokensParams.nomeClasseDiRitorno, className);
		presenter.fireEvent(new GoToPlaceEvent(place));
	}

}

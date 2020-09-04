package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;

public class CollegaFascicoloCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {

	public CollegaFascicoloCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	@Override
	public void _execute() {
		Place place = new Place();
		place.setToken(NameTokens.sceltafascicolo);
		place.addParam(NameTokensParams.idPratica, getPresenter().getFascicoloPath());
		getPresenter()._getEventBus().fireEvent(new GoToPlaceEvent(place));
	}

}

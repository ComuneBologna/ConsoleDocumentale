package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;

public class GoToDettaglioModuloCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public GoToDettaglioModuloCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	private String praticaModulisticaPath;

	@Override
	public void _execute() {
		Place place = new Place();
		place.setToken(NameTokens.dettagliopraticamodulistica);
		place.addParam(NameTokensParams.idPratica, praticaModulisticaPath);
		getPresenter()._getEventBus().fireEvent(new GoToPlaceEvent(place));
	}

	public void setPraticaModulisticaPath(String praticaModulisticaPath) {
		this.praticaModulisticaPath = praticaModulisticaPath;
	}

}

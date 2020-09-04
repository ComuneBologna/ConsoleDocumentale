package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.ModificaVisibilitaAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class ModificaVisibilitaAllegatiCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public ModificaVisibilitaAllegatiCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	@Override
	public void _execute() {
		final AllegatoDTO allegatoDTO = getPresenter().getView().getAllegatiSelezionati().iterator().next();

		getPresenter().getPecInPraticheDB().getFascicoloByPath(getPresenter().getFascicoloPath(), true, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				getPresenter()._getEventBus().fireEvent(new ModificaVisibilitaAllegatoEvent(allegatoDTO, fascicolo));
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}
		});
	}

}

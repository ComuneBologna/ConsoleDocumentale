package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class AnnullaSalvaFascicoloCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public AnnullaSalvaFascicoloCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	public void _execute() {
		getPresenter().getPecInPraticheDB().getFascicoloByPath(getPresenter().getFascicoloPath(), getPresenter().getSitemapMenu().containsLink(getPresenter().getFascicoloPath()),
				new PraticaFascicoloLoaded() {

					@Override
					public void onPraticaLoaded(FascicoloDTO fascicolo) {
						getPresenter().getView().mostraPratica(fascicolo);
					}

					@Override
					public void onPraticaError(String error) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getPresenter()._getEventBus().fireEvent(event);
					}
				});
	}
}

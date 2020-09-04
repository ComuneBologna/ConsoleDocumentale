package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import com.google.gwt.user.client.rpc.AsyncCallback;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.GestionePresaInCaricoFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.GestionePresaInCaricoFascicoloActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class GestioneInCaricoCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public GestioneInCaricoCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	public void _execute() {
		GestionePresaInCaricoFascicoloAction action = new GestionePresaInCaricoFascicoloAction();
		action.setClientID(getPresenter().getFascicoloPath());
		ShowAppLoadingEvent.fire(getPresenter(), true);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);
		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<GestionePresaInCaricoFascicoloActionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(GestionePresaInCaricoFascicoloActionResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (!result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setMessageDropped(true);
					getPresenter()._getEventBus().fireEvent(event);

					getPresenter().getPecInPraticheDB().insertOrUpdate(result.getPraticaDTO().getClientID(), result.getPraticaDTO(),
							getPresenter().getSitemapMenu().containsLink(result.getPraticaDTO().getClientID()));

					getPresenter().getView().mostraPratica((FascicoloDTO) result.getPraticaDTO());
				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					getPresenter()._getEventBus().fireEvent(event);
				}
			}

		});
	}

}

package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import com.google.gwt.user.client.rpc.AsyncCallback;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.UpdateSiteMapEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.RiportaInLettura;
import it.eng.portlet.consolepec.gwt.shared.action.RiportaInLetturaResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class RiportaInLetturaCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {

	public RiportaInLetturaCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		ShowAppLoadingEvent.fire(getPresenter(), true);

		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);

		RiportaInLettura action = new RiportaInLettura(getPresenter().getFascicoloPath());

		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<RiportaInLetturaResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(RiportaInLetturaResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (result.getError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getMessageError());
					getPresenter()._getEventBus().fireEvent(event);
				} else {
					FascicoloDTO fascicoloRes = result.getFascicolo();
					getPresenter().getPecInPraticheDB().remove(fascicoloRes.getClientID());
					getPresenter().getView().mostraPratica(fascicoloRes);
					getPresenter()._getEventBus().fireEvent(new UpdateSiteMapEvent());
				}
			}
		});
	}

}

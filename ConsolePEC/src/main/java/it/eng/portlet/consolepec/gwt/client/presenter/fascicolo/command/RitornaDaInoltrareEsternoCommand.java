package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import com.google.gwt.user.client.rpc.AsyncCallback;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.RitornaDaInoltrareEsternoAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.RitornaDaInoltrareEsternoResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class RitornaDaInoltrareEsternoCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {

	public RitornaDaInoltrareEsternoCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {

		ShowAppLoadingEvent.fire(getPresenter(), true);

		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);

		RitornaDaInoltrareEsternoAction action = new RitornaDaInoltrareEsternoAction(getPresenter().getFascicoloPath());

		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<RitornaDaInoltrareEsternoResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(RitornaDaInoltrareEsternoResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getMessageError());
					getPresenter()._getEventBus().fireEvent(event);
				} else {
					FascicoloDTO fascicoloRes = (FascicoloDTO) result.getPratica();
					getPresenter().getPecInPraticheDB().remove(fascicoloRes.getClientID());
					getPresenter().getView().mostraPratica(fascicoloRes);
				}
			}
		});
	}

}

package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.AvvioChiusuraProcedimentiPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.RiepilogoAvvioChiusuraProcedimentoEvent;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.AvvioProcedimento;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.AvvioProcedimentoResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AvviaProcedimentoCommand extends AbstractConsolePecCommand<AvvioChiusuraProcedimentiPresenter> {

	public AvviaProcedimentoCommand(AvvioChiusuraProcedimentiPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);

		if (!getPresenter().checkInput())
			return;

		ShowAppLoadingEvent.fire(getPresenter(), true);

		AvvioProcedimento action = getPresenter().getAvvioProcedimentoAction();

		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<AvvioProcedimentoResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(AvvioProcedimentoResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					getPresenter()._getEventBus().fireEvent(event);
				} else {
					// rimuovo la pratica dalla cache locale per farla
					// ricaricare dopo le modifiche
					getPresenter().getPecInPraticheDB().remove(result.getFascicoloPath());
					getPresenter()._getEventBus().fireEvent(new RiepilogoAvvioChiusuraProcedimentoEvent(result, result.getFascicoloPath()));
				}
			}
		});

	}

}

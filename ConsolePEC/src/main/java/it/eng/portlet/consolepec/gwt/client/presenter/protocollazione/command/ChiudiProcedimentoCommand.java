package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.AvvioChiusuraProcedimentiPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.RiepilogoAvvioChiusuraProcedimentoEvent;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.ChiusuraProcedimentoAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.ChiusuraProcedimentoResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChiudiProcedimentoCommand extends AbstractConsolePecCommand<AvvioChiusuraProcedimentiPresenter> {

	public ChiudiProcedimentoCommand(AvvioChiusuraProcedimentiPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);
		ShowAppLoadingEvent.fire(getPresenter(), true);

		ChiusuraProcedimentoAction action = getPresenter().getChiusuraProcedimentoAction();

		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<ChiusuraProcedimentoResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(ChiusuraProcedimentoResult result) {
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

package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import com.google.gwt.user.client.rpc.AsyncCallback;

import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ValidazioneDatiAggiuntivi;
import it.eng.portlet.consolepec.gwt.shared.action.ValidazioneDatiAggiuntiviResult;

public class SalvaFascicoloCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public SalvaFascicoloCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	public void _execute() {

		ShowAppLoadingEvent.fire(getPresenter(), true);

		ValidazioneDatiAggiuntivi validazioneDatiAggiuntivi = new ValidazioneDatiAggiuntivi(getPresenter().getView().getDatiAggiuntivi());

		getPresenter().getDispatchAsync().execute(validazioneDatiAggiuntivi, new AsyncCallback<ValidazioneDatiAggiuntiviResult>() {

			@Override
			public void onFailure(Throwable error) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(ValidazioneDatiAggiuntiviResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);

				if (result.getError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getMessError());
					getPresenter()._getEventBus().fireEvent(event);

				} else if (!result.getErroriDaVisualizzare().isEmpty()) {
					getPresenter().getView().controlloServerDatiAggiuntivi(result.getValidazioneDatiAggiuntivi());
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(GenericsUtil.format(result.getErroriDaVisualizzare()));
					getPresenter()._getEventBus().fireEvent(event);

				} else if (getPresenter().getView().controlloServerDatiAggiuntivi(result.getValidazioneDatiAggiuntivi())) {
					getPresenter().salvaFascicolo(null);
				}

			}

		});

	}
}

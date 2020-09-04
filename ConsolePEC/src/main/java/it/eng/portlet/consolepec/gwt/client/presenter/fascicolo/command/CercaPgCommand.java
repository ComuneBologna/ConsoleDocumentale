package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import com.google.gwt.user.client.rpc.AsyncCallback;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ComposizioneFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.ComposizioneFascicoloResult;

public class CercaPgCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {

	public CercaPgCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		String numPG = getPresenter().getView().getSearchPGParams().getNumero();
		Integer annoPG = Integer.parseInt(getPresenter().getView().getSearchPGParams().getAnno());
		ComposizioneFascicolo action = new ComposizioneFascicolo(numPG, annoPG);

		ShowMessageEvent msgEvent = new ShowMessageEvent();
		msgEvent.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(msgEvent);
		ShowAppLoadingEvent.fire(getPresenter(), true);

		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<ComposizioneFascicoloResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(ComposizioneFascicoloResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					getPresenter()._getEventBus().fireEvent(event);
				} else {
					getPresenter().getView().showCatenaDocumentale(result.getCatenaDocumentaleDTO());
				}
			}
		});
	}

}

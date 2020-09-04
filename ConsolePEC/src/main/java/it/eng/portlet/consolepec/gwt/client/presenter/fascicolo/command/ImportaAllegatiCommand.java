package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiAllegatiEmailEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.ImportaAllegatiEmailPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ImportaAllegatiCommand extends AbstractConsolePecCommand<ImportaAllegatiEmailPresenter> {

	public ImportaAllegatiCommand(ImportaAllegatiEmailPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		ShowAppLoadingEvent.fire(getPresenter(), true);
		Map<String, List<AllegatoDTO>> allegatiSelezionati = getPresenter().getView().getAllegatiEmailSelezionati();
		UploadAllegatoPraticaAction action = new UploadAllegatoPraticaAction(allegatiSelezionati, getPresenter().getClientID());
		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<UploadAllegatoPraticaResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);

			}

			@Override
			public void onSuccess(UploadAllegatoPraticaResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (!result.getError()) {
					PraticaDTO pratica = result.getPratica();
					ChiudiAllegatiEmailEvent event = new ChiudiAllegatiEmailEvent();
					event.setFascicolo((FascicoloDTO) pratica);
					getPresenter()._getEventBus().fireEvent(event);
				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getPresenter()._getEventBus().fireEvent(event);
				}

			}
		});
	}

}

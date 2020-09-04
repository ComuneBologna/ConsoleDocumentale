package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import java.util.Set;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.rpc.AsyncCallback;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.ScaricaAllegatiMultipli;
import it.eng.portlet.consolepec.gwt.shared.action.ScaricaAllegatiMultipliResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

public class ScaricaAllegatiCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public ScaricaAllegatiCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	public void _execute() {
		ShowAppLoadingEvent.fire(getPresenter(), true);
		Set<AllegatoDTO> selez = getPresenter().getView().getAllegatiSelezionati();
		ScaricaAllegatiMultipli action = new ScaricaAllegatiMultipli(getPresenter().getFascicoloPath(), selez);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);
		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<ScaricaAllegatiMultipliResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(ScaricaAllegatiMultipliResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (!result.isError()) {
					SafeUri uri = UriMapping.generaDownloadAllegatiZippati(result.getName(), result.getDir());
					getPresenter().getView().sendDownload(uri);
					getPresenter().getView().mostraPratica(getPresenter().getView().getFascicolo()); // ricarico il fascicolo se no mi restano gli allegati selezionati
				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					getPresenter()._getEventBus().fireEvent(event);
				}
			}
		});
	}

}

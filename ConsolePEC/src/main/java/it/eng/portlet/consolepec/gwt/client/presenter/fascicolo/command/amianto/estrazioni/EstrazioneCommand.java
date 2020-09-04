package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.amianto.estrazioni;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.amianto.EstrazioniAmiantoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.amianto.EstrazioneAmianto;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.amianto.EstrazioneAmiantoResult;
import it.eng.portlet.consolepec.gwt.shared.model.amianto.EstrazioneAmiantoDTO;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EstrazioneCommand extends AbstractConsolePecCommand<EstrazioniAmiantoPresenter> {

	public EstrazioneCommand(EstrazioniAmiantoPresenter presenter) {
		super(presenter);
	}
	
	@Override
	public void _execute() {
		boolean condizioneCampiObbligatori = getPresenter().getView().controllaCampiObbligatori();
		if (!condizioneCampiObbligatori) {
			
			StringBuilder messaggioIntestazione = new StringBuilder("<br/>I campi in rosso devono essere valorizzati correttamente:<br/>");
			ShowMessageEvent event = new ShowMessageEvent();
			event.setWarningMessage(messaggioIntestazione.toString());
			getPresenter()._getEventBus().fireEvent(event);
		} else {
			estrai();
		}
	}

	private void estrai() {
		EstrazioneAmiantoDTO dto = getPresenter().getView().getEstrazioneAmiantoDTO();
		EstrazioneAmianto action = new EstrazioneAmianto(dto.getInizio(), dto.getFine());

		ShowAppLoadingEvent.fire(getPresenter(), true);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);
		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<EstrazioneAmiantoResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(EstrazioneAmiantoResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (result.getError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getMessageError());
					getPresenter()._getEventBus().fireEvent(event);
					return;
				}
				SafeUri uri = UriMapping.generaDownloadEstrazioniAmianto(result.getFileName(), result.getFileDir());
				getPresenter().getView().sendDownload(uri);
			}
		});
	}
	

}

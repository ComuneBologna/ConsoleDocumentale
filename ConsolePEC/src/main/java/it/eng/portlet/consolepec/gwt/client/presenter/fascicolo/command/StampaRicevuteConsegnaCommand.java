package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.StampaRicevuteConsegna;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.StampaRicevuteConsegnaResult;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class StampaRicevuteConsegnaCommand<T extends IStampaRicevute> extends AbstractConsolePecCommand<T> {

	public StampaRicevuteConsegnaCommand(T presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		ShowAppLoadingEvent.fire(getPresenter(), true);
		String praticaPath = getPresenter().getPecOutPath(); 
		StampaRicevuteConsegna action = new StampaRicevuteConsegna(praticaPath);
		if(action.getPraticaPath() != null){
			getPresenter().getDispatchAsync().execute(action, new AsyncCallback<StampaRicevuteConsegnaResult>() {
	
				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(getPresenter(), false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getPresenter()._getEventBus().fireEvent(event);
				}
	
				@Override
				public void onSuccess(StampaRicevuteConsegnaResult result) {
					ShowAppLoadingEvent.fire(getPresenter(), false);
					if (!result.isError()) {
					
						SafeUri uri = UriMapping.generaDownloadAllegatiZippati(result.getFileName(), result.getFileDir());
						getPresenter().downloadStampa(uri);
	
					} else {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getPresenter()._getEventBus().fireEvent(event);
					}
				}
			});
		} else {
			ShowAppLoadingEvent.fire(getPresenter(), false);
			ShowMessageEvent event = new ShowMessageEvent();
			event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			getPresenter()._getEventBus().fireEvent(event);
		}
		
	}

}

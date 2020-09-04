package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.RiversamentoCartaceo;
import it.eng.portlet.consolepec.gwt.shared.action.RiversamentoCartaceoResult;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractRiversamentoCartaceoCommand<T extends IStampa> extends AbstractConsolePecCommand<T> {

	public AbstractRiversamentoCartaceoCommand(T presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		ShowAppLoadingEvent.fire(getPresenter(), true);
		RiversamentoCartaceo action = getRiversamentoCartaceoAction();
		if(action != null && action.getFascicoloPath() != null && action.getAnnoPG() != null && action.getNumeroPG() != null){
			getPresenter().getDispatchAsync().execute(action, new AsyncCallback<RiversamentoCartaceoResult>() {
	
				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(getPresenter(), false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getPresenter()._getEventBus().fireEvent(event);
				}
	
				@Override
				public void onSuccess(RiversamentoCartaceoResult result) {
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

	public abstract RiversamentoCartaceo getRiversamentoCartaceoAction();
	
//	public interface IRiversamentoCartaceo extends ConsolePecCommandBinder {
//		
//		public void downloadRiversamentoCartaceo(SafeUri uri);
//
//	}

}

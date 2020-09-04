package it.eng.portlet.consolepec.gwt.client.presenter.pec.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.UpdateSiteMapEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.DettaglioPecInPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ElettoraleAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ElettoraleAction.OperazioneElettorale;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ElettoraleResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractElettoraleCommand<T extends DettaglioPecInPresenter> extends AbstractConsolePecCommand<T>{
	
	public AbstractElettoraleCommand(T presenter) {
		super(presenter);
	}

	public abstract OperazioneElettorale getOperazioneElettorale();
	
	@Override
	protected void _execute() {
		
		ElettoraleAction action = new ElettoraleAction(getPresenter().getPecInPath(), getOperazioneElettorale());
		
		ShowMessageEvent msgEvent = new ShowMessageEvent();
		msgEvent.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(msgEvent);
		ShowAppLoadingEvent.fire(getPresenter(), true);
		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<ElettoraleResult>(){

			@Override
			public void onFailure(Throwable arg0) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(ElettoraleResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if(result.isError()){
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					getPresenter()._getEventBus().fireEvent(event);
				} else {
					PecInDTO pecRes = result.getDTO();
					getPresenter().getPecInPraticheDB().remove(pecRes.getClientID());
					for(String clientID : result.getClientIDPraticheCollegate())
						getPresenter().getPecInPraticheDB().remove(clientID);
					getPresenter().mostraPratica();
					getPresenter()._getEventBus().fireEvent(new UpdateSiteMapEvent());
				}
			}
			
		});
	}

}

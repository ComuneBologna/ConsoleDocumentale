package it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.DettaglioComunicazionePresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.NuovoInvioComunicazioneAction;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.NuovoInvioComunicazioneActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class InviaCsvCommand extends AbstractConsolePecCommand<DettaglioComunicazionePresenter> {

	public InviaCsvCommand(DettaglioComunicazionePresenter presenter) {
		super(presenter);
	}

	private AllegatoDTO allegato;

	@Override
	protected void _execute() {
		NuovoInvioComunicazioneAction action = new NuovoInvioComunicazioneAction(getPresenter().getComunicazione(), allegato);
		
		ShowAppLoadingEvent.fire(getPresenter(), true);
		
		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<NuovoInvioComunicazioneActionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			
			@Override
			public void onSuccess(NuovoInvioComunicazioneActionResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (!result.getError()) {
					getPresenter().getPecInPraticheDB().update(result.getComunicazione().getClientID(), result.getComunicazione(), getPresenter().getSitemapMenu().containsLink(result.getComunicazione().getClientID()));
					getPresenter().getPlaceManager().revealCurrentPlace();
				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getMessError());
					getPresenter()._getEventBus().fireEvent(event);
				}
			}

		});
	}

	public void setAllegato(AllegatoDTO allegato) {
		this.allegato = allegato;
	}

}
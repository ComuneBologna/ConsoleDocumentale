package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import com.google.gwt.user.client.rpc.AsyncCallback;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.RimozionePubblicazioneAllegati;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.RimozionePubblicazioneAllegatiResult;

public class RimuoviPubblicazioneAllegatiCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {

	public RimuoviPubblicazioneAllegatiCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	public void _execute() {
		// il controllo che getAllegatiSelezionati().size() ==1 è presente anche nella view per l'abilitazione del pulsante
		ShowAppLoadingEvent.fire(getPresenter(), true);
		RimozionePubblicazioneAllegati action = new RimozionePubblicazioneAllegati(getPresenter().getFascicoloPath(), getPresenter().getView().getAllegatiSelezionati().iterator().next().getNome());
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);
		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<RimozionePubblicazioneAllegatiResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(RimozionePubblicazioneAllegatiResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (!result.isError()) {
					// praticheDB.remove(result.getFascicolo().getClientID());
					getPresenter().getPecInPraticheDB().insertOrUpdate(getPresenter().getFascicoloPath(), result.getFascicolo(),
							getPresenter().getSitemapMenu().containsLink(getPresenter().getFascicoloPath()));
					getPresenter().getView().mostraPratica(result.getFascicolo());
				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					getPresenter()._getEventBus().fireEvent(event);
				}
			}

		});
	}

}

package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CancellaAllegatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CancellaAllegatoFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

public class EliminaAllegatoCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public EliminaAllegatoCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	public void _execute() {
		ShowAppLoadingEvent.fire(getPresenter(), true);
		Set<AllegatoDTO> selez = getPresenter().getView().getAllegatiSelezionati();
		CancellaAllegatoFascicolo action = new CancellaAllegatoFascicolo(getPresenter().getFascicoloPath());
		action.setAllegati(selez);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);
		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<CancellaAllegatoFascicoloResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(CancellaAllegatoFascicoloResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (!result.isError()) {
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

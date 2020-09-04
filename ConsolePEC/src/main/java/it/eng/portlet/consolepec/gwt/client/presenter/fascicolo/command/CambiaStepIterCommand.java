package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import com.google.gwt.user.client.rpc.AsyncCallback;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.StepIter;
import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToConfermaMailDaTemplateEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStepIter;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStepIterResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class CambiaStepIterCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {

	private StepIter stepIterDto;

	public CambiaStepIterCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	public void setStepIterDto(StepIter stepIterDto) {
		this.stepIterDto = stepIterDto;
	}

	@Override
	protected void _execute() {
		ShowAppLoadingEvent.fire(getPresenter(), true);

		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);

		CambiaStepIter action = new CambiaStepIter(getPresenter().getFascicoloPath(), stepIterDto);

		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<CambiaStepIterResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(CambiaStepIterResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getMessageError());
					getPresenter()._getEventBus().fireEvent(event);
				} else {
					FascicoloDTO fascicoloRes = result.getFascicolo();
					getPresenter().getPecInPraticheDB().remove(fascicoloRes.getClientID());

					if (fascicoloRes.getStepIter().isCreaBozza()) {
						GoToConfermaMailDaTemplateEvent event = new GoToConfermaMailDaTemplateEvent(fascicoloRes.getClientID());
						getPresenter()._getEventBus().fireEvent(event);

					} else {
						getPresenter().getView().mostraPratica(fascicoloRes);
					}
				}
			}
		});
	}

}

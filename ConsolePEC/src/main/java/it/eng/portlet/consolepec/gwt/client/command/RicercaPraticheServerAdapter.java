package it.eng.portlet.consolepec.gwt.client.command;

import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.RicercaCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPraticheResult;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;

public class RicercaPraticheServerAdapter implements WorklistStrategy.RicercaEventListener, HasHandlers {

	private final DispatchAsync dispatcher;
	private final EventBus eventBus;

	@Inject
	public RicercaPraticheServerAdapter(DispatchAsync dispatcher, EventBus eventbus) {
		this.dispatcher = dispatcher;
		this.eventBus = eventbus;
	}

	public void startRicerca(CercaPratiche action, final RicercaCallback callback) {
		action.setCount(false);
		/* applico splash screen */
		ShowAppLoadingEvent.fire(RicercaPraticheServerAdapter.this, true);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
		dispatcher.execute(action, new AsyncCallback<CercaPraticheResult>() {

			@Override
			public void onSuccess(CercaPraticheResult res) {
				if (res.getError()) {
					ShowAppLoadingEvent.fire(RicercaPraticheServerAdapter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(res.getMessError());
					eventBus.fireEvent(event);
				} else {
					List<PraticaDTO> pratiche = res.getPratiche();
					callback.setRisultati(pratiche);
					ShowAppLoadingEvent.fire(RicercaPraticheServerAdapter.this, false);
				}

			}

			@Override
			public void onFailure(Throwable arg0) {
				ShowAppLoadingEvent.fire(RicercaPraticheServerAdapter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
				callback.setNoResult();
			}
		});
		if (action.getInizio() == 0) {
			action.setCount(true);
			dispatcher.execute(action, new AsyncCallback<CercaPraticheResult>() {

				@Override
				public void onSuccess(CercaPraticheResult res) {
					if (res.getError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(res.getMessError());
						eventBus.fireEvent(event);
					} else {
						int count = res.getMaxResult();
						boolean estimate = res.getEstimate();
						callback.setCount(count, estimate);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(RicercaPraticheServerAdapter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
					callback.setNoResult();
				}
			});
		}
	}

	@Override
	public void onStartRicerca(int start, int length, ColonnaWorklist campoOrdinamento, boolean asc, final RicercaCallback callback) {
		CercaPratiche action = new CercaPratiche();
		action.setFine(start + length);
		action.setInizio(start);
		action.setCampoOrdinamento(campoOrdinamento);
		action.setOrdinamentoAsc(asc);
		// action.setSoloWorklist(soloWorklist);
		/**
		 * TODO Implementare un meccanismo di coerenza con la cache lato server. inoltre svuotare sono le righe selezionate, non tutto
		 */
		// RicercaLiberaPresenter.this.pecInDb.flush();

	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEventFromSource(event, this);
	}
}
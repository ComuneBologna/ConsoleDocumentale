package it.eng.portlet.consolepec.gwt.client.handler.profilazione;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Counter;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Tipo;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler.ProfilazioneUtenteCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaWorklistAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaWorklistResult;
import lombok.Getter;

public class WorklistHandler {

	private Map<AnagraficaWorklist, Counter> worklistAbilitate = new LinkedHashMap<AnagraficaWorklist, Counter>();

	protected Map<AnagraficaWorklist, Counter> getWorklistAbilitate() {
		return new LinkedHashMap<AnagraficaWorklist, Counter>(worklistAbilitate);
	}

	@Getter
	private AnagraficaWorklist defaultWorklist;

	private DispatchAsync dispatchAsync;

	@Inject
	public WorklistHandler(DispatchAsync dispatchAsync) {
		super();
		this.dispatchAsync = dispatchAsync;
	}

	protected void caricaWorklist(final ProfilazioneUtenteCallback callback) {
		caricaWorklist(false, new WorklistHandlerCallback() {

			@Override
			public void onSuccess(Map<AnagraficaWorklist, Counter> worklist) {
				callback.onSuccess();
			}

			@Override
			public void onFailure(String error) {
				callback.onFailure(error);
			}
		});
	}

	public void caricaWorklist(boolean reload, final WorklistHandlerCallback callback) {

		try {

			if (reload || worklistAbilitate.isEmpty()) {
				dispatchAsync.execute(new CaricaWorklistAction(reload), new AsyncCallback<CaricaWorklistResult>() {

					@Override
					public void onFailure(Throwable arg0) {
						callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);
					}

					@Override
					public void onSuccess(CaricaWorklistResult result) {
						worklistAbilitate = result.getWorklist();
						callback.onSuccess(getWorklist());
					}

				});

			} else {
				callback.onSuccess(getWorklist());
			}

		} catch (Exception e) {
			callback.onFailure(e.getLocalizedMessage());
		}
	}

	private Map<AnagraficaWorklist, Counter> getWorklist() {

		for (AnagraficaWorklist aw : worklistAbilitate.keySet()) {
			if (aw.getTipo().equals(Tipo.WORKLIST_ATTIVITA)) {
				defaultWorklist = aw;
				break;
			}
		}

		if (defaultWorklist == null && !worklistAbilitate.isEmpty()) {
			defaultWorklist = worklistAbilitate.keySet().iterator().next();
		}

		return worklistAbilitate;
	}

	public static abstract class WorklistHandlerCallback {
		public abstract void onSuccess(Map<AnagraficaWorklist, Counter> worklist);

		public abstract void onFailure(String error);
	}
}

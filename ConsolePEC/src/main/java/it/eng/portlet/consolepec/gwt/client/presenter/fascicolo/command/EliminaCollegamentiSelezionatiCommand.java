package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import java.util.ArrayList;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.collegamenti.Permessi;
import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;
import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoli;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoliMultiplo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoliMultiploResult;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.EliminaCollegaPraticaProcediAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.EliminaCollegaPraticaProcediResult;
import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class EliminaCollegamentiSelezionatiCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {

	public EliminaCollegamentiSelezionatiCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	public void _execute() {
		Set<CollegamentoDto> collegamentiSelezionati = getPresenter().getView().getCollegamentiSelezionati();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);

		CollegamentoFascicoliMultiplo collegamentoFascicoliMultiplo = new CollegamentoFascicoliMultiplo();

		for (CollegamentoDto collegamentoDto : collegamentiSelezionati) {
			collegamentoFascicoliMultiplo.getCollegamentoFascicoliList().add(
					new CollegamentoFascicoli(CollegamentoFascicoli.LINK_DELETE, collegamentoDto.getClientId(), getPresenter().getFascicoloPath(), new Permessi()));
		}

		ShowAppLoadingEvent.fire(getPresenter(), true);

		if (!collegamentiSelezionati.isEmpty()) {
			getPresenter().getDispatchAsync().execute(collegamentoFascicoliMultiplo, new AsyncCallback<CollegamentoFascicoliMultiploResult>() {

				@Override
				public void onFailure(Throwable arg0) {
					ShowAppLoadingEvent.fire(getPresenter(), false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getPresenter()._getEventBus().fireEvent(event);
				}

				@Override
				public void onSuccess(CollegamentoFascicoliMultiploResult result) {
					ShowAppLoadingEvent.fire(getPresenter(), false);
					if (result.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getPresenter()._getEventBus().fireEvent(event);
					} else {
						// rimuovo la pratica dalla cache locale per farla
						// ricaricare dopo le modifiche e poi ci torno
						getPresenter().getPecInPraticheDB().remove(getPresenter().getFascicoloPath());
						getPresenter().getPecInPraticheDB().getFascicoloByPath(getPresenter().getFascicoloPath(), true, new PraticaFascicoloLoaded() {

							@Override
							public void onPraticaLoaded(FascicoloDTO pec) {
								if (!getPresenter().getView().getPraticheProcediSelezionate().isEmpty()) {
									eliminaCollegamentoPraticaProcedi(getPresenter().getView().getPraticheProcediSelezionate());

								} else {
									getPresenter().getView().mostraPratica(pec);
								}
							}

							@Override
							public void onPraticaError(String error) {
								ShowMessageEvent event = new ShowMessageEvent();
								event.setErrorMessage(error);
								getPresenter()._getEventBus().fireEvent(event);
							}
						});
					}
				}
			});
		} else if (!getPresenter().getView().getPraticheProcediSelezionate().isEmpty()) {
			eliminaCollegamentoPraticaProcedi(getPresenter().getView().getPraticheProcediSelezionate());
			getPresenter()._getEventBus().fireEvent(event);
		}
	}

	void eliminaCollegamentoPraticaProcedi(final Set<PraticaProcedi> praticheSelezionate) {

		EliminaCollegaPraticaProcediAction action = new EliminaCollegaPraticaProcediAction(getPresenter().getFascicoloPath(), new ArrayList<PraticaProcedi>(praticheSelezionate));

		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<EliminaCollegaPraticaProcediResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(caught.getMessage());
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(EliminaCollegaPraticaProcediResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				getPresenter().getView().rimuoviPraticheProcedi(new ArrayList<PraticaProcedi>(praticheSelezionate));
				getPresenter().getView().mostraPratica((FascicoloDTO) result.getPraticaDTO());
			}

		});
	}

}

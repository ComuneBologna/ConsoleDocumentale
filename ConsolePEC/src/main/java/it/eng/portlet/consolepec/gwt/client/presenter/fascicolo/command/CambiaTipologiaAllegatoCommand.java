package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaTipologiaAllegato;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaTipologiaAllegatoResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class CambiaTipologiaAllegatoCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {

	private String tipologiaAllegato;

	private List<String> nomiAllegati;

	public CambiaTipologiaAllegatoCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	public void setTipologiaAllegato(String tipologiaAllegato) {
		this.tipologiaAllegato = tipologiaAllegato;
	}

	public void setNomiAllegati(List<String> nomiAllegati) {
		this.nomiAllegati = nomiAllegati;
	}

	@Override
	protected void _execute() {
		ShowAppLoadingEvent.fire(getPresenter(), true);

		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);

		CambiaTipologiaAllegato action = new CambiaTipologiaAllegato(getPresenter().getFascicoloPath(), tipologiaAllegato, nomiAllegati);

		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<CambiaTipologiaAllegatoResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(CambiaTipologiaAllegatoResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getMessageError());
					getPresenter()._getEventBus().fireEvent(event);
				} else {
					SitemapMenu sitemapMenu = getPresenter().getSitemapMenu();

					PecInPraticheDB praticheDB = getPresenter().getPecInPraticheDB();

					FascicoloDTO fascicoloRes = result.getFascicolo();
					praticheDB.insertOrUpdate(fascicoloRes.getClientID(), fascicoloRes, sitemapMenu.containsLink(fascicoloRes.getClientID()));
					getPresenter().getView().mostraPratica(fascicoloRes);
				}
			}
		});
	}

}

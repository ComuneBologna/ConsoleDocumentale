package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.sara;

import com.google.gwt.user.client.rpc.AsyncCallback;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.sara.EmissionePermessoAction;
import it.eng.portlet.consolepec.gwt.shared.action.sara.EmissionePermessoResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class EmissionePermessoCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {

	public EmissionePermessoCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		ShowAppLoadingEvent.fire(getPresenter(), true);

		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getPresenter()._getEventBus().fireEvent(event);

		EmissionePermessoAction action = new EmissionePermessoAction(getPresenter().getFascicoloPath());

		getPresenter().getDispatchAsync().execute(action, new AsyncCallback<EmissionePermessoResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(EmissionePermessoResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getMsgError());
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

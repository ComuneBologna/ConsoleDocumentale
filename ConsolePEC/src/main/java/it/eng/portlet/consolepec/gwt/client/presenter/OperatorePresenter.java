package it.eng.portlet.consolepec.gwt.client.presenter;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.event.MostraSceltaOperatoreEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraSceltaOperatoreEvent.MostraSceltaOperatoreHandler;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.BackToFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.BackToDettaglioPecInEvent;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaOperatore;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaOperatoreResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class OperatorePresenter extends Presenter<OperatorePresenter.MyView, OperatorePresenter.MyProxy> implements MostraSceltaOperatoreHandler {

	private final DispatchAsync dispatcher;
	private final PecInPraticheDB praticheDB;
	private final EventBus eventBus;
	private final SitemapMenu sitemapMenu;
	private ConfigurazioniHandler configurazioniHandler;

	private PraticaDTO pratica;

	public interface MyView extends View {
		void clearForm();

		void setOperatori(List<String> operatori, String operatoreDefault);

		String getOperatore();

		void setAnnullaCommand(com.google.gwt.user.client.Command annullaCommand);

		void setConfermaCommand(com.google.gwt.user.client.Command confermaCommand);

		void setErrorMessage(String message);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<OperatorePresenter> {/**/}

	@Inject
	public OperatorePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, PecInPraticheDB db, final SitemapMenu sitemapMenu,
			ConfigurazioniHandler configurazioniHandler) {
		super(eventBus, view, proxy);
		this.praticheDB = db;
		this.dispatcher = dispatcher;
		this.eventBus = eventBus;
		this.sitemapMenu = sitemapMenu;
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setAnnullaCommand(new AnnullaCommand());
		getView().setConfermaCommand(new ConfermaCommand());
	}

	@Override
	protected void onHide() {
		super.onHide();
		dropMessage();
	}

	private void dropMessage() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
	}

	public class ConfermaCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			ShowAppLoadingEvent.fire(OperatorePresenter.this, true);

			String operatore = getView().getOperatore();

			if (operatore == null || operatore.trim().isEmpty()) {
				getView().setErrorMessage("Operatore non valido");

			} else {
				dispatcher.execute(new ModificaOperatore(pratica, operatore), new AsyncCallback<ModificaOperatoreResult>() {

					@Override
					public void onSuccess(ModificaOperatoreResult result) {
						ShowAppLoadingEvent.fire(OperatorePresenter.this, false);
						if (!result.isError()) {
							dropMessage();

							PraticaDTO praticaAggiornata = result.getPratica();

							praticheDB.insertOrUpdate(praticaAggiornata.getClientID(), praticaAggiornata, sitemapMenu.containsLink(praticaAggiornata.getClientID()));
							if (pratica instanceof FascicoloDTO) {
								eventBus.fireEvent(new BackToFascicoloEvent(pratica.getClientID()));
							}
							if (pratica instanceof PecInDTO) {
								eventBus.fireEvent(new BackToDettaglioPecInEvent(pratica.getClientID()));
							}

						} else {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(result.getMsgError());
							eventBus.fireEvent(event);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						ShowAppLoadingEvent.fire(OperatorePresenter.this, false);
						ShowMessageEvent showMessageEvent = new ShowMessageEvent();
						showMessageEvent.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(showMessageEvent);
					}

				});
			}
		}
	}

	public class AnnullaCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			if (pratica instanceof FascicoloDTO) {
				eventBus.fireEvent(new BackToFascicoloEvent(pratica.getClientID()));
			}
			if (pratica instanceof PecInDTO) {
				eventBus.fireEvent(new BackToDettaglioPecInEvent(pratica.getClientID()));
			}

		}
	}

	@Override
	@ProxyEvent
	public void onMostraSceltaOperatoreHandler(MostraSceltaOperatoreEvent event) {
		this.pratica = event.getPratica();
		String gruppo = pratica.getAssegnatario();
		gruppo = configurazioniHandler.getAnagraficaRuoloByEtichetta(gruppo).getRuolo();
		getView().setOperatori(configurazioniHandler.getOperatori(gruppo), pratica.getOperatore());
		revealInParent();
	}

}

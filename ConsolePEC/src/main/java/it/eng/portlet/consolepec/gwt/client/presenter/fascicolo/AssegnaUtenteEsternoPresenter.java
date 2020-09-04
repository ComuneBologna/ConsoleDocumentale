package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.BackToFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaEsternoFromDettaglioFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaEsternoFromDettaglioFascicoloEvent.GoToAssegnaEsternoFromDettaglioFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.AssegnaUtenteEsternoAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.AssegnaUtenteEsternoResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.GetDatiAssegnaEsterno;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.GetDatiAssegnaEsternoResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
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

public class AssegnaUtenteEsternoPresenter extends Presenter<AssegnaUtenteEsternoPresenter.MyView, AssegnaUtenteEsternoPresenter.MyProxy> implements GoToAssegnaEsternoFromDettaglioFascicoloHandler {

	private String fascicoloPath;
	private final PecInPraticheDB praticheDB;
	private final DispatchAsync dispatcher;

	public interface MyView extends View {

		void setSuggestOracleDestinatari(List<String> destinatari, Command command);

		void popolaOperazioniFascicolo(String titolo, ImageResource icona, HashMap<String, Boolean> operazioni, Command onSelezioneCommand);

		void setAnnullaCommand(Command command);

		void setConfermaCommand(Command command);

		List<String> getDestinatari();

		String getTestoEmail();

		List<String> getOperazioni();

		void resetTestoEmail();

	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<AssegnaUtenteEsternoPresenter> {
	}

	@Inject
	public AssegnaUtenteEsternoPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB praticheDB, final DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.praticheDB = praticheDB;
		this.dispatcher = dispatcher;
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
	protected void onReveal() {
		super.onReveal();

		ShowAppLoadingEvent.fire(AssegnaUtenteEsternoPresenter.this, true);

		dispatcher.execute(new GetDatiAssegnaEsterno(), new AsyncCallback<GetDatiAssegnaEsternoResult>() {

			@Override
			public void onSuccess(final GetDatiAssegnaEsternoResult result) {

				praticheDB.getFascicoloByPath(fascicoloPath, false, new PraticaFascicoloLoaded() {

					@Override
					public void onPraticaLoaded(FascicoloDTO fascicolo) {
						HashMap<String, Boolean> mappaCompletaOperazioni = new HashMap<String, Boolean>();

						for (String operazione : fascicolo.getOperazioni()) {
							boolean abilitata = fascicolo.getOperazioniAssegnaUtenteEsterno().contains(operazione);
							mappaCompletaOperazioni.put(operazione, abilitata);
						}

						ImageResource icona = ConsolePECIcons._instance.fascicolo();
						String titolo = fascicolo.getTitolo() + " (" + fascicolo.getNumeroRepertorio() + ")";

						getView().popolaOperazioniFascicolo(titolo, icona, mappaCompletaOperazioni, new Command() {

							@Override
							public void execute() {
								// TODO: cosa?
							}
						});

						getView().setSuggestOracleDestinatari(result.getEmails(), new Command() {

							@Override
							public void execute() {
								// TODO: cosa?
							}
						});

						getView().resetTestoEmail();
						ShowAppLoadingEvent.fire(AssegnaUtenteEsternoPresenter.this, false);

					}

					@Override
					public void onPraticaError(String error) {
						ShowAppLoadingEvent.fire(AssegnaUtenteEsternoPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}

				});

			}

			@Override
			public void onFailure(Throwable t) {
				ShowAppLoadingEvent.fire(AssegnaUtenteEsternoPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}

		});

	}

	@Override
	@ProxyEvent
	public void onGoToAssegnaEsternoFromDettaglioFascicolo(GoToAssegnaEsternoFromDettaglioFascicoloEvent event) {
		fascicoloPath = event.getIdFascicolo();
		revealInParent();
	}

	private class AnnullaCommand implements Command {

		@Override
		public void execute() {
			getEventBus().fireEvent(new BackToFascicoloEvent(fascicoloPath));
		}

	}

	private class ConfermaCommand implements Command {

		private final static String MAIL_VALIDATION_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		@Override
		public void execute() {

			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);

			List<String> destinatari = getView().getDestinatari();
			String testoEmail = getView().getTestoEmail();
			List<String> operazioni = getView().getOperazioni();

			if (destinatari.isEmpty()) {
				showValidationError("Indicare almeno un destinatario");
				return;
			}
			
			for(String destinatario : destinatari) {
				if(!destinatario.matches(MAIL_VALIDATION_REGEX)) {
					showValidationError("L'indirizzo email \"" + destinatario + "\" non e' ben formato" );
					return;
				}
			}


			if (operazioni.isEmpty()) {
				showValidationError("Indicare almeno un'operazione abilitata");
				return;
			}
			
				
			AssegnaUtenteEsternoAction action = new AssegnaUtenteEsternoAction(fascicoloPath, destinatari, testoEmail, operazioni);

			ShowAppLoadingEvent.fire(AssegnaUtenteEsternoPresenter.this, true);

			dispatcher.execute(action, new AsyncCallback<AssegnaUtenteEsternoResult>() {

				@Override
				public void onSuccess(AssegnaUtenteEsternoResult res) {
					ShowAppLoadingEvent.fire(AssegnaUtenteEsternoPresenter.this, false);
					if (res.isError() == false) {
						praticheDB.remove(fascicoloPath);
						getEventBus().fireEvent(new BackToFascicoloEvent(fascicoloPath));
					} else {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}

				}

				@Override
				public void onFailure(Throwable t) {
					ShowAppLoadingEvent.fire(AssegnaUtenteEsternoPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

			});

		}

		private void showValidationError(String msg) {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setWarningMessage(msg);
			getEventBus().fireEvent(event);

		}
	}
}

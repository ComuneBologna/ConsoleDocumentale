package it.eng.portlet.consolepec.gwt.client.presenter.rubrica;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.GestioneRubricaAbilitazione;
import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraCreaAnagraficaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraCreaAnagraficaEvent.MostraCreaAnagraficaHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraDettaglioAnagraficaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraListaAnagraficheEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraListaAnagraficheEvent.MostraListaAnagraficheHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.SelezionaAnagraficaFineEvent;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.CreaAnagraficaAction;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.CreaAnagraficaResult;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.ImportaLagAction;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.ImportaLagResult;
import lombok.Getter;

/**
 * @author GiacomoFM
 * @since 16/ott/2017
 */
public class CreaAnagraficaPresenter extends Presenter<CreaAnagraficaPresenter.MyView, CreaAnagraficaPresenter.MyProxy> implements MostraCreaAnagraficaHandler, MostraListaAnagraficheHandler {

	@ProxyCodeSplit
	@NameToken(NameTokens.creaanagrafica)
	public interface MyProxy extends ProxyPlace<CreaAnagraficaPresenter> {
		//
	}

	public interface MyView extends View {
		void reset();

		void prePopola(Anagrafica anagrafica);

		String getCodiceFiscale();

		void setImportaLagCommand(com.google.gwt.user.client.Command command);

		void setIndietroCommand(com.google.gwt.user.client.Command command);

		void setCreaCommand(Command<Void, Anagrafica> command);
	}

	private final DispatchAsync dispatcher;

	private final SitemapMenu sitemapMenu;

	private TrackerOpReq tracker = new TrackerOpReq(null, null);

	@Inject
	public CreaAnagraficaPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final SitemapMenu sitemapMenu,
			ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);

		if (!profilazioneUtenteHandler.isAbilitato(GestioneRubricaAbilitazione.class)) {
			throw new IllegalArgumentException("Utente non abilitato");
		}

		this.dispatcher = dispatcher;
		this.sitemapMenu = sitemapMenu;

		sitemapMenu.setActiveVoce(VociRootSiteMap.CREA_ANAGRAFICA.getId());
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	@ProxyEvent
	public void onMostraCreaAnagrafica(MostraCreaAnagraficaEvent event) {
		tracker = new TrackerOpReq(event.getOpeningRequestor(), event.getNomeDatoAggiuntivo());
		revealInParent();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		getView().reset();
		sitemapMenu.setActiveVoce(VociRootSiteMap.CREA_ANAGRAFICA.getId());
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().setIndietroCommand(new com.google.gwt.user.client.Command() {
			@Override
			public void execute() {
				if (tracker.isUsed()) {
					getEventBus().fireEvent(new BackFromPlaceEvent());
				} else {
					SelezionaAnagraficaFineEvent event = new SelezionaAnagraficaFineEvent(tracker.use());
					event.setNomeDatoAggiuntivo(tracker.getNomeDatoAggiuntivo());
					getEventBus().fireEvent(event);
				}
			}
		});

		getView().setCreaCommand(new Command<Void, Anagrafica>() {
			@Override
			public Void exe(Anagrafica t) {
				salvaAnagrafica(tracker, t);
				return null;
			}
		});

		getView().setImportaLagCommand(new com.google.gwt.user.client.Command() {
			@Override
			public void execute() {
				importaLag();
			}
		});
	}

	private void importaLag() {
		if (getView().getCodiceFiscale() == null || getView().getCodiceFiscale().isEmpty()) {
			fireErrorMessageEvent("Nessun codice fiscale inserito per l'importazione dal LAG");
			return;
		}

		ShowAppLoadingEvent.fire(CreaAnagraficaPresenter.this, true);
		dispatcher.execute(new ImportaLagAction(getView().getCodiceFiscale()), new AsyncCallback<ImportaLagResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(CreaAnagraficaPresenter.this, false);
				fireErrorMessageEvent(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(ImportaLagResult result) {
				ShowAppLoadingEvent.fire(CreaAnagraficaPresenter.this, false);
				if (result.isError()) {
					fireErrorMessageEvent(result.getMsgError());
				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					if (result.getAnagrafica() != null) {
						event.setInfoMessage("Anagrafica importata correttamente dal LAG"
								+ (result.getAnagrafica().getIndirizzi().isEmpty() ? "" : ", " + result.getAnagrafica().getIndirizzi().size() + " indirizzi aggiunti"));
						getEventBus().fireEvent(event);
						getView().prePopola(result.getAnagrafica());
					}
				}
			}
		});
	}

	private void salvaAnagrafica(final TrackerOpReq tracker, Anagrafica anagrafica) {
		List<String> errors = ValidationUtilities.valida(anagrafica);
		if (errors.size() != 0) {
			StringBuilder sb = new StringBuilder();
			for (String error : errors) {
				sb.append(error).append("\n");
			}
			fireErrorMessageEvent(sb.toString());
			return;
		}

		ShowAppLoadingEvent.fire(CreaAnagraficaPresenter.this, true);
		dispatcher.execute(new CreaAnagraficaAction(anagrafica), new AsyncCallback<CreaAnagraficaResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(CreaAnagraficaPresenter.this, false);
				fireErrorMessageEvent(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(CreaAnagraficaResult result) {
				ShowAppLoadingEvent.fire(CreaAnagraficaPresenter.this, false);
				if (result.isError()) {
					fireErrorMessageEvent(result.getMsgError());
				} else {
					if (tracker.isUsed()) {
						getEventBus().fireEvent(new MostraDettaglioAnagraficaEvent(result.getAnagrafica(), CreaAnagraficaPresenter.this));
					} else {
						SelezionaAnagraficaFineEvent event = new SelezionaAnagraficaFineEvent(tracker.use(), result.getAnagrafica());
						event.setNomeDatoAggiuntivo(tracker.getNomeDatoAggiuntivo());
						getEventBus().fireEvent(event);
					}
				}
			}
		});
	}

	private void fireErrorMessageEvent(String msg) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(msg);
		getEventBus().fireEvent(event);
	}

	@Override
	@ProxyEvent
	public void onMostraListaAnagrafiche(MostraListaAnagraficheEvent event) {
		if (this.equals(event.getOpeningRequestor())) {
			getView().reset();
			revealInParent();
		}
	}

	private class TrackerOpReq {
		private boolean track;
		@Getter private String nomeDatoAggiuntivo;
		private Object openingRequestor;

		TrackerOpReq(Object openingRequestor, String nomeDatoAggiuntivo) {
			this.track = false;
			this.openingRequestor = openingRequestor;
			this.nomeDatoAggiuntivo = nomeDatoAggiuntivo;
		}

		Object use() {
			this.track = true;
			return openingRequestor;
		}

		boolean isUsed() {
			return openingRequestor == null || this.track;
		}
	}

}

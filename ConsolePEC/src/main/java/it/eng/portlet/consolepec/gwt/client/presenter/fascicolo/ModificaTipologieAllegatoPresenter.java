package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.BackToFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.MostraModificaTipologieAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.MostraModificaTipologieAllegatoEvent.MostraModificaTipologieAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.BackToFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.IBackToFascicolo;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.ModificaTipologieAllegatiAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.ModificaTipologieAllegatiResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

/**
 * @author GiacomoFM
 * @since 10/dic/2018
 */
public class ModificaTipologieAllegatoPresenter extends Presenter<ModificaTipologieAllegatoPresenter.MyView, ModificaTipologieAllegatoPresenter.MyProxy> implements IBackToFascicolo, MostraModificaTipologieAllegatoHandler {

	private final DispatchAsync dispatcher;
	private final PlaceManager placeManager;
	private final PecInPraticheDB praticheDB;

	private ConfigurazioniHandler configurazioniHandler;

	private String pathFascicolo;
	private FascicoloDTO fascicoloDTO;

	private Set<AllegatoDTO> allegati = new HashSet<>();
	private List<String> tipologie = new ArrayList<>();

	@Inject
	public ModificaTipologieAllegatoPresenter(EventBus eventBus, MyView view, MyProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager, PecInPraticheDB praticheDB,
			ConfigurazioniHandler configurazioniHandler) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
		this.placeManager = placeManager;
		this.praticheDB = praticheDB;

		this.configurazioniHandler = configurazioniHandler;
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<ModificaTipologieAllegatoPresenter> {
		// ~
	}

	public interface MyView extends View {

		void setAllegatiSelezionati(Set<AllegatoDTO> allegati);

		void setTipologieSelezionabili(List<String> tipologie);

		List<String> getTipologieSelezionate();

		void setAnnullaCommand(Command annullaCommand);

		void setConfermaCommand(Command confermaCommand);

	}

	@Override
	public EventBus _getEventBus() {
		return getEventBus();
	}

	@Override
	public DispatchAsync getDispatchAsync() {
		return dispatcher;
	}

	@Override
	public PlaceManager getPlaceManager() {
		return placeManager;
	}

	@Override
	public PecInPraticheDB getPecInPraticheDB() {
		return praticheDB;
	}

	@Override
	public String getFascicoloPath() {
		return pathFascicolo;
	}

	@Override
	@ProxyEvent
	public void onMostraModificaTipologiaAllegato(MostraModificaTipologieAllegatoEvent event) {
		allegati.clear();
		tipologie.clear();

		allegati.addAll(event.getAllegati());
		pathFascicolo = event.getPathFascicolo();
		praticheDB.getFascicoloByPath(pathFascicolo, false, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {
				fascicoloDTO = fascicolo;
				AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(fascicolo.getTipologiaPratica().getNomeTipologia());
				tipologie.addAll(af.getTipologieAllegato());
				revealInParent();
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(ModificaTipologieAllegatoPresenter.this, false);
				fireErrorMessageEvent(ConsolePecConstants.ERROR_MESSAGE);
			}
		});
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	private void fireErrorMessageEvent(String msg) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(msg);
		getEventBus().fireEvent(event);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setAnnullaCommand(new BackToFascicoloCommand<ModificaTipologieAllegatoPresenter>(this));
		getView().setConfermaCommand(new ConfermaCommand());
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		getView().setAllegatiSelezionati(allegati);
		getView().setTipologieSelezionabili(tipologie);
		Window.scrollTo(0, 0);
	}

	private class ConfermaCommand implements Command {
		@Override
		public void execute() {
			ShowAppLoadingEvent.fire(ModificaTipologieAllegatoPresenter.this, true);
			gestisciModificaTipologie(getView().getTipologieSelezionate());
			ModificaTipologieAllegatiAction action = new ModificaTipologieAllegatiAction(fascicoloDTO, new ArrayList<>(allegati));
			dispatcher.execute(action, new AsyncCallback<ModificaTipologieAllegatiResult>() {
				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(ModificaTipologieAllegatoPresenter.this, false);
					fireErrorMessageEvent(ConsolePecConstants.ERROR_MESSAGE);
				}

				@Override
				public void onSuccess(ModificaTipologieAllegatiResult result) {
					ShowAppLoadingEvent.fire(ModificaTipologieAllegatoPresenter.this, false);
					if (result.isError()) {
						fireErrorMessageEvent(result.getErrorMessage());
					} else {
						praticheDB.remove(pathFascicolo);
						getEventBus().fireEvent(new BackToFascicoloEvent(pathFascicolo));
					}
				}
			});
		}

	}

	private void gestisciModificaTipologie(List<String> tipologieSelezionate) {
		if (allegati.size() == 1) {
			AllegatoDTO a = allegati.iterator().next();
			a.getTipologiaAllegato().clear();
			a.getTipologiaAllegato().addAll(tipologieSelezionate);
		} else {
			Set<String> tipologieComuni = loadTipologieComuni();
			for (AllegatoDTO allegato : allegati) {
				for (String tipologiaComune : tipologieComuni) {
					if (!tipologieSelezionate.contains(tipologiaComune)) {
						allegato.getTipologiaAllegato().remove(tipologiaComune);
					}
				}
				for (String tipologiaSelezionata : tipologieSelezionate) {
					if (!allegato.getTipologiaAllegato().contains(tipologiaSelezionata)) {
						allegato.getTipologiaAllegato().add(tipologiaSelezionata);
					}
				}
			}
		}
	}

	private Set<String> loadTipologieComuni() {
		Set<String> tipologieComuni = new HashSet<>();
		if (!allegati.isEmpty()) {
			tipologieComuni.addAll(allegati.iterator().next().getTipologiaAllegato());
		}
		for (AllegatoDTO allegato : allegati) {
			tipologieComuni.retainAll(allegato.getTipologiaAllegato());
		}
		return tipologieComuni;
	}

}

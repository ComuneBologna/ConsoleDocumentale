package it.eng.portlet.consolepec.gwt.client.presenter.visibilita;

import java.util.TreeSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SuggestBox;
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

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.ModificaVisibilitaAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.ModificaVisibilitaAllegatoEvent.ModificaVisibilitaAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.ModificaVisibilitaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.ModificaVisibilitaFascicoloEvent.ModificaVisibilitaFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.GruppiVisibilitaSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaVisibilitaAllegato;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaVisibilitaAllegatoResult;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaVisibilitaFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaVisibilitaFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaGruppiVisibilita;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaGruppiVisibilitaResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita;

public class ModificaVisibilitaPresenter extends Presenter<ModificaVisibilitaPresenter.MyView, ModificaVisibilitaPresenter.MyProxy> implements ModificaVisibilitaAllegatoHandler, ModificaVisibilitaFascicoloHandler {

	private String idFascicolo;
	private FascicoloDTO fascicolo;
	private String assegnatario;
	private AllegatoDTO allegatoDTO;
	private Command modificaVisibilitaCommand;
	private Command annullaCommnad;
	private TreeSet<GruppoVisibilita> gruppiCorrenti = new TreeSet<GruppoVisibilita>();
	private SuggestBox suggestBox;

	private final PlaceManager placeManager;
	private final DispatchAsync dispatchAsync;
	private final PecInPraticheDB pecInPraticheDB;

	private TreeSet<GruppoVisibilita> gruppoVisibilita = new TreeSet<GruppoVisibilita>();

	public interface MyView extends View {

		public Button getAnnullaButton();

		public Button getConfermaButton();

		public Button getAggiungiButton();

		public void setTitle(String title);

		public void mostraGruppoVisibilita(TreeSet<GruppoVisibilita> gruppiCorrenti, String assegnatario);

		public void setSuggestBox(SuggestBox suggestBox);

		public void setRimuoviGruppoVisibilitaCommand(RimuoviGruppoVisibilita rimuoviGruppoVisibilita);

	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<ModificaVisibilitaPresenter> {}

	@Inject
	public ModificaVisibilitaPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PlaceManager placeManager, final DispatchAsync dispatchAsync,
			final PecInPraticheDB pecInPraticheDB) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
		this.dispatchAsync = dispatchAsync;
		this.pecInPraticheDB = pecInPraticheDB;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		Window.scrollTo(0, 0);
		getView().mostraGruppoVisibilita(gruppiCorrenti, assegnatario);
		initGruppiVisibilita();
	}

	private void initGruppiVisibilita() {

		RecuperaGruppiVisibilita recuperaGruppiVisibilita = new RecuperaGruppiVisibilita();
		recuperaGruppiVisibilita.setTipologiaPratica(fascicolo.getTipologiaPratica());

		ShowAppLoadingEvent.fire(ModificaVisibilitaPresenter.this, true);
		dispatchAsync.execute(recuperaGruppiVisibilita, new AsyncCallback<RecuperaGruppiVisibilitaResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				ShowAppLoadingEvent.fire(ModificaVisibilitaPresenter.this, false);
				ShowMessageEvent errorEvent = new ShowMessageEvent();
				errorEvent.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(errorEvent);
			}

			@Override
			public void onSuccess(RecuperaGruppiVisibilitaResult result) {
				ShowAppLoadingEvent.fire(ModificaVisibilitaPresenter.this, false);
				if (result.isError()) {
					ShowMessageEvent errorEvent = new ShowMessageEvent();
					errorEvent.setErrorMessage(result.getErrorMessage());
					getEventBus().fireEvent(errorEvent);

				} else {
					gruppoVisibilita = result.getGruppoVisibilita();
					reloadSuggestBox(gruppoVisibilita);

				}

			}
		});
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getAnnullaButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				annullaCommnad.execute();
			}
		});
		getView().getConfermaButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				modificaVisibilitaCommand.execute();
			}
		});

		getView().getAggiungiButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {

				String selectedGruppoVisibilita = suggestBox.getValue();
				if (selectedGruppoVisibilita == null || selectedGruppoVisibilita.isEmpty()) {
					ShowMessageEvent errorEvent = new ShowMessageEvent();
					errorEvent.setMessageDropped(false);
					errorEvent.setWarningMessage("Selezionare almeno un gruppo");
					getEventBus().fireEvent(errorEvent);
				} else {

					for (GruppoVisibilita gruppo : gruppoVisibilita) {
						if (gruppo.getLabel().equalsIgnoreCase(selectedGruppoVisibilita.trim())) {
							gruppiCorrenti.add(gruppo);
							getView().mostraGruppoVisibilita(gruppiCorrenti, assegnatario);
							reloadSuggestBox(gruppoVisibilita);
							ShowMessageEvent event = new ShowMessageEvent();
							event.setMessageDropped(true);
							getEventBus().fireEvent(event);
						}
					}
				}
				suggestBox.setValue(null);
			}
		});

		getView().setRimuoviGruppoVisibilitaCommand(new RimuoviGruppoVisibilita());
	}

	@Override
	@ProxyEvent
	public void onModificaVisibilitaFascicolo(ModificaVisibilitaFascicoloEvent event) {
		idFascicolo = event.getFascicolo().getClientID();
		fascicolo = event.getFascicolo();
		assegnatario = fascicolo.getAssegnatario();
		gruppiCorrenti = new TreeSet<GruppoVisibilita>(fascicolo.getVisibilita());
		allegatoDTO = null;
		modificaVisibilitaCommand = new ModificaVisibilitaFascicoloCommnad();
		annullaCommnad = new AnnullaCommand();
		getView().setTitle(ConsolePecConstants.TITOLO_FASCICOLO);
		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onModificaVisibilitaAllegato(ModificaVisibilitaAllegatoEvent event) {
		idFascicolo = event.getFascicolo().getClientID();
		fascicolo = event.getFascicolo();
		assegnatario = null;
		allegatoDTO = event.getAllegatoDTO();
		gruppiCorrenti = new TreeSet<GruppoVisibilita>(allegatoDTO.getVisibilita());
		modificaVisibilitaCommand = new ModificaVisibilitaAllegatoCommand();
		annullaCommnad = new AnnullaCommand();
		getView().setTitle(ConsolePecConstants.TITOLO_ALLEGATO);
		revealInParent();

	}

	/** INNER CLASS */

	private void goToFascicolo() {
		placeManager.revealCurrentPlace();
	}

	private void reloadSuggestBox(TreeSet<GruppoVisibilita> gruppoVisibilita) {
		TreeSet<GruppoVisibilita> internalGruppiVisibilita = new TreeSet<GruppoVisibilita>(gruppoVisibilita);
		internalGruppiVisibilita.removeAll(gruppiCorrenti);
		GruppiVisibilitaSuggestOracle gruppi = new GruppiVisibilitaSuggestOracle(internalGruppiVisibilita);
		suggestBox = new SuggestBox(gruppi);
		suggestBox.setStyleName("testo");
		getView().setSuggestBox(suggestBox);
	}

	public class AnnullaCommand implements Command {

		@Override
		public void execute() {
			goToFascicolo();

		}
	}

	public class RimuoviGruppoVisibilita implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, GruppoVisibilita> {

		@Override
		public Void exe(GruppoVisibilita gruppoVisibilita) {
			gruppiCorrenti.remove(gruppoVisibilita);
			getView().mostraGruppoVisibilita(gruppiCorrenti, assegnatario);
			reloadSuggestBox(ModificaVisibilitaPresenter.this.gruppoVisibilita);
			return null;
		}

	}

	protected abstract class ModificaVisibilita implements Command {

		@Override
		public void execute() {
			modificaVisibilita();
		}

		protected abstract void modificaVisibilita();
	}

	public class ModificaVisibilitaFascicoloCommnad extends ModificaVisibilita {

		@Override
		protected void modificaVisibilita() {
			ModificaVisibilitaFascicolo modificaVisibilitaFascicolo = new ModificaVisibilitaFascicolo();
			modificaVisibilitaFascicolo.setGruppiVisibilita(gruppiCorrenti);
			modificaVisibilitaFascicolo.setClientID(idFascicolo);

			ShowAppLoadingEvent.fire(ModificaVisibilitaPresenter.this, true);

			dispatchAsync.execute(modificaVisibilitaFascicolo, new AsyncCallback<ModificaVisibilitaFascicoloResult>() {

				@Override
				public void onFailure(Throwable arg0) {
					ShowAppLoadingEvent.fire(ModificaVisibilitaPresenter.this, false);
					ShowMessageEvent errorEvent = new ShowMessageEvent();
					errorEvent.setWarningMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(errorEvent);
					pecInPraticheDB.remove(idFascicolo);
					goToFascicolo();
				}

				@Override
				public void onSuccess(ModificaVisibilitaFascicoloResult result) {
					ShowAppLoadingEvent.fire(ModificaVisibilitaPresenter.this, false);
					if (result.isError()) {
						ShowMessageEvent errorEvent = new ShowMessageEvent();
						errorEvent.setWarningMessage(result.getErrorMessage());
						getEventBus().fireEvent(errorEvent);
					} else {
						pecInPraticheDB.remove(idFascicolo);
					}
					goToFascicolo();
				}
			});

		}
	}

	public class ModificaVisibilitaAllegatoCommand extends ModificaVisibilita {

		@Override
		protected void modificaVisibilita() {
			ModificaVisibilitaAllegato modificaVisibilitaAllegato = new ModificaVisibilitaAllegato();
			modificaVisibilitaAllegato.setGruppiVisibilita(gruppiCorrenti);
			modificaVisibilitaAllegato.setClientID(idFascicolo);
			modificaVisibilitaAllegato.setNomeAllegato(allegatoDTO.getNome());

			ShowAppLoadingEvent.fire(ModificaVisibilitaPresenter.this, true);

			dispatchAsync.execute(modificaVisibilitaAllegato, new AsyncCallback<ModificaVisibilitaAllegatoResult>() {

				@Override
				public void onFailure(Throwable arg0) {
					ShowAppLoadingEvent.fire(ModificaVisibilitaPresenter.this, false);
					ShowMessageEvent errorEvent = new ShowMessageEvent();
					errorEvent.setWarningMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(errorEvent);
					pecInPraticheDB.remove(idFascicolo);
					goToFascicolo();
				}

				@Override
				public void onSuccess(ModificaVisibilitaAllegatoResult result) {
					ShowAppLoadingEvent.fire(ModificaVisibilitaPresenter.this, false);
					if (result.isError()) {
						ShowMessageEvent errorEvent = new ShowMessageEvent();
						errorEvent.setWarningMessage(result.getErrorMessage());
						getEventBus().fireEvent(errorEvent);
					} else {
						pecInPraticheDB.remove(idFascicolo);
					}
					goToFascicolo();
				}
			});

		}
	}
}

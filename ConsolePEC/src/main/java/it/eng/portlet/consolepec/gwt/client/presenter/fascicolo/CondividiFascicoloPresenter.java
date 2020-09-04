package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiCondividiFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraCondividiFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraCondividiFascicoloEvent.MostraCondividiFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.AnagraficheRuoliSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicoloMultipla;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicoloMultiplaResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;
import it.eng.portlet.consolepec.gwt.shared.dto.CondivisioneDto;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class CondividiFascicoloPresenter extends Presenter<CondividiFascicoloPresenter.MyView, CondividiFascicoloPresenter.MyProxy> implements MostraCondividiFascicoloHandler {

	protected static final boolean RuoliDTO = false;
	private final PecInPraticheDB praticheDB;
	private DispatchAsync dispatcher;
	private FascicoloDTO fascicolo;
	private String fascicoloPath;
	private SuggestBox gruppiSuggestBox;

	private ConfigurazioniHandler configurazioniHandler;

	public interface MyView extends View {

		public void setGruppiSuggestBox(SuggestBox suggestBox);

		public void setAnnullaCommand(Command annullaCommand);

		public void setConfermaCommand(Command confermaCommand);

		public void setAggiungiCommand(Command aggiungiCommand);

		public void popolaCondivisioneGruppo(String gruppo, ImageResource icona, HashMap<String, Boolean> operazioni, boolean eliminaAttivo, Command onSelezioneCommand);

		public void clearCondivisioni();

		public void attivaPulsanteAggiungi(boolean enabled);

		public List<String> getOperazioniSelezionate(String nomeGruppo);

		public void attivaPulsanteConferma(boolean enabled);

		public void setEliminaCommand(String nomeGruppo, Command eliminaCommand);

		public void attivaPulsanteElimina(String nomeGruppo, boolean enabled);

		public void refreshCondivisioni();

		public HashMap<String, List<String>> getOperazioniSelezionatePerGruppo();

		public void setGruppoAssegnazione(String gruppo);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<CondividiFascicoloPresenter> {}

	@Inject
	public CondividiFascicoloPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final PecInPraticheDB praticheDB,
			ConfigurazioniHandler configurazioniHandler) {
		super(eventBus, view, proxy);
		this.praticheDB = praticheDB;
		this.dispatcher = dispatcher;
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		Window.scrollTo(0, 0);
		caricaCondivisioni();
	}

	@Override
	public void onHide() {
		super.onHide();
		getView().clearCondivisioni();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setAnnullaCommand(new Command() {
			@Override
			public void execute() {
				getEventBus().fireEvent(new ChiudiCondividiFascicoloEvent(fascicoloPath));
			}
		});
		getView().setConfermaCommand(new Command() {

			@Override
			public void execute() {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setMessageDropped(true);
				getEventBus().fireEvent(event);
				CondivisioneFascicoloMultipla action = new CondivisioneFascicoloMultipla();
				HashMap<String, List<String>> mappa = getView().getOperazioniSelezionatePerGruppo();
				for (String gruppo : mappa.keySet()) {
					CondivisioneFascicolo condivisioneSingola = new CondivisioneFascicolo(CondivisioneFascicolo.SHARE_MERGE, gruppo, fascicoloPath, mappa.get(gruppo));
					action.getCondivisioni().add(condivisioneSingola);
				}
				ShowAppLoadingEvent.fire(CondividiFascicoloPresenter.this, true);
				dispatcher.execute(action, new AsyncCallback<CondivisioneFascicoloMultiplaResult>() {

					@Override
					public void onFailure(Throwable arg0) {
						ShowAppLoadingEvent.fire(CondividiFascicoloPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}

					@Override
					public void onSuccess(CondivisioneFascicoloMultiplaResult result) {
						ShowAppLoadingEvent.fire(CondividiFascicoloPresenter.this, false);
						if (result.isError()) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(result.getErrorMessage());
							getEventBus().fireEvent(event);
						} else {
							// rimuovo la pratica dalla cache locale per farla ricaricare dopo le modifiche e poi ci torno
							praticheDB.remove(fascicoloPath);
							getEventBus().fireEvent(new ChiudiCondividiFascicoloEvent(fascicoloPath));
						}
					}
				});
			}
		});
		getView().setAggiungiCommand(new Command() {

			@Override
			public void execute() {
				String gruppo = gruppiSuggestBox.getValue();
				if (gruppo != null && !gruppo.trim().equals("")) {

					if (presenteNellaListaDeiRuoli(gruppo)) {
						aggiungiCondivisione(fascicolo, gruppo);
						setGruppiSuggestBox();
					} else {
						ShowMessageEvent warning = new ShowMessageEvent();
						warning.setWarningMessage("Gruppo non valido");
						getEventBus().fireEvent(warning);
					}

				} else {
					ShowMessageEvent warning = new ShowMessageEvent();
					warning.setWarningMessage("Selezionare almeno un gruppo.");
					getEventBus().fireEvent(warning);
				}
			}
		});
	}

	private boolean presenteNellaListaDeiRuoli(String gruppoSelezionato) {
		for (AnagraficaRuolo ruolo : configurazioniHandler.getAnagraficheRuoli()) {
			if (gruppoSelezionato.equals(ruolo.getEtichetta()))
				return true;
		}

		return false;
	}

	@Override
	@ProxyEvent
	public void onMostraCondividiFascicolo(MostraCondividiFascicoloEvent event) {
		this.fascicoloPath = event.getFascicoloPath();
		revealInParent();
	}

	private void caricaCondivisioni() {
		ShowAppLoadingEvent.fire(CondividiFascicoloPresenter.this, true);
		praticheDB.getFascicoloByPath(fascicoloPath, true, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {
				ShowAppLoadingEvent.fire(CondividiFascicoloPresenter.this, false);
				CondividiFascicoloPresenter.this.fascicolo = fascicolo;
				getView().setGruppoAssegnazione(fascicolo.getAssegnatario());
				for (CondivisioneDto condivisioneDto : fascicolo.getCondivisioni())
					aggiungiCondivisione(fascicolo, condivisioneDto.getRuolo().getEtichetta());
				getView().attivaPulsanteConferma(false);
				setGruppiSuggestBox();
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(CondividiFascicoloPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}
		});
	}

	private void aggiungiCondivisione(FascicoloDTO fascicolo, final String gruppo) {
		List<String> operazioniFascicolo = fascicolo.getOperazioni();
		HashMap<String, Boolean> mappaCompletaOperazioni = new HashMap<String, Boolean>();
		boolean nuovaCondivisione = true;
		for (CondivisioneDto condivisione : fascicolo.getCondivisioni()) {
			if (gruppo.equals(condivisione.getRuolo().getEtichetta())) {
				for (String operazione : operazioniFascicolo)
					mappaCompletaOperazioni.put(operazione, condivisione.getOperazioni().contains(operazione));
				nuovaCondivisione = false;
			}
		}
		if (mappaCompletaOperazioni.isEmpty())
			for (String operazione : operazioniFascicolo)
				mappaCompletaOperazioni.put(operazione, false);
		getView().popolaCondivisioneGruppo(gruppo, ConsolePECIcons._instance.gruppo(), mappaCompletaOperazioni, isCondivisioneEliminabile(gruppo, fascicolo), new Command() {

			@Override
			public void execute() {
				getView().attivaPulsanteConferma(isConfermaAttiva());
			}
		});
		getView().attivaPulsanteConferma(isConfermaAttiva());
		getView().attivaPulsanteElimina(gruppo, !nuovaCondivisione);
		getView().setEliminaCommand(gruppo, new Command() {

			@Override
			public void execute() {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setMessageDropped(true);
				getEventBus().fireEvent(event);
				boolean cancellabile = true;
				for (CollegamentoDto collegamento : CondividiFascicoloPresenter.this.fascicolo.getCollegamenti())
					if (gruppo.equalsIgnoreCase(collegamento.getDisplayNameGruppo()))
						cancellabile = false;
				if (cancellabile) {
					eliminaCondivisione(gruppo);
				} else {
					ShowMessageEvent warning = new ShowMessageEvent();
					warning.setWarningMessage("Non è possibile rimuovere una condivisione se esistono fascicoli collegati.");
					getEventBus().fireEvent(warning);
				}

			}
		});
	}

	private boolean isCondivisioneEliminabile(String gruppo, FascicoloDTO fascicolo) {
		List<CollegamentoDto> collegamenti = fascicolo.getCollegamenti();
		for (CollegamentoDto c : collegamenti) {
			if (gruppo.equals(c.getDisplayNameGruppo())) {
				return false;
			}
		}
		return true;
	}

	private void eliminaCondivisione(String gruppo) {
		CondivisioneFascicolo action = new CondivisioneFascicolo(CondivisioneFascicolo.SHARE_DELETE, gruppo, fascicoloPath, getView().getOperazioniSelezionate(gruppo));
		ShowAppLoadingEvent.fire(CondividiFascicoloPresenter.this, true);
		dispatcher.execute(action, new AsyncCallback<CondivisioneFascicoloResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				ShowAppLoadingEvent.fire(CondividiFascicoloPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(CondivisioneFascicoloResult result) {
				ShowAppLoadingEvent.fire(CondividiFascicoloPresenter.this, false);
				if (result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					getEventBus().fireEvent(event);
				} else {
					// rimuovo la pratica dalla cache locale per farla ricaricare dopo le modifiche e poi ci torno
					praticheDB.remove(fascicoloPath);
					getEventBus().fireEvent(new ChiudiCondividiFascicoloEvent(fascicoloPath));
				}
			}
		});
	}

	private void setGruppiSuggestBox() {

		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);

		List<AnagraficaRuolo> anagraficheRuoliTotali = configurazioniHandler.getAnagraficheRuoli();
		List<AnagraficaRuolo> ruoliFiltrati = new ArrayList<AnagraficaRuolo>();
		for (AnagraficaRuolo dto : anagraficheRuoliTotali) {
			String nomeGruppo = dto.getEtichetta();
			String assegnatario = fascicolo.getAssegnatario();
			if (getView().getOperazioniSelezionatePerGruppo().get(nomeGruppo) == null && !nomeGruppo.equals(assegnatario))
				ruoliFiltrati.add(dto);
		}

		gruppiSuggestBox = new SuggestBox(new AnagraficheRuoliSuggestOracle(ruoliFiltrati));

		if (ruoliFiltrati.size() >= 1) {
			getView().attivaPulsanteAggiungi(true);
			gruppiSuggestBox.setEnabled(true);
			gruppiSuggestBox.setStyleName("testo");

		} else {
			getView().attivaPulsanteAggiungi(false);
			gruppiSuggestBox.setEnabled(false);
			gruppiSuggestBox.setStyleName("testo disabilitato");
			ShowMessageEvent warning = new ShowMessageEvent();
			warning.setWarningMessage("Non è possibile aggiungere altri gruppi.");
			getEventBus().fireEvent(warning);
		}
		getView().setGruppiSuggestBox(gruppiSuggestBox);

	}

	// abilita il pulsante anche se non ci sono azioni selezionate.
	// per le condivisioni ro.
	private boolean isConfermaAttiva() {
		// boolean attiva = true;
		// for (String gruppo : getView().getOperazioniSelezionatePerGruppo().keySet()) {
		// if (getView().getOperazioniSelezionate(gruppo).isEmpty()) {
		// attiva = false;
		// break;
		// }
		// }
		// return attiva;
		return true;
	}

}

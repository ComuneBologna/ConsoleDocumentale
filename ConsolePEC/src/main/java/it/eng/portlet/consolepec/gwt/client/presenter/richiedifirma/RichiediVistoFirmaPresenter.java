package it.eng.portlet.consolepec.gwt.client.presenter.richiedifirma;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.richiedifirma.RichiediFirmaVistoFineEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.richiedifirma.RichiediFirmaVistoInizioEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.richiedifirma.RichiediFirmaVistoInizioEvent.RichiediFirmaVistoInizioHandler;
import it.eng.portlet.consolepec.gwt.client.tasks.gestionefascicolo.operazioni.richiedifirma.RichiediFirmaTaskApiClient;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioGruppoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.ProponenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoPropostaTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.richiedifirma.RichiediFirmaVistoDTO;

/**
 *
 * @author biagiot
 *
 */
public class RichiediVistoFirmaPresenter extends Presenter<RichiediVistoFirmaPresenter.MyView, RichiediVistoFirmaPresenter.MyProxy> implements RichiediFirmaVistoInizioHandler {

	private Object openingRequestor;
	private final RichiediFirmaTaskApiClient richiediFirmaTaskApiClient;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private Set<DestinatarioDTO> destinatari;

	final long SECONDS = 1000;
	final long MINUTES = 60 * SECONDS;
	final long HOURS = 60 * MINUTES;

	public interface MyView extends View {
		void reset();

		void initViewElements();

		void setAnnullaCommand(Command cmd);

		void setConfermaCommand(Command cmd);

		void setEliminaUtenteCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Utente> eliminaUtenteCommand);

		void setElencoUtentiSelectionHandler(SelectionHandler<SuggestOracle.Suggestion> selectionHandler);

		void setElencoUtentiClickHandler(ClickHandler clickHandler);

		void setEliminaGruppoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AnagraficaRuolo> eliminaGruppoCommand);

		void setElencoGruppiSelectionHandler(SelectionHandler<SuggestOracle.Suggestion> selectionHandler);

		void setElencoGruppiClickHandler(ClickHandler clickHandler);

		void setOnChangeTipoDestinatarioCommand(Command command);

		void setTitle(String title);

		void showErrors(List<String> errors);

		void addUtenteWidget(Utente utente, boolean editable);

		void enableAggiungiUtenteButton(boolean enable);

		void enableAggiungiRuoloButton(boolean enable);

		void clearElencoUtentiSuggestBox();

		void setDataProposta(Date date);

		List<String> getIndirizziNotifica();

		String getProponente();

		String getMittenteOriginale();

		String getOggetto();

		String getNote();

		String getTextFromInputListIndirizziNotifica();

		Date getDataProposta();

		Date getDataScadenza();

		Integer getOraScadenza();

		Integer getMinutoScadenza();

		TipoPropostaTaskFirmaDTO getTipoRichiesta();

		Utente getUtenteSelezionato();

		AnagraficaRuolo getRuoloSelezionato();

		void addGruppoWidget(AnagraficaRuolo ruoloSelezionato, boolean b);

		void clearElencoGruppiSuggestBox();

		void aggiungiIndirizzoNotifica(AnagraficaRuolo ruolo);

		void setGruppoProponente(String gruppoProponente);

		void initTipoRichiestaListBox(boolean allegatoProtocollato);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<RichiediVistoFirmaPresenter> {
		//
	}

	@Inject
	public RichiediVistoFirmaPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final RichiediFirmaTaskApiClient richiediFirmaTaskApiClient,
			ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.richiediFirmaTaskApiClient = richiediFirmaTaskApiClient;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
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
		getView().setOnChangeTipoDestinatarioCommand(new OnChangeTipoDestinatarioCommand());
		getView().setEliminaUtenteCommand(new EliminaUtenteCommand());
		getView().setElencoUtentiSelectionHandler(new ElencoUtentiSelectionHandler());
		getView().setElencoUtentiClickHandler(new ElencoUtentiClickHandler());
		getView().setEliminaGruppoCommand(new EliminaGruppoCommand());
		getView().setElencoGruppiSelectionHandler(new ElencoGruppiSelectionHandler());
		getView().setElencoGruppiClickHandler(new ElencoGruppiClickHandler());
		getView().initViewElements();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
	}

	@Override
	protected void onHide() {
		super.onHide();
		destinatari.clear();
		getView().reset();
	}

	@Override
	@ProxyEvent
	public void onRichiediFirmaFascicolo(RichiediFirmaVistoInizioEvent event) {

		destinatari = new LinkedHashSet<DestinatarioDTO>();
		openingRequestor = event.getOpeningRequestor();

		getView().initTipoRichiestaListBox(event.isAllegatoProtocollato());
		getView().setGruppoProponente(event.getAssegnatario());
		getView().setTitle(ConsolePecConstants.TITOLO_RICHIEDI_VISTO_FIRMA);
		getView().setDataProposta(new Date());

		ShowAppLoadingEvent.fire(RichiediVistoFirmaPresenter.this, false);
		revealInParent();
	}

	private List<String> getDatiRichiesta(RichiediFirmaVistoDTO dto) {

		List<String> errors = new ArrayList<String>();

		// Proponente
		String proponente = getView().getProponente();
		if (proponente == null || proponente.trim().isEmpty())
			errors.add("Selezionare un gruppo proponente");

		else {
			ProponenteDTO proponenteDTO = new ProponenteDTO();
			proponenteDTO.setNomeGruppo(proponente);
			dto.setProponente(proponenteDTO);
		}

		// Oggetto
		String oggetto = getView().getOggetto();
		if (oggetto == null || oggetto.trim().isEmpty())
			errors.add("Oggetto proposta obbligatorio");
		else
			dto.setOggettoDocumento(oggetto);

		// Note
		String note = getView().getNote();
		if (note != null && !note.trim().isEmpty())
			dto.setNote(note);

		// Indirizzi Notifica
		List<String> indirizziNotifica = getView().getIndirizziNotifica();

		Set<String> set = new HashSet<String>(indirizziNotifica);
		if (set.size() < indirizziNotifica.size()) {
			errors.add("Ci sono duplicati tra gli indirizzi notifica");
		}

		String textIndirizziNotifica = getView().getTextFromInputListIndirizziNotifica();
		if (textIndirizziNotifica != null && !textIndirizziNotifica.trim().isEmpty() && !ValidationUtilities.validateEmailAddress(textIndirizziNotifica)) {
			errors.add("Indirizzo di notifica non valido");
		}
		dto.getIndirizziEmailNotifica().addAll(indirizziNotifica);

		// Destinatari
		if (destinatari.isEmpty()) {
			errors.add("Selezionare almeno un destinatario");

		} else {

			if (profilazioneUtenteHandler.getDatiUtente() != null && destinatari.contains(richiediFirmaTaskApiClient.getDestinatario(profilazioneUtenteHandler.getDatiUtente()))) {
				errors.add("Destinatario utente non valido");
			}

			if (proponente != null && !proponente.trim().isEmpty()) {
				for (DestinatarioDTO destinatarioDTO : destinatari) {
					if (destinatarioDTO instanceof DestinatarioGruppoDTO)
						if (((DestinatarioGruppoDTO) destinatarioDTO).getNomeGruppoDisplay().equalsIgnoreCase(proponente)) {
							errors.add("Non è possibile inoltrare una richiesta al gruppo proponente");
							break;
						}
				}
			}

			dto.getDestinatari().addAll(destinatari);
		}

		// Tipo approvazione
		TipoPropostaTaskFirmaDTO tipoRichiesta = getView().getTipoRichiesta();
		if (tipoRichiesta == null)
			errors.add("Tipo proposta obbligatorio");
		else
			dto.setTipoRichiesta(tipoRichiesta);

		// Mittente originale
		String mittenteOriginale = getView().getMittenteOriginale();
		if (mittenteOriginale != null && !mittenteOriginale.trim().isEmpty()) {
			dto.setMittenteOriginale(mittenteOriginale);
		}

		// Data/Ora scadenza
		if (getView().getDataScadenza() == null && (getView().getOraScadenza() != null && getView().getMinutoScadenza() != null)) {
			errors.add("Specificare una data di scadenza della proposta");

		} else if (getView().getDataScadenza() != null) {

			dto.setDataScadenza(getView().getDataScadenza());

			if (getView().getOraScadenza() != null && getView().getMinutoScadenza() != null) {
				Date date = new Date();
				date.setTime(getView().getDataScadenza().getTime() + getView().getOraScadenza() * HOURS + getView().getMinutoScadenza() * MINUTES);

				if (date.before(new Date()))
					errors.add("La data di scadenza deve essere successiva alla data odierna");

				else {
					dto.setOraScadenza(getView().getOraScadenza());
					dto.setMinutoScadenza(getView().getMinutoScadenza());
				}

			} else if (getView().getDataScadenza() != null && getView().getDataScadenza().before(new Date()) && !CalendarUtil.isSameDate(getView().getDataScadenza(), new Date())) {
				errors.add("La data di scadenza deve essere successiva alla data odierna");
			}
		}

		return errors;
	}

	/* Commands */
	private class AnnullaCommand implements Command {

		@Override
		public void execute() {
			RichiediFirmaVistoFineEvent.fire(RichiediVistoFirmaPresenter.this, openingRequestor);
		}
	}

	private class ConfermaCommand implements Command {
		@Override
		public void execute() {
			RichiediFirmaVistoDTO dto = new RichiediFirmaVistoDTO();
			List<String> errors = getDatiRichiesta(dto);

			if (errors.size() > 0)
				getView().showErrors(errors);
			else
				RichiediFirmaVistoFineEvent.fire(RichiediVistoFirmaPresenter.this, dto, openingRequestor);
		}
	}

	private class EliminaUtenteCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Utente> {

		@Override
		public Void exe(Utente t) {

			DestinatarioDTO destinatario = richiediFirmaTaskApiClient.getDestinatario(t);

			if (destinatari.contains(destinatario))
				destinatari.remove(destinatario);

			return null;
		}
	}

	private class EliminaGruppoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AnagraficaRuolo> {

		@Override
		public Void exe(AnagraficaRuolo t) {
			DestinatarioDTO destinatario = richiediFirmaTaskApiClient.getDestinatario(t);

			if (destinatari.contains(destinatario))
				destinatari.remove(destinatario);

			return null;
		}
	}

	private class ElencoUtentiSelectionHandler implements SelectionHandler<SuggestOracle.Suggestion> {

		@Override
		public void onSelection(SelectionEvent<Suggestion> event) {
			Utente utenteSelezionato = getView().getUtenteSelezionato();

			if (utenteSelezionato != null) {
				DestinatarioDTO dest = richiediFirmaTaskApiClient.getDestinatario(utenteSelezionato);
				getView().enableAggiungiUtenteButton(!destinatari.contains(dest));

			}
		}
	}

	private class ElencoGruppiSelectionHandler implements SelectionHandler<SuggestOracle.Suggestion> {

		@Override
		public void onSelection(SelectionEvent<Suggestion> event) {
			AnagraficaRuolo ruoloSelezionato = getView().getRuoloSelezionato();

			if (ruoloSelezionato != null) {
				DestinatarioDTO dest = richiediFirmaTaskApiClient.getDestinatario(ruoloSelezionato);
				getView().enableAggiungiRuoloButton(!destinatari.contains(dest));
			}
		}
	}

	private class ElencoUtentiClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			Utente utente = getView().getUtenteSelezionato();

			if (utente != null) {
				DestinatarioDTO dest = richiediFirmaTaskApiClient.getDestinatario(utente);

				if (!destinatari.contains(dest)) {
					getView().addUtenteWidget(utente, true);
					destinatari.add(dest);
					getView().enableAggiungiRuoloButton(false);
					getView().clearElencoUtentiSuggestBox();
				}
			}
		}
	}

	private class ElencoGruppiClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			AnagraficaRuolo ruoloSelezionato = getView().getRuoloSelezionato();

			if (ruoloSelezionato != null) {
				DestinatarioDTO dest = richiediFirmaTaskApiClient.getDestinatario(ruoloSelezionato);

				if (!destinatari.contains(dest)) {
					getView().addGruppoWidget(ruoloSelezionato, true);
					destinatari.add(dest);
					getView().enableAggiungiUtenteButton(false);
					getView().clearElencoGruppiSuggestBox();
					getView().aggiungiIndirizzoNotifica(ruoloSelezionato);
				}
			}

		}
	}

	private class OnChangeTipoDestinatarioCommand implements Command {

		@Override
		public void execute() {
			destinatari.clear();
		}
	}
}

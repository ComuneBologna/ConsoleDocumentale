package it.eng.portlet.consolepec.gwt.client.presenter.cartellafirma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.AccettaFirmaVistoFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.AccettaFirmaVistoInizioEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.AccettaFirmaVistoInizioEvent.AccettaFirmaVistoInizioHandler;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.DiniegaFirmaVistoFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.DiniegaFirmaVistoInizioEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.DiniegaFirmaVistoInizioEvent.DiniegaFirmaVistoInizioHandler;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.RispondiParereFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.RispondiParereInizioEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.RispondiParereInizioEvent.RispondiParereInizioHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaWizardApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaWizardApiClient.OperazioneWizardTaskFirma;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaWizardApiClient.UploadAllegatiCallback;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent.UploadStatus;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.RispostaFileUploaderDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.ProponenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoRispostaParereDTO;

/**
 *
 * @author biagiot
 *
 */
public class StepOperazioneWizardTaskFirmaPresenter extends Presenter<StepOperazioneWizardTaskFirmaPresenter.MyView, StepOperazioneWizardTaskFirmaPresenter.MyProxy> implements AccettaFirmaVistoInizioHandler, DiniegaFirmaVistoInizioHandler, RispondiParereInizioHandler {

	private EventBus eventBus;
	private CartellaFirmaWizardApiClient cartellaFirmaWizardApiClient;

	private Set<DocumentoFirmaVistoDTO> documentiFirmaVisto;
	private String ruoloSelezionato;
	private boolean isTaskRuolo = false;
	private List<FileDTO> fileCaricati = new ArrayList<FileDTO>();

	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Inject
	public StepOperazioneWizardTaskFirmaPresenter(EventBus eventBus, MyView view, MyProxy proxy, CartellaFirmaWizardApiClient cartellaFirmaWizardApiClient,
			ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.cartellaFirmaWizardApiClient = cartellaFirmaWizardApiClient;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	public interface MyView extends View {
		void configuraPannelloProponentiGruppi(Map<ProponenteDTO, Set<AllegatoDTO>> map);

		void setAnnullaCommand(Command command);

		void setConfermaCommand(Command command);

		void disabilitaRiassegnazione();

		boolean isRiassegna();

		void setTitolo(String titolo);

		void reset();

		void impostaRiassegnazioneValore(boolean value);

		void mostraPannelloTipoRisposta();

		TipoRispostaParereDTO getTipoRisposta();

		void configuraSceltaRuolo(Set<String> ruoliAbiltiati, it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> selectCommand);

		void showErrors(List<String> errors);

		void sendDownload(SafeUri uri);

		void startUpload();

		void setUploadAllegatoCommand(UploadAllegatoCommand uploadAllegatoCommand);

		void setEliminaAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<FileDTO>> command);

		void cancellaListaAllegatiSelezionati();

		Set<FileDTO> getAllegatiSelezionati();

		void mostraAllegati(List<FileDTO> allegati);

		String getMotivazione();

		void setMotivazione(String motivazione);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<StepOperazioneWizardTaskFirmaPresenter> {/**/}

	@Override
	@ProxyEvent
	public void onAccettaPropostaFirmaVisto(AccettaFirmaVistoInizioEvent event) {
		init(event.getDocumentiFirmaVisto(), event.getOperazioneRichiesta(), event.isRiassegnaEnabled(), event.isTaskRuoli(), event.getRuoliAbilitati(), event.getFileDaAllegare(),
				event.getMotivazione());
	}

	@Override
	@ProxyEvent
	public void onRispondiParereInizio(RispondiParereInizioEvent event) {
		init(event.getDocumentiFirmaVisto(), OperazioneWizardTaskFirma.RISPOSTA_PARERE, event.isRiassegnaEnabled(), event.isTaskRuoli(), event.getRuoliAbilitati(), event.getFileDaAllegare(),
				event.getMotivazione());
	}

	@Override
	@ProxyEvent
	public void onDiniegaPropostaFirmaVisto(DiniegaFirmaVistoInizioEvent event) {
		init(event.getDocumentiFirmaVisto(), event.getOperazioneRichiesta(), event.isRiassegnaEnabled(), event.isTaskRuoli(), event.getRuoliAbilitati(), event.getFileDaAllegare(),
				event.getMotivazione());
	}

	private void init(Set<DocumentoFirmaVistoDTO> documentiFirmaVisto, OperazioneWizardTaskFirma operazioneRichiesta, Boolean riassegnaEnabled, Boolean isTaskRuoli, Set<String> ruoliAbilitati,
			List<FileDTO> fileDaAllegare, String motivazione) {
		ShowMessageEvent showMessageEvent = new ShowMessageEvent();
		showMessageEvent.setMessageDropped(true);
		eventBus.fireEvent(showMessageEvent);

		this.documentiFirmaVisto = documentiFirmaVisto;

		getView().reset();
		ruoloSelezionato = null;

		if (motivazione != null) {
			getView().setMotivazione(motivazione);
		}

		boolean riassegnaDefault = (profilazioneUtenteHandler != null && profilazioneUtenteHandler.getPreferenzeUtente() != null
				&& profilazioneUtenteHandler.getPreferenzeUtente().getPreferenzeCartellaAttivita() != null)
						? profilazioneUtenteHandler.getPreferenzeUtente().getPreferenzeCartellaAttivita().isRiassegnaDefault() : true;

		getView().impostaRiassegnazioneValore(!OperazioneWizardTaskFirma.DINIEGO.equals(operazioneRichiesta) && riassegnaDefault);

		if (!riassegnaEnabled) {
			getView().disabilitaRiassegnazione();
		}

		switch (operazioneRichiesta) {
		case VISTO:
		case FIRMA:
			getView().setTitolo("Accetta");
			getView().setConfermaCommand(new ConfermaAccettaCommand());
			getView().setAnnullaCommand(new AnnullaAccettaCommand());
			break;

		case RISPOSTA_PARERE:
			getView().setTitolo("Rispondi");
			getView().setConfermaCommand(new ConfermaRispostaCommand());
			getView().setAnnullaCommand(new AnnullaRispostaCommand());
			getView().mostraPannelloTipoRisposta();
			break;

		case DINIEGO:
			getView().setTitolo("Diniega");
			getView().setConfermaCommand(new ConfermaDiniegaCommand());
			getView().setAnnullaCommand(new AnnullaDiniegaCommand());
			break;

		default:
			ShowMessageEvent event = new ShowMessageEvent();
			event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			eventBus.fireEvent(event);
			break;
		}

		if (isTaskRuoli) {
			isTaskRuolo = true;

			if (ruoliAbilitati.size() > 1) {
				getView().configuraSceltaRuolo(ruoliAbilitati, new SelectRuoloCommand());

			} else {
				ruoloSelezionato = ruoliAbilitati.iterator().next();
			}
		}

		if (fileDaAllegare != null) {
			fileCaricati = fileDaAllegare;
			getView().mostraAllegati(fileDaAllegare);
		}

		configuraPannelloGruppiProponenti();
		revealInParent();
	}

	private void configuraPannelloGruppiProponenti() {
		Map<ProponenteDTO, Set<AllegatoDTO>> map = new HashMap<ProponenteDTO, Set<AllegatoDTO>>();

		for (DocumentoFirmaVistoDTO documento : documentiFirmaVisto) {

			if (map.containsKey(documento.getGruppoProponente())) {
				map.get(documento.getGruppoProponente()).add(documento.getAllegato());

			} else {
				Set<AllegatoDTO> allegati = new HashSet<AllegatoDTO>();
				allegati.add(documento.getAllegato());
				map.put(documento.getGruppoProponente(), allegati);
			}
		}

		getView().configuraPannelloProponentiGruppi(map);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setUploadAllegatoCommand(new UploadAllegatoCommand());
		getView().setEliminaAllegatoCommand(new EliminaAllegatoCommand());
	}

	@Override
	protected void onHide() {
		super.onHide();
		getView().reset();
		ruoloSelezionato = null;
		isTaskRuolo = false;

		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
	}

	public class ConfermaAccettaCommand implements Command {

		@Override
		public void execute() {
			if (isTaskRuolo && ruoloSelezionato == null) {
				getView().showErrors(Arrays.asList("Selezionare un gruppo"));
			} else {
				AccettaFirmaVistoFineEvent.fire(StepOperazioneWizardTaskFirmaPresenter.this, ruoloSelezionato, getView().isRiassegna(), fileCaricati, getView().getMotivazione());
			}
		}
	}

	public class AnnullaAccettaCommand implements Command {

		@Override
		public void execute() {
			eliminaAllegati(new HashSet<FileDTO>(fileCaricati));
			AccettaFirmaVistoFineEvent.fire(StepOperazioneWizardTaskFirmaPresenter.this);
		}
	}

	public class ConfermaDiniegaCommand implements Command {

		@Override
		public void execute() {

			String motivazione = getView().getMotivazione();

			if (motivazione == null) {
				getView().showErrors(Arrays.asList("Inserire una motivazione"));

			} else {
				if (isTaskRuolo && ruoloSelezionato == null) {
					getView().showErrors(Arrays.asList("Selezionare un gruppo"));

				} else {
					DiniegaFirmaVistoFineEvent.fire(StepOperazioneWizardTaskFirmaPresenter.this, ruoloSelezionato, getView().isRiassegna(), fileCaricati, motivazione);
				}
			}

		}
	}

	public class AnnullaDiniegaCommand implements Command {

		@Override
		public void execute() {
			eliminaAllegati(new HashSet<FileDTO>(fileCaricati));
			DiniegaFirmaVistoFineEvent.fire(StepOperazioneWizardTaskFirmaPresenter.this);
		}
	}

	public class ConfermaRispostaCommand implements Command {

		@Override
		public void execute() {

			if (isTaskRuolo && ruoloSelezionato == null) {
				getView().showErrors(Arrays.asList("Selezionare un gruppo"));

			} else if (getView().getTipoRisposta() == null) {
				getView().showErrors(Arrays.asList("Tipo risposta obbligatorio"));

			} else {
				TipoRispostaParereDTO tipoRisposta = getView().getTipoRisposta();
				String motivazione = getView().getMotivazione();

				if ((TipoRispostaParereDTO.RISPOSTA_NEGATIVA.equals(tipoRisposta) || TipoRispostaParereDTO.RISPOSTA_RIFIUTATA.equals(tipoRisposta)
						|| TipoRispostaParereDTO.RISPOSTA_POSITIVA_CON_PRESCRIZIONI.equals(tipoRisposta) || TipoRispostaParereDTO.RISPOSTA_SOSPESA.equals(tipoRisposta)) && motivazione == null) {
					getView().showErrors(Arrays.asList("Inserire una motivazione"));

				} else {
					RispondiParereFineEvent.fire(StepOperazioneWizardTaskFirmaPresenter.this, ruoloSelezionato, getView().getTipoRisposta(), getView().isRiassegna(), fileCaricati, motivazione);
				}
			}
		}
	}

	public class AnnullaRispostaCommand implements Command {

		@Override
		public void execute() {
			eliminaAllegati(new HashSet<FileDTO>(fileCaricati));
			RispondiParereFineEvent.fire(StepOperazioneWizardTaskFirmaPresenter.this);
		}
	}

	public class SelectRuoloCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> {

		@Override
		public Void exe(String t) {
			ruoloSelezionato = t;
			return null;
		}
	}

	public class UploadAllegatoCommand {

		private String idUpload = DOM.createUniqueId();

		public void onFileSelected(String fileName) {
			getEventBus().fireEvent(new UploadEvent(idUpload, UploadStatus.START));
			StepOperazioneWizardTaskFirmaPresenter.this.getView().startUpload();
		}

		public void onFileUploaded(RispostaFileUploaderDTO dto) {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);

			if (dto.isError()) {
				ShowMessageEvent eventErr = new ShowMessageEvent();
				eventErr.setErrorMessage(dto.getMessError());
				getEventBus().fireEvent(eventErr);
				getEventBus().fireEvent(new UploadEvent(idUpload, UploadStatus.ERROR));

			} else {

				cartellaFirmaWizardApiClient.getFileDaAllegare(dto.getTmpFiles(), fileCaricati, new UploadAllegatiCallback() {

					@Override
					public void onError(String message) {
						ShowMessageEvent eventErr = new ShowMessageEvent();
						eventErr.setErrorMessage(message);
						getEventBus().fireEvent(eventErr);
						getEventBus().fireEvent(new UploadEvent(idUpload, UploadStatus.ERROR));
					}

					@Override
					public void onAllegatiUploaded(List<FileDTO> allegati) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setMessageDropped(true);
						getEventBus().fireEvent(event);

						for (FileDTO allegato : allegati) {
							if (!fileCaricati.contains(allegato)) {
								fileCaricati.add(allegato);
							}
						}

						getView().mostraAllegati(fileCaricati);
						getEventBus().fireEvent(new UploadEvent(idUpload, UploadStatus.DONE));
					}
				});
			}
		}
	}

	private void eliminaAllegati(Set<FileDTO> t) {
		for (FileDTO file : t) {
			if (fileCaricati.contains(file)) {
				fileCaricati.remove(file);
			}

			cartellaFirmaWizardApiClient.eliminaFileDaAllegare(file.getPath());
		}
	}

	private class EliminaAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<FileDTO>> {

		@Override
		public Void exe(Set<FileDTO> t) {
			eliminaAllegati(t);

			getView().mostraAllegati(fileCaricati);
			return null;
		}
	}
}

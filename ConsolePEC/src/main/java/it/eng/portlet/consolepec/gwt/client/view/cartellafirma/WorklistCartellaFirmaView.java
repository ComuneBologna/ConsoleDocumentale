package it.eng.portlet.consolepec.gwt.client.view.cartellafirma;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaRicercaApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaWizardApiClient;
import it.eng.portlet.consolepec.gwt.client.presenter.cartellafirma.WorklistCartellaFirmaPresenter.MyView;
import it.eng.portlet.consolepec.gwt.client.widget.CustomPager;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.cartellafirma.FormRicercaCartellaFirma;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ResPager;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistCartellaFirmaStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistCartellaFirmaStrategy.CheckRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoPropostaTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoStatoTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoUtenteTaskFirmaDTO;

/**
 *
 * @author biagiot
 *
 */
public class WorklistCartellaFirmaView extends ViewImpl implements MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, WorklistCartellaFirmaView> {/**/}

	@UiField(provided = true) DataGridWidget<DocumentoFirmaVistoDTO> dataGrid = new DataGridWidget<DocumentoFirmaVistoDTO>(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA,
			WorklistCartellaFirmaStrategy.DOCUMENTO_FIRMA_VISTO_KEY_PROVIDER, WorklistCartellaFirmaStrategy.DOCUMENTO_FIRMA_VISTO_PROVIDER);
	@UiField(provided = true) CustomPager pager;
	@UiField HTMLPanel dettaglioPanel;
	@UiField Button accettaButton;
	@UiField Button firmaButton;
	@UiField Button diniegaButton;
	@UiField Button ritiraButton;
	@UiField Button rispondiButton;
	@UiField Button evadiButton;
	@UiField HTMLPanel pannelloRicerca;
	@UiField(provided = true) MessageAlertWidget messageWidget;
	@UiField HeadingElement titolo;
	@UiField DownloadAllegatoWidget downloadWidget;
	@UiField DivElement documentiSelezionatiDiv;

	private FormRicercaCartellaFirma formRicercaCartellaFirma;
	private CartellaFirmaWizardApiClient cartellaFirmaWizardApiClient;
	private WorklistCartellaFirmaStrategy strategy;
	private Map<String, FormRicercaCartellaFirma> formRicercaWorklistMap = new HashMap<String, FormRicercaCartellaFirma>();
	private CartellaFirmaRicercaApiClient cartellaFirmaRicercaApiClient;
	private TipoUtenteTaskFirmaDTO tipoRicerca;

	@Inject
	public WorklistCartellaFirmaView(final Binder binder, final EventBus eventBus, CartellaFirmaRicercaApiClient cartellaFirmaRicercaApiClient,
			CartellaFirmaWizardApiClient cartellaFirmaWizardApiClient, ConfigurazioniHandler configurazioniHandler) {
		pager = new CustomPager(SimplePager.TextLocation.CENTER, ResPager.CSS, false, 0, true, configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist());
		this.cartellaFirmaRicercaApiClient = cartellaFirmaRicercaApiClient;
		this.cartellaFirmaWizardApiClient = cartellaFirmaWizardApiClient;
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		titolo.scrollIntoView();
		Window.scrollTo(0, 0);
	}

	@Override
	public void initForm(AnagraficaWorklist worklistConfiguration, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler, Command cercaCommand) {
		if (!formRicercaWorklistMap.containsKey(worklistConfiguration.getNome())) {
			formRicercaCartellaFirma = new FormRicercaCartellaFirma(cartellaFirmaRicercaApiClient);
			formRicercaWorklistMap.put(worklistConfiguration.getNome(), formRicercaCartellaFirma);
			pannelloRicerca.clear();
			pannelloRicerca.add(formRicercaCartellaFirma);

			formRicercaCartellaFirma.initForm(configurazioniHandler, profilazioneUtenteHandler, cercaCommand);
			pager.firstPage();
		}

		pannelloRicerca.clear();
		FormRicercaCartellaFirma formRicerca = formRicercaWorklistMap.get(worklistConfiguration.getNome());
		pannelloRicerca.add(formRicerca);

		if (formRicercaCartellaFirma != formRicerca) pager.firstPage();

		formRicercaCartellaFirma = formRicerca;
	}

	@Override
	public void initWorklist(WorklistCartellaFirmaStrategy strategy, final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> accettaCommand,
			final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> firmaCommand,
			final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> diniegaCommand,
			final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> ritiraCommand,
			final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> rispondiCommand,
			final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<DocumentoFirmaVistoDTO>> evadiCommand, final Command selezioneDocumentoCommand) {
		disabilitaPulsanti();

		this.accettaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				accettaCommand.exe(getDocumentiFirmaVistoSelezionati());
			}
		});

		this.firmaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				firmaCommand.exe(getDocumentiFirmaVistoSelezionati());
			}
		});

		this.diniegaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				diniegaCommand.exe(getDocumentiFirmaVistoSelezionati());
			}
		});

		this.rispondiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				rispondiCommand.exe(getDocumentiFirmaVistoSelezionati());

			}
		});

		this.ritiraButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ritiraCommand.exe(getDocumentiFirmaVistoSelezionati());

			}
		});

		this.evadiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				evadiCommand.exe(getDocumentiFirmaVistoSelezionati());
			}
		});

		this.strategy = strategy;
		this.strategy.configuraGrid(dataGrid, pager);
		this.strategy.addCheckRigaEventListener(new CheckRigaEventListener() {

			@Override
			public void onCheckRiga(DocumentoFirmaVistoDTO documentoFirmaVisto, boolean checked) {
				selezioneDocumentoCommand.execute();
			}
		});
	}

	public Set<DocumentoFirmaVistoDTO> getDocumentiFirmaVistoSelezionati() {
		return strategy.getDocumentiFirmaVistoSelezionati();
	}

	@Override
	public FormRicercaCartellaFirma getFormRicerca() {
		return formRicercaCartellaFirma;
	}

	@Override
	public void impostaTitolo(String titolo) {
		this.titolo.setInnerText(titolo);
	}

	@Override
	public void impostaAbilitazioniPulsantiera() {
		Set<DocumentoFirmaVistoDTO> documentiSelezionati = getDocumentiFirmaVistoSelezionati();

		if (documentiSelezionati.size() == 0) {
			disabilitaPulsanti();
		} else {
			configuraPulsanti(documentiSelezionati);
		}
	}

	private void configuraPulsanti(final Set<DocumentoFirmaVistoDTO> documentiSelezionati) {
		documentiSelezionatiDiv.getStyle().setVisibility(Visibility.VISIBLE);

		if (documentiSelezionati.size() == 1) {
			documentiSelezionatiDiv.setInnerText("" + 1 + " documento selezionato");
		} else {
			documentiSelezionatiDiv.setInnerText("" + documentiSelezionati.size() + " documenti selezionati");
		}

		boolean diniega = true;
		boolean accetta = true;
		boolean firma = true;
		boolean ritira = true;
		boolean rispondi = true;
		boolean evadi = true;

		for (DocumentoFirmaVistoDTO documento : documentiSelezionati) {

			if (TipoStatoTaskFirmaDTO.CONCLUSO.equals(documento.getStatoRichiesta().getTipologiaStato())) {
				diniega = false;
				accetta = false;
				firma = false;
				ritira = false;
				rispondi = false;

				if (!evadi) break;

			} else if (TipoStatoTaskFirmaDTO.IN_APPROVAZIONE.equals(documento.getStatoRichiesta().getTipologiaStato())) {
				evadi = false;

				if (TipoPropostaTaskFirmaDTO.FIRMA.equals(documento.getTipoRichiesta())) {
					accetta = false;
					rispondi = false;

				} else if (TipoPropostaTaskFirmaDTO.VISTO.equals(documento.getTipoRichiesta())) {
					firma = false;
					rispondi = false;

				} else if (TipoPropostaTaskFirmaDTO.PARERE.equals(documento.getTipoRichiesta())) {
					firma = false;
					accetta = false;
					diniega = false;
				}

				if (!firma && !accetta && !rispondi && !diniega) break;
			}
		}

		boolean operazioniDestinatarioAbilitate = operazioniDestinatarioAbilitate(documentiSelezionati);
		boolean operazioniProponenteAbilitate = operazioniProponenteAbilitate(documentiSelezionati);
		boolean checkDestinatariGruppi = true;

		if (cartellaFirmaWizardApiClient.checkDestinatariGruppo(documentiSelezionati)) {
			checkDestinatariGruppi = cartellaFirmaWizardApiClient.operazioniDestinariAbilitate(documentiSelezionati);
		}

		/*
		 * Operazioni destinatario
		 */
		diniegaButton.setEnabled(diniega && tipoRicerca.equals(TipoUtenteTaskFirmaDTO.DESTINATARIO)//
				&& cartellaFirmaWizardApiClient.operazioniDestinariAbilitate(documentiSelezionati) && checkDestinatariGruppi//
				&& operazioniDestinatarioAbilitate && cartellaFirmaWizardApiClient.isOperazioneAbilitata(documentiSelezionati));

		accettaButton.setEnabled(accetta && tipoRicerca.equals(TipoUtenteTaskFirmaDTO.DESTINATARIO)//
				&& cartellaFirmaWizardApiClient.operazioniDestinariAbilitate(documentiSelezionati) && checkDestinatariGruppi//
				&& operazioniDestinatarioAbilitate && cartellaFirmaWizardApiClient.isOperazioneAbilitata(documentiSelezionati));

		firmaButton.setEnabled(firma && tipoRicerca.equals(TipoUtenteTaskFirmaDTO.DESTINATARIO)//
				&& cartellaFirmaWizardApiClient.operazioniDestinariAbilitate(documentiSelezionati) && checkDestinatariGruppi//
				&& operazioniDestinatarioAbilitate && cartellaFirmaWizardApiClient.isOperazioneAbilitata(documentiSelezionati));

		rispondiButton.setEnabled(rispondi && tipoRicerca.equals(TipoUtenteTaskFirmaDTO.DESTINATARIO)//
				&& cartellaFirmaWizardApiClient.operazioniDestinariAbilitate(documentiSelezionati) && checkDestinatariGruppi//
				&& operazioniDestinatarioAbilitate && cartellaFirmaWizardApiClient.isOperazioneAbilitata(documentiSelezionati));

		/*
		 * Operazioni proponente
		 */
		ritiraButton.setEnabled(ritira && tipoRicerca.equals(TipoUtenteTaskFirmaDTO.PROPONENTE) && operazioniProponenteAbilitate);

		evadiButton.setEnabled(evadi && tipoRicerca.equals(TipoUtenteTaskFirmaDTO.PROPONENTE) && operazioniProponenteAbilitate);
	}

	private boolean operazioniDestinatarioAbilitate(Set<DocumentoFirmaVistoDTO> documenti) {
		for (DocumentoFirmaVistoDTO documento : documenti)
			if (!documento.isOperazioniDestinatarioAbilitate()) return false;
		return true;
	}

	private boolean operazioniProponenteAbilitate(Set<DocumentoFirmaVistoDTO> documenti) {
		for (DocumentoFirmaVistoDTO documento : documenti)
			if (!documento.isOperazioniProponenteAbilitate()) return false;
		return true;
	}

	private void disabilitaPulsanti() {
		documentiSelezionatiDiv.getStyle().setVisibility(Visibility.HIDDEN);
		documentiSelezionatiDiv.setInnerText("");
		diniegaButton.setEnabled(false);
		accettaButton.setEnabled(false);
		firmaButton.setEnabled(false);
		rispondiButton.setEnabled(false);
		ritiraButton.setEnabled(false);
		evadiButton.setEnabled(false);
	}

	@Override
	public void sendDownload(SafeUri uri) {
		downloadWidget.sendDownload(uri);
	}

	@Override
	public void espandiRiga(DocumentoFirmaVistoDTO documento) {
		strategy.espandiRiga(documento);
	}

	@Override
	public void nascondiRiga(DocumentoFirmaVistoDTO documento) {
		dataGrid.chiudiRigaByKey(DocumentoFirmaVistoDTO.getKey(documento));
	}

	@Override
	public void showFormErrors(List<String> errors) {
		getFormRicerca().showErrors(errors);
	}

	@Override
	public void setTipoRicerca(TipoUtenteTaskFirmaDTO tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}
}